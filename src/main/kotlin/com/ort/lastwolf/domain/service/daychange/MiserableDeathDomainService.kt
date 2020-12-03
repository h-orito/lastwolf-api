package com.ort.lastwolf.domain.service.daychange

import com.ort.lastwolf.domain.model.charachip.Charas
import com.ort.lastwolf.domain.model.daychange.DayChange
import com.ort.lastwolf.domain.model.message.Message
import com.ort.lastwolf.domain.model.village.Village
import org.springframework.stereotype.Service

@Service
class MiserableDeathDomainService {

    fun processDayChangeAction(dayChange: DayChange): DayChange {
        val latestDay = dayChange.village.days.latestDay()

        val miserableDeathCharaList = dayChange.village.participants.list.filter {
            !it.isAlive() && it.dead?.villageDay?.id == latestDay.id && it.dead.toCdef().isMiserableDeath
        }.map { member -> member.chara }

        return dayChange.copy(
            messages = dayChange.messages.add(createMiserableDeathMessage(dayChange.village, Charas(miserableDeathCharaList)))
        ).setIsChange(dayChange)
    }

    // ===================================================================================
    //                                                                        Assist Logic
    //                                                                        ============
    /**
     * 無惨メッセージ
     * @param village village
     * @param charas 犠牲者キャラ
     * @return 無惨メッセージ
     */
    private fun createMiserableDeathMessage(
        village: Village,
        charas: Charas
    ): Message {
        val text = if (charas.list.isEmpty()) {
            "今日は犠牲者がいないようだ。人狼は襲撃に失敗したのだろうか。"
        } else {
            charas.list.shuffled().joinToString(
                prefix = "次の日の朝、",
                postfix = "が無惨な姿で発見された。",
                separator = "と"
            ) { it.name.name }
        }
        return Message.createPublicSystemMessage(text, village.days.latestDay().id, true)
    }
}