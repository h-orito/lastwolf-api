package com.ort.lastwolf.domain.service.coming_out

import com.ort.lastwolf.domain.model.charachip.Chara
import com.ort.lastwolf.domain.model.message.Message
import com.ort.lastwolf.domain.model.myself.participant.VillageComingOutSituation
import com.ort.lastwolf.domain.model.skill.Skill
import com.ort.lastwolf.domain.model.village.Village
import com.ort.lastwolf.domain.model.village.participant.VillageParticipant
import com.ort.lastwolf.fw.exception.LastwolfBusinessException
import org.springframework.stereotype.Service

@Service
class ComingOutDomainService {

    fun convertToSituation(village: Village, participant: VillageParticipant?): VillageComingOutSituation {
        return VillageComingOutSituation(
            isAvailableComingOut = isAvailableComingOut(village, participant),
            currentComingOut = participant?.comingOut,
            selectableSkillList = selectableSkillList(village, participant)
        )
    }

    fun assertComingOut(
        village: Village,
        participant: VillageParticipant?
    ) {
        if (!isAvailableComingOut(village, participant)) throw LastwolfBusinessException("カミングアウトできません")
    }

    fun createComingOutMessage(chara: Chara, skill: Skill?, villageDayId: Int): Message {
        return Message.createPublicSystemMessage(getComingOutSetMessage(chara, skill), villageDayId, true)
    }

    // ===================================================================================
    //                                                                        Assist Logic
    //                                                                        ============
    private fun selectableSkillList(village: Village, participant: VillageParticipant?): List<Skill> {
        if (!isAvailableComingOut(village, participant)) return listOf()
        return village.setting.organizations.mapToSkillCount(village.participants.count)
            .filter { it.value > 0 }.keys.sortedBy { it.order().toInt() }.map { Skill(it) }
    }

    private fun isAvailableComingOut(village: Village, participant: VillageParticipant?): Boolean {
        // 村として可能か
        if (!village.isAvailableComingOut()) return false
        // 参加者として可能か
        participant ?: return false
        return participant.isAvailableComingOut()
    }

    private fun getComingOutSetMessage(chara: Chara, skill: Skill?): String {
        val name = chara.name.name
        return if (skill == null) {
            "${name}がカミングアウトを取り消しました。"
        } else {
            "${name}が${skill.name}をカミングアウトしました。"
        }
    }
}