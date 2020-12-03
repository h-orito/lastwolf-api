package com.ort.lastwolf.domain.service.daychange

import com.ort.lastwolf.domain.model.commit.Commits
import com.ort.lastwolf.domain.model.daychange.DayChange
import org.springframework.stereotype.Service

@Service
class DayChangeDomainService(
    private val prologueDomainService: PrologueDomainService,
    private val rollCallingDomainService: RollCallingDomainService,
    private val progressDomainService: ProgressDomainService,
    private val epilogueDomainService: EpilogueDomainService
) {
    fun extendVillageIfNeeded(dayChange: DayChange): DayChange {
        val status = dayChange.village.status
        return when {
            status.isRecruiting() -> prologueDomainService.extendIfNeeded(dayChange)
            status.isRollCalling() -> rollCallingDomainService.extendIfNeeded(dayChange)
            else -> dayChange
        }
    }

    // コミットや時間経過で次の日に遷移させる場合は村日付を追加
    fun addDayIfNeeded(dayChange: DayChange, commits: Commits): DayChange {
        val status = dayChange.village.status
        return when {
            // プロローグ
            status.isRecruiting() -> dayChange // 手動でしか進まない
            // 点呼中
            status.isRollCalling() -> rollCallingDomainService.addDayIfNeeded(dayChange)
            // 進行中
            status.isProgress() -> progressDomainService.addDayIfNeeded(dayChange, commits)
            // エピローグ
            status.isSettled() -> epilogueDomainService.addDayIfNeeded(dayChange)
            // 終了後
            else -> dayChange
        }
    }

    // 日付変更処理
    fun process(dayChange: DayChange): DayChange {
        val status = dayChange.village.status
        return when {
            // プロローグ
            status.isRecruiting() -> dayChange
            // 点呼中
            status.isRollCalling() -> rollCallingDomainService.dayChange(dayChange)
            // 進行中
            status.isProgress() -> progressDomainService.dayChange(dayChange)
            // エピローグ
            status.isSettled() -> epilogueDomainService.toFinished(dayChange)
            // 終了後
            else -> dayChange
        }
    }
}