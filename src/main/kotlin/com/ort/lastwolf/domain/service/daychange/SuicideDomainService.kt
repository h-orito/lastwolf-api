package com.ort.lastwolf.domain.service.daychange

import com.ort.dbflute.allcommon.CDef
import com.ort.lastwolf.domain.model.charachip.Charas
import com.ort.lastwolf.domain.model.daychange.DayChange
import com.ort.lastwolf.domain.model.message.Message
import com.ort.lastwolf.domain.model.skill.toModel
import com.ort.lastwolf.domain.model.village.Village
import com.ort.lastwolf.domain.model.village.participant.VillageParticipant
import org.springframework.stereotype.Service

@Service
class SuicideDomainService {

    fun suicide(daychange: DayChange): DayChange {
        var village = daychange.village.copy()
        var messages = daychange.messages.copy()

        while (existsSuicideTarget(village)) {
            val target = findSuicideTarget(village)!!
            messages = messages.add(createImmoralSuicideMessage(village, target))
            village = village.suicideParticipant(target.id)
        }

        return daychange.copy(village = village, messages = messages)
    }

    private fun findSuicideTarget(village: Village): VillageParticipant? {
        // 背徳者
        return findImmoralSuicideTarget(village)
    }

    private fun existsSuicideTarget(village: Village): Boolean = findSuicideTarget(village) != null

    private fun findImmoralSuicideTarget(village: Village): VillageParticipant? {
        // 妖狐系が生存していたら後追いしない
        if (village.participants.filterAlive().list.any { it.skill!!.isFoxCount() }) {
            return null
        }
        // 生存している背徳者が後追い対象
        return village.participants
            .filterAlive()
            .filterBySkill(CDef.Skill.背徳者.toModel())
            .list
            .firstOrNull()
    }

    private fun createImmoralSuicideMessage(village: Village, target: VillageParticipant): Message {
        return Message.createPublicSystemMessage(
            text = "${target.chara.name.fullName()}は、妖狐の後を追い、いなくなってしまった。",
            villageDayId = village.days.latestDay().id
        )
    }
}
