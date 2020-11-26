package com.ort.lastwolf.domain.service.daychange

import com.ort.dbflute.allcommon.CDef
import com.ort.lastwolf.domain.model.commit.Commits
import com.ort.lastwolf.domain.model.daychange.DayChange
import com.ort.lastwolf.domain.model.message.Message
import com.ort.lastwolf.domain.model.village.Village
import com.ort.lastwolf.domain.model.village.vote.VillageVotes
import com.ort.lastwolf.domain.service.ability.AttackDomainService
import com.ort.lastwolf.domain.service.ability.DivineDomainService
import com.ort.lastwolf.domain.service.ability.PsychicDomainService
import com.ort.lastwolf.domain.service.vote.VoteDomainService
import com.ort.lastwolf.fw.LastwolfDateUtil
import org.springframework.stereotype.Service

@Service
class ProgressDomainService(
    private val executeDomainService: ExecuteDomainService,
    private val psychicDomainService: PsychicDomainService,
    private val divineDomainService: DivineDomainService,
    private val attackDomainService: AttackDomainService,
    private val miserableDeathDomainService: MiserableDeathDomainService,
    private val suddenlyDeathDomainService: SuddenlyDeathDomainService,
    private val epilogueDomainService: EpilogueDomainService,
    private val voteDomainService: VoteDomainService
) {

    fun addDayIfNeeded(dayChange: DayChange, commits: Commits): DayChange {
        // 日付更新の必要がなかったら終了
        if (!shouldForward(dayChange.village, commits, dayChange.votes)) return dayChange
        // 日付追加
        return addNewDay(dayChange).setIsChange(dayChange)
    }

    fun dayChange(
        beforeDayChange: DayChange
    ): DayChange {
        val latestDay = beforeDayChange.village.days.latestDay()
        return when {
            latestDay.isNoonTime() -> toNoonTime(beforeDayChange)
            latestDay.isVoteTime() -> toVoteTime(beforeDayChange)
            latestDay.isNightTime() -> toNightTime(beforeDayChange)
            else -> beforeDayChange
        }
    }

    // ===================================================================================
    //                                                                        Assist Logic
    //                                                                        ============
    // 日付を進める必要があるか
    private fun shouldForward(village: Village, commits: Commits, votes: VillageVotes): Boolean {
        val latestDay = village.days.latestDay()
        if (isAfterDaychangeDatetime(village)) return true
        return when {
            latestDay.isNoonTime() -> allCommitted(village, commits) // 全員コミットしている
            latestDay.isVoteTime() -> allVoted(village, votes) // 全員投票している
            else -> false
        }
    }

    private fun isAfterDaychangeDatetime(village: Village): Boolean =
        !LastwolfDateUtil.currentLocalDateTime().isBefore(village.days.latestDay().endDatetime)

    private fun allCommitted(village: Village, commits: Commits): Boolean {
        if (!village.setting.rules.availableCommit) return false
        // ダミーを除く最新日の生存者数
        val livingPersonCount = village.notDummyParticipants().filterAlive().count
        // コミット数
        val commitCount = commits.list.filter { it.villageDayId == village.days.latestDay().id && it.isCommitting }.size

        return livingPersonCount == commitCount
    }

    private fun allVoted(village: Village, votes: VillageVotes): Boolean {
        // ダミーを除く最新日の生存者数
        val livingPersonCount = village.notDummyParticipants().filterAlive().count
        // 投票数
        val voteCount = votes.list.filter { it.villageDayId == village.days.latestDay().id }.size

        return livingPersonCount == voteCount
    }

    // 日付追加
    private fun addNewDay(dayChange: DayChange): DayChange {
        val latestDay = dayChange.village.days.latestDay()
        return if (latestDay.isVoteTime()) {
            // 3回目の投票か
            val isThirdVote = latestDay.noonNight.toCdef() == CDef.Noonnight.投票3回目
            // 最多票が複数いるか
            val existsMultiMaxVotedPlayer = executeDomainService.existsMultiMaxVotedPlayer(dayChange)

            dayChange.copy(
                village = dayChange.village.addNewDay(
                    toNextVote = existsMultiMaxVotedPlayer,
                    isEpilogue = isThirdVote && existsMultiMaxVotedPlayer
                )
            ).setIsChange(dayChange)

        } else {
            dayChange.copy(
                village = dayChange.village.addNewDay()
            ).setIsChange(dayChange)
        }
    }

    // 昼時間に遷移
    private fun toNoonTime(beforeDayChange: DayChange): DayChange {
        // 突然死
        var dayChange = suddenlyDeathDomainService.nightSuddenlyDeath(beforeDayChange)

        // 占い（メッセージは夜時間に追加してあるので呪殺のみ）
        dayChange = divineDomainService.divineKill(dayChange)

        // 襲撃
        dayChange = attackDomainService.attack(dayChange)

        // 無惨メッセージ
        dayChange = miserableDeathDomainService.processDayChangeAction(dayChange)

        // 勝敗
        dayChange = epilogueDomainService.transitionToEpilogueIfNeeded(dayChange)

        return dayChange.setIsChange(beforeDayChange)
    }

    // 投票に遷移（昼→1回目の投票、1→2回目の投票、2→3回目の投票）
    private fun toVoteTime(beforeDayChange: DayChange): DayChange {
        // 突然死
        var dayChange = suddenlyDeathDomainService.voteSuddenlyDeath(beforeDayChange)
        // 前回の投票結果メッセージ追加
        dayChange = addVoteResultMessageIfNeeded(dayChange)
        // 投票促しメッセージ追加
        dayChange = addVoteMessage(dayChange)

        return dayChange.setIsChange(beforeDayChange)
    }

    // 夜時間に遷移
    private fun toNightTime(beforeDayChange: DayChange): DayChange {
        // 突然死
        var dayChange = suddenlyDeathDomainService.voteSuddenlyDeath(beforeDayChange)

        // 最新日がエピローグだったら引き分け処理
        if (dayChange.village.days.latestDay().isEpilogue) {
            return epilogueDomainService.transitionToDrawEpilogue(dayChange).setIsChange(beforeDayChange)
        }

        // 処刑
        dayChange = executeDomainService.processDayChangeAction(dayChange)

        // 霊能
        dayChange = psychicDomainService.processDayChangeAction(dayChange)

        // 勝敗
        dayChange = epilogueDomainService.transitionToEpilogueIfNeeded(dayChange)

        return dayChange.setIsChange(beforeDayChange)
    }

    private fun addVoteResultMessageIfNeeded(dayChange: DayChange): DayChange {
        val village = dayChange.village
        if (village.days.latestDay().noonNight.toCdef() == CDef.Noonnight.投票1回目) {
            return dayChange
        }

        // 得票 key: target participant id, value: vote list
        val votedMap = dayChange.votes.toVotedMap(village)
        if (votedMap.isEmpty()) return dayChange // 全員突然死
        val message = voteDomainService.createEachVoteMessage(village, votedMap)

        return dayChange.copy(
            messages = dayChange.messages.add(message)
        )
    }

    private fun addVoteMessage(dayChange: DayChange): DayChange {
        return dayChange.copy(
            messages = dayChange.messages.add(createVoteMessage(dayChange.village))
        )
    }

    private fun createVoteMessage(village: Village): Message {
        return when (village.days.latestDay().noonNight.toCdef()) {
            CDef.Noonnight.投票1回目 -> Message.createPublicSystemMessage(
                text = "処刑したい人に投票してください。\n" +
                    "最多票が同数となった場合は再投票が行われます。\n" +
                    "3回目の投票で決着がつかなかった場合は引き分けとなります。\n" +
                    "現在1回目の投票です。",
                villageDayId = village.days.latestNoonDay().id
            )
            CDef.Noonnight.投票2回目 -> Message.createPublicSystemMessage(
                text = "最多票が同数となったため再投票となります。処刑したい人に投票してください。\n" +
                    "次も最多票が同数となった場合は3回目の投票が行われます。\n" +
                    "3回目の投票で決着がつかなかった場合は引き分けとなります。\n" +
                    "現在2回目の投票です。",
                villageDayId = village.days.latestNoonDay().id
            )
            CDef.Noonnight.投票3回目 -> Message.createPublicSystemMessage(
                text = "最多票が同数となったため再投票となります。処刑したい人に投票してください。\n" +
                    "この投票で最多票が同数となった場合は引き分けとなります。",
                villageDayId = village.days.latestNoonDay().id
            )
            else -> throw IllegalStateException("invalid noontime.")
        }
    }
}