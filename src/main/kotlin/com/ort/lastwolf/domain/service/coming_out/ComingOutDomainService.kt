package com.ort.lastwolf.domain.service.coming_out

import com.ort.lastwolf.domain.model.charachip.Chara
import com.ort.lastwolf.domain.model.message.Message
import com.ort.lastwolf.domain.model.myself.participant.VillageComingOutSituation
import com.ort.lastwolf.domain.model.skill.Skill
import com.ort.lastwolf.domain.model.skill.Skills
import com.ort.lastwolf.domain.model.village.Village
import com.ort.lastwolf.domain.model.village.participant.VillageParticipant
import com.ort.lastwolf.domain.model.village.participant.coming_out.ComingOuts
import com.ort.lastwolf.fw.exception.LastwolfBusinessException
import org.springframework.stereotype.Service

@Service
class ComingOutDomainService {

    fun convertToSituation(village: Village, participant: VillageParticipant?): VillageComingOutSituation {
        return VillageComingOutSituation(
            isAvailableComingOut = isAvailableComingOut(village, participant),
            currentComingOuts = participant?.commigOuts ?: ComingOuts(),
            selectableSkillList = selectableSkillList(village, participant)
        )
    }

    fun assertComingOut(
        village: Village,
        participant: VillageParticipant?
    ) {
        if (!isAvailableComingOut(village, participant)) throw LastwolfBusinessException("カミングアウトできません")
    }

    fun createComingOutMessage(chara: Chara, skills: Skills, villageDayId: Int): Message {
        return Message.createPublicSystemMessage(getComingOutSetMessage(chara, skills), villageDayId)
    }

    // ===================================================================================
    //                                                                        Assist Logic
    //                                                                        ============
    private fun selectableSkillList(village: Village, participant: VillageParticipant?): List<Skill> {
        if (!isAvailableComingOut(village, participant)) return listOf()
        return village.setting.organizations.mapToSkillCount(village.participant.count)
            .filter { it.value > 0 }.keys.sortedBy { it.order().toInt() }.map { Skill(it) }
    }

    private fun isAvailableComingOut(village: Village, participant: VillageParticipant?): Boolean {
        // 村として可能か
        if (!village.isAvailableComingOut()) return false
        // 参加者として可能か
        participant ?: return false
        return participant.isAvailableComingOut()
    }

    private fun getComingOutSetMessage(chara: Chara, skills: Skills): String {
        val name = chara.charaName.fullName()
        return if (skills.list.isEmpty()) {
            "${name}がカミングアウトを取り消しました。"
        } else {
            val skills = skills.list.map { it.name }.joinToString("と")
            "${name}が${skills}をカミングアウトしました。"
        }
    }
}