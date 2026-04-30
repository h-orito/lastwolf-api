#!/usr/bin/env bash
set -euo pipefail

# PostToolUse hook: Edit/Write後に変更対象のソースセットだけktlintCheckを実行し、
# 違反があればadditionalContextとしてフィードバックする。
# 無限ループ防止: 同一ファイルに対して連続MAX_RETRIES回失敗したらスキップ。

MAX_RETRIES=3

# stdinからJSON入力を読み取る
INPUT=$(cat)

# ファイルパスを取得
FILE_PATH=$(echo "$INPUT" | jq -r '.tool_input.file_path // empty')
if [[ -z "$FILE_PATH" ]]; then
  exit 0
fi

# .kt / .kts ファイル以外はスキップ
if [[ "$FILE_PATH" != *.kt && "$FILE_PATH" != *.kts ]]; then
  exit 0
fi

# プロジェクトルートを取得
PROJECT_ROOT=$(echo "$INPUT" | jq -r '.cwd // empty')
if [[ -z "$PROJECT_ROOT" ]]; then
  exit 0
fi

# ステートディレクトリ（.context/はgitignore済み）
STATE_DIR="$PROJECT_ROOT/.context/ktlint-hook"
mkdir -p "$STATE_DIR"

# ファイルパスをプロジェクトルートからの相対パスに変換
REL_PATH="${FILE_PATH#"$PROJECT_ROOT"/}"

# --- 無限ループ防止 ---
if command -v md5sum >/dev/null 2>&1; then
  FILE_HASH=$(echo -n "$REL_PATH" | md5sum | cut -d' ' -f1)
else
  FILE_HASH=$(echo -n "$REL_PATH" | md5 -q)
fi
STATE_FILE="$STATE_DIR/$FILE_HASH"

FAIL_COUNT=0
if [[ -f "$STATE_FILE" ]]; then
  FAIL_COUNT=$(cat "$STATE_FILE")
fi

# --- ソースセットを特定（シングルモジュール） ---
GRADLE_TASK=""

if [[ "$REL_PATH" == src/main/* ]]; then
  GRADLE_TASK="ktlintMainSourceSetCheck"
elif [[ "$REL_PATH" == src/test/* ]]; then
  GRADLE_TASK="ktlintTestSourceSetCheck"
elif [[ "$FILE_PATH" == *.kts ]]; then
  GRADLE_TASK="ktlintKotlinScriptCheck"
else
  rm -f "$STATE_FILE"
  exit 0
fi

# --- リトライ上限チェック ---
if [[ "$FAIL_COUNT" -ge "$MAX_RETRIES" ]]; then
  cd "$PROJECT_ROOT"
  if ./gradlew "$GRADLE_TASK" > /dev/null 2>&1; then
    rm -f "$STATE_FILE"
  else
    jq -n '{"hookSpecificOutput": {"hookEventName": "PostToolUse", "additionalContext": "ktlint check skipped (max retries reached). Run ./gradlew ktlintCheck manually."}}'
  fi
  exit 0
fi

# --- ktlintCheck 実行 ---
cd "$PROJECT_ROOT"
./gradlew "$GRADLE_TASK" > /dev/null 2>&1 && CHECK_EXIT=0 || CHECK_EXIT=$?

if [[ "$CHECK_EXIT" -eq 0 ]]; then
  rm -f "$STATE_FILE"
  exit 0
fi

# --- 違反あり → レポートからエラー内容を取得 ---
REPORT_DIR="build/reports/ktlint/${GRADLE_TASK}"
REPORT_FILE=$(find "$REPORT_DIR" -name '*.txt' 2>/dev/null | head -1)

VIOLATIONS=""
if [[ -n "$REPORT_FILE" && -f "$REPORT_FILE" ]]; then
  VIOLATIONS=$(sed 's/\x1b\[[0-9;]*m//g' "$REPORT_FILE" | grep -v '^$' | grep -v '^Summary' | head -30)
fi

if [[ -z "$VIOLATIONS" ]]; then
  VIOLATIONS="ktlintCheck failed for $GRADLE_TASK but no detailed report found. Run ./gradlew $GRADLE_TASK to see details."
fi

echo $((FAIL_COUNT + 1)) > "$STATE_FILE"
REMAINING=$((MAX_RETRIES - FAIL_COUNT - 1))

jq -n \
  --arg violations "$VIOLATIONS" \
  --argjson remaining "$REMAINING" \
  '{
    hookSpecificOutput: {
      hookEventName: "PostToolUse",
      additionalContext: ("ktlint violations found. Please fix them.\nRemaining auto-check retries: " + ($remaining | tostring) + "\n\n" + $violations)
    }
  }'
