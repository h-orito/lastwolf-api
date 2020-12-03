package com.ort.lastwolf.domain.service.daychange

import com.ort.dbflute.allcommon.CDef
import com.ort.lastwolf.domain.model.charachip.Chara
import com.ort.lastwolf.domain.model.daychange.DayChange
import com.ort.lastwolf.domain.model.message.Message
import com.ort.lastwolf.domain.service.ability.AbilityDomainService
import org.springframework.stereotype.Service

@Service
class SuddenlyDeathDomainService(
    private val abilityDomainService: AbilityDomainService
) {

    // 夜→昼の突然死
    fun nightSuddenlyDeath(dayChange: DayChange): DayChange {
        // 能力行使していない人がいたら突然死
        var village = dayChange.village.copy()
        val abilities = dayChange.abilities
        var players = dayChange.players.copy()
        var messages = dayChange.messages.copy()

        // 突然死あり設定でなければ何もしない
        if (!village.setting.rules.availableSuddenlyDeath) return dayChange

        dayChange.village.notDummyParticipants().list.filter { participant ->
            participant.skill!!.abilityList.any { abilityType ->
                // 襲撃は誰かがセットしていればOK
                // 他は自分がセットしていなければだめ
                if (abilityType.toCdef() == CDef.AbilityType.襲撃) {
                    abilities.filterYesterday(village).filterByType(abilityType).list.isEmpty()
                } else if (abilityType.toCdef() == CDef.AbilityType.護衛) {
                    // 護衛は1日目はセットできない
                    village.days.latestDay().day != 2 &&
                        abilities.filterYesterday(village).filterByType(abilityType).list.none { it.myselfId == participant.id }
                } else {
                    abilities.filterYesterday(village).filterByType(abilityType).list.none { it.myselfId == participant.id }
                }
            }
        }.forEach { participant ->
            // 突然死
            village = village.suddenlyDeathParticipant(participant.id, village.days.latestDay())
            // 入村制限
            players = players.restrictParticipation(participant.player.id)
            // 突然死メッセージ
            messages = messages.add(createSuddenlyDeathMessage(participant.chara, village.days.latestDay().id))
        }

        return dayChange.copy(
            village = village,
            messages = messages,
            players = players
        ).setIsChange(dayChange)
    }

    // 昼→投票、投票→投票、投票→夜の突然死
    fun voteSuddenlyDeath(dayChange: DayChange): DayChange {
        // 前日が投票で投票していない人がいたら突然死
        var village = dayChange.village.copy()
        // 前日が投票でないなら突然死なし
        if (!village.days.yesterday().isVoteTime()) return dayChange

        // 突然死あり設定でなければ何もしない
        if (!village.setting.rules.availableSuddenlyDeath) return dayChange

        // 前日に投票していない人が対象
        var players = dayChange.players.copy()
        var messages = dayChange.messages.copy()
        village.notDummyParticipants().filterAlive().list.filter { member ->
            dayChange.votes.filterYesterday(village).list.none { vote ->
                vote.myselfId == member.id
            }
        }.forEach { member ->
            // 突然死
            village = village.suddenlyDeathParticipant(member.id, village.days.latestDay())
            // 入村制限
            players = players.restrictParticipation(member.player.id)
            // 突然死メッセージ
            messages = messages.add(createSuddenlyDeathMessage(member.chara, village.days.latestNoonDay().id))
        }

        return dayChange.copy(
            village = village,
            messages = messages,
            players = players
        ).setIsChange(dayChange)
    }

    // ===================================================================================
    //                                                                        Assist Logic
    //                                                                        ============
    /**
     * 突然死メッセージ
     * @param chara chara
     * @param villageDayId 村日付ID
     */
    private fun createSuddenlyDeathMessage(
        chara: Chara,
        villageDayId: Int
    ): Message {
        return Message.createPublicSystemMessage(
            createSuddenlyDeathMessageString(chara), villageDayId, true
        )
    }

    private fun createSuddenlyDeathMessageString(chara: Chara): String =
        "${chara.name.name}は突然死した。"
}