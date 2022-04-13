package com.ort.lastwolf.domain.service.ability

import com.ort.dbflute.allcommon.CDef
import com.ort.lastwolf.domain.model.ability.AbilityType
import com.ort.lastwolf.domain.model.charachip.Chara
import com.ort.lastwolf.domain.model.daychange.DayChange
import com.ort.lastwolf.domain.model.message.Message
import com.ort.lastwolf.domain.model.village.Village
import com.ort.lastwolf.domain.model.village.ability.VillageAbilities
import com.ort.lastwolf.domain.model.village.ability.VillageAbility
import com.ort.lastwolf.domain.model.village.participant.VillageParticipant
import org.springframework.stereotype.Service

@Service
class DivineDomainService : IAbilityDomainService {

    override fun getAbilityType(): AbilityType = AbilityType(CDef.AbilityType.占い)

    override fun getSelectableTargetList(
        village: Village,
        participant: VillageParticipant?,
        abilities: VillageAbilities
    ): List<VillageParticipant> {
        participant ?: return listOf()

        // すでに指定していたらもう使えない
        if (abilities
                .filterByType(getAbilityType())
                .filterLatestday(village).list
                .any { it.myselfId == participant.id }
        ) {
            return listOf()
        }

        // 自分以外の生存者全員
        return village.participants.list.filter {
            it.id != participant.id && it.isAlive()
        }
    }

    override fun processDummyAbility(
        dayChange: DayChange
    ): DayChange {
        val village = dayChange.village
        // 進行中のみ
        if (!village.status.isProgress()) return dayChange
        // ダミー
        val dummyParticipant = village.dummyParticipant()!!
        // 対象
        val target = getSelectableTargetList(village, dummyParticipant, dayChange.abilities).random()
        // 能力
        val ability = VillageAbility(
            villageDayId = village.days.latestDay().id,
            myselfId = dummyParticipant.id,
            targetId = target.id,
            abilityType = getAbilityType()
        )
        // メッセージ
        val message = createDivineMessage(village, ability, dummyParticipant)

        return dayChange.copy(
            messages = dayChange.messages.add(message),
            abilities = dayChange.abilities.add(ability)
        )
    }

    fun divineRandomNowolf(dayChange: DayChange, seer: VillageParticipant): DayChange {
        val village = dayChange.village
        // 進行中のみ
        if (!village.status.isProgress()) return dayChange
        // 対象（黒が出ない、占いで死なない、ダミーでない）
        val target = getSelectableTargetList(village, seer, dayChange.abilities)
            .filterNot {
                village.dummyParticipant()!!.id == it.id
            }.filterNot {
                val cdefSkill = it.skill!!.toCdef()
                cdefSkill.isDivineResultWolf || cdefSkill.isDeadByDivine
            }.random()
        // 能力
        val ability = VillageAbility(
            villageDayId = village.days.latestDay().id,
            myselfId = seer.id,
            targetId = target.id,
            abilityType = getAbilityType()
        )
        // メッセージ
        val message = createDivineMessage(village, ability, seer)

        return dayChange.copy(
            messages = dayChange.messages.add(message),
            abilities = dayChange.abilities.add(ability)
        )
    }

    override fun createAbilityMessage(
        village: Village,
        participant: VillageParticipant,
        ability: VillageAbility
    ) = createDivineMessage(village, ability, participant)

    fun divineKill(dayChange: DayChange): DayChange {
        var village = dayChange.village.copy()

        dayChange.village.participants.list.filter {
            it.isAlive() && it.skill!!.toCdef().isHasDivineAbility
        }.forEach { seer ->
            dayChange.abilities.filterYesterday(village).list.find {
                it.myselfId == seer.id
            }?.let { ability ->
                // 占いメッセージは実行時に追加するのでここでは何もしない
                // 呪殺対象なら死亡
                if (isDivineKill(dayChange, ability.targetId!!)) {
                    village = village.divineKillParticipant(ability.targetId)
                }
            }
        }

        return dayChange.copy(
            village = village
        ).setIsChange(dayChange)
    }

    override fun isAvailableNoTarget(village: Village): Boolean = false

    override fun isUsable(
        village: Village,
        participant: VillageParticipant,
        abilities: VillageAbilities
    ): Boolean {
        // すでに指定していたらもう使えない
        if (abilities
                .filterByType(getAbilityType())
                .filterLatestday(village).list
                .any { it.myselfId == participant.id }
        ) {
            return false
        }

        // 生存していたら行使できる
        return participant.isAlive()
    }

    // ===================================================================================
    //                                                                        Assist Logic
    //                                                                        ============
    private fun createDivineMessage(
        village: Village,
        ability: VillageAbility,
        seer: VillageParticipant
    ): Message {
        val target = village.participants.first(ability.targetId!!)
        val isWolf = village.participants.first(ability.targetId).skill!!.toCdef().isDivineResultWolf
        val text = createDivineMessageString(target.chara, isWolf)
        return Message.createPrivateAbilityMessage(text, village.days.latestDay().id, seer, true)
    }

    private fun createDivineMessageString(targetChara: Chara, isWolf: Boolean): String =
        "${targetChara.name.name}を占った。${targetChara.name.name}は人狼${if (isWolf) "の" else "ではない"}ようだ。"

    private fun isDivineKill(dayChange: DayChange, targetId: Int): Boolean {
        // 対象が既に死亡していたら呪殺ではない
        if (!dayChange.village.participants.first(targetId).isAlive()) return false
        // 対象が呪殺対象でなければ呪殺ではない
        return dayChange.village.participants.first(targetId).skill!!.toCdef().isDeadByDivine
    }
}