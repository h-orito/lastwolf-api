package com.ort.lastwolf.domain.service.ability

import com.ort.lastwolf.domain.model.charachip.Chara
import com.ort.lastwolf.domain.model.daychange.DayChange
import com.ort.lastwolf.domain.model.message.Message
import com.ort.lastwolf.domain.model.village.Village
import com.ort.lastwolf.domain.model.village.participant.VillageParticipant
import org.springframework.stereotype.Service

@Service
class PsychicDomainService {

    fun processDayChangeAction(dayChange: DayChange): DayChange {
        // 霊能がいない、または処刑・突然死がいない場合は何もしない
        val existsAlivePsychic = dayChange.village.participants.filterAlive().list.any { it.skill!!.toCdef().isHasPsychicAbility }
        if (!existsAlivePsychic) return dayChange
        val todayDeadParticipants = dayChange.village.todayDeadParticipants().list.filter { it.dead!!.toCdef().isPsychicableDeath }
        if (todayDeadParticipants.isEmpty()) return dayChange

        var messages = dayChange.messages.copy()
        todayDeadParticipants.forEach { deadParticipant ->
            messages = messages.add(createPsychicPrivateMessage(dayChange.village, deadParticipant))
        }
        return dayChange.copy(messages = messages).setIsChange(dayChange)
    }

    // ===================================================================================
    //                                                                        Assist Logic
    //                                                                        ============
    private fun createPsychicPrivateMessage(
        village: Village,
        deadParticipant: VillageParticipant
    ): Message {
        val isWolf = village.participants.first(deadParticipant.id).skill!!.toCdef().isPsychicResultWolf
        val text = createPsychicPrivateMessageString(deadParticipant.chara, isWolf)
        return Message.createPsychicPrivateMessage(text, village.days.latestDay().id, true)
    }

    private fun createPsychicPrivateMessageString(chara: Chara, isWolf: Boolean): String =
        "${chara.name.name}は人狼${if (isWolf) "の" else "ではない"}ようだ。"
}