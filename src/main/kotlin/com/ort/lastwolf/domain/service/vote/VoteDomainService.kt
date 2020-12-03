package com.ort.lastwolf.domain.service.vote

import com.ort.lastwolf.domain.model.charachip.Chara
import com.ort.lastwolf.domain.model.message.Message
import com.ort.lastwolf.domain.model.myself.participant.VillageVoteSituation
import com.ort.lastwolf.domain.model.village.Village
import com.ort.lastwolf.domain.model.village.participant.VillageParticipant
import com.ort.lastwolf.domain.model.village.vote.VillageVote
import com.ort.lastwolf.domain.model.village.vote.VillageVotes
import com.ort.lastwolf.fw.exception.LastwolfBusinessException
import org.springframework.stereotype.Service

@Service
class VoteDomainService {

    fun convertToSituation(
        village: Village,
        participant: VillageParticipant?,
        votes: VillageVotes
    ): VillageVoteSituation {
        return VillageVoteSituation(
            isAvailableVote = isAvailableVote(village, participant),
            targetList = getSelectableTargetList(village, participant),
            target = getSelectingTarget(village, participant, votes)
        )
    }

    fun assertVote(village: Village, participant: VillageParticipant?, targetId: Int) {
        if (!isAvailableVote(village, participant)) throw LastwolfBusinessException("投票できません")
        if (getSelectableTargetList(village, participant).none { it.id == targetId }) throw LastwolfBusinessException("投票できません")
    }

    /**
     * 投票結果メッセージ
     * @param village village
     * @param votedMap key: 非投票参加者ID, value: 投票
     * @return 投票結果メッセージ
     */
    fun createEachVoteMessage(
        village: Village,
        votedMap: Map<Int, List<VillageVote>>
    ): Message {
        val maxFromCharaNameLength = votedMap.values.flatten().map { vote ->
            village.participants.first(vote.myselfId).chara.name.name.length
        }.max()!!
        val maxToCharaNameLength = votedMap.values.flatten().map { vote ->
            village.participants.first(vote.targetId).chara.name.name.length
        }.max()!!

        val text = votedMap.entries.sortedBy { it.value.size }.reversed().map { entry ->
            // 得票数が多い順
            entry.value.map { vote ->
                val fromChara = village.participants.first(vote.myselfId).chara
                val toChara = village.participants.first(vote.targetId).chara
                createEachVoteResultString(
                    fromChara,
                    toChara,
                    maxFromCharaNameLength,
                    maxToCharaNameLength,
                    entry.value.size
                )
            }
        }.flatten().joinToString(
            prefix = "投票結果は以下の通り。\n",
            separator = "\n"
        )
        return Message.createPublicSystemMessage(text, village.days.latestNoonDay().id)
    }

    // ===================================================================================
    //                                                                        Assist Logic
    //                                                                        ============
    /**
     * @param fromChara 投票したキャラ
     * @param toChara 投票されたキャラ
     * @param maxFromCharaNameLength 投票したキャラの最大文字数
     * @param maxToCharaNameLength 投票されたキャラの最大文字数
     * @param count 得票数
     * @return {キャラ名} -> {キャラ名}（{N}票）
     */
    private fun createEachVoteResultString(
        fromChara: Chara,
        toChara: Chara,
        maxFromCharaNameLength: Int,
        maxToCharaNameLength: Int,
        count: Int
    ): String {
        return fromChara.name.name.padEnd(
            length = maxFromCharaNameLength,
            padChar = '　'
        ) +
            " → " +
            toChara.name.name.padEnd(
                length = maxToCharaNameLength,
                padChar = '　'
            ) +
            "(${count}票)"
    }

    // ===================================================================================
    //                                                                        Assist Logic
    //                                                                        ============
    private fun isAvailableVote(village: Village, participant: VillageParticipant?): Boolean {
        // 参加者として可能か
        participant ?: return false
        if (!participant.isAvailableVote()) return false
        // 村として可能か
        return village.isAvailableVote()
    }

    fun getSelectableTargetList(village: Village, participant: VillageParticipant?): List<VillageParticipant> {
        if (!isAvailableVote(village, participant)) return listOf()
        return village.participants.list.filter { it.isAlive() && it.id != participant!!.id }
    }

    fun getSelectingTarget(village: Village, participant: VillageParticipant?, votes: VillageVotes): VillageParticipant? {
        if (!isAvailableVote(village, participant)) return null
        val voteTargetParticipantId = votes.list.find {
            it.villageDayId == village.days.latestDay().id
                && it.myselfId == participant!!.id
        }?.targetId ?: return null
        return village.participants.first(voteTargetParticipantId)
    }
}