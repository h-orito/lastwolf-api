package com.ort.lastwolf.domain.service.daychange

import com.ort.lastwolf.domain.model.daychange.DayChange
import com.ort.lastwolf.domain.model.message.Message
import com.ort.lastwolf.domain.model.village.Village
import com.ort.lastwolf.domain.model.village.vote.VillageVote
import com.ort.lastwolf.domain.service.vote.VoteDomainService
import org.springframework.stereotype.Service

@Service
class ExecuteDomainService(
    private val voteDomainService: VoteDomainService
) {

    fun existsMultiMaxVotedPlayer(dayChange: DayChange): Boolean {
        val village = dayChange.village
        // 得票 key: target participant id, value: vote list
        val votedMap = dayChange.votes.list
            .filter {
                it.villageDayId == village.days.latestDay().id &&
                    village.participants.first(it.myselfId).isAlive()
            }
            .groupBy { it.targetId }

        if (votedMap.isEmpty()) return false

        // 得票数トップの参加者リスト
        val maxVotedParticipantIdList = filterMaxVotedParticipantList(votedMap)

        return maxVotedParticipantIdList.size > 1
    }

    fun processDayChangeAction(dayChange: DayChange): DayChange {
        var village = dayChange.village.copy()
        var messages = dayChange.messages.copy()

        // 得票 key: target participant id, value: vote list
        val votedMap = dayChange.votes.toVotedMap(village)
        if (votedMap.isEmpty()) return dayChange // 全員突然死

        // 個別投票メッセージ
        messages = messages.add(voteDomainService.createEachVoteMessage(village, votedMap))

        // 得票数トップの参加者リスト
        val maxVotedParticipantIdList = filterMaxVotedParticipantList(votedMap)
        // うち一人を処刑する（同数なら引き分け処理なので実質意味はない）
        maxVotedParticipantIdList.shuffled().first().let { executedParticipantId ->
            // 処刑（突然死していた場合は死因を上書きしない）
            val executedParticipant = village.participants.first(executedParticipantId)
            if (executedParticipant.isAlive()) {
                village = village.executeParticipant(executedParticipantId, village.days.latestDay())
            }
            // 処刑メッセージ
            messages = messages.add(createExecuteMessage(village, executedParticipantId))
        }
        return dayChange.copy(
            village = village,
            messages = messages
        ).setIsChange(dayChange)
    }

    // ===================================================================================
    //                                                                        Assist Logic
    //                                                                        ============
    // 得票数トップの参加者idリスト
    private fun filterMaxVotedParticipantList(votedMap: Map<Int, List<VillageVote>>): List<Int> {
        // 最大得票数
        val maxVotedCount = votedMap.maxBy { it.value.size }!!.value.size
        // 最大得票数の参加者idリスト
        return votedMap.filter { it.value.size == maxVotedCount }.keys.toList()
    }

    /**
     * 処刑メッセージ
     * @param village village
     * @param participantId 処刑される村参加者ID
     * @return 処刑メッセージ
     */
    private fun createExecuteMessage(
        village: Village,
        participantId: Int
    ): Message {
        val executedCharaName = village.participants.first(participantId).chara.name.name
        val message = "${executedCharaName}は村人達の手により処刑された。"
        return Message.createPublicSystemMessage(
            message,
            village.days.latestDay().id,
            isStrong = true
        )
    }
}