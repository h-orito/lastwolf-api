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
class GuardDomainService : IAbilityDomainService {

    override fun getAbilityType(): AbilityType = AbilityType(CDef.AbilityType.護衛)

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

        // 1日目は護衛できない
        if (village.days.latestDay().day <= 1) return listOf()

        // 連続護衛可能なら自分以外の生存者全員
        val targets = village.participants.filterAlive().list.filter {
            it.id != participant.id
        }
        if (village.setting.rules.availableSameTargetGuard) return targets
        // 前日護衛した人
        val yesterdayAbility = abilities.filterByType(getAbilityType()).list.lastOrNull {
            it.myselfId == participant.id
        } ?: return targets
        return targets.filterNot { it.id == yesterdayAbility.targetId }
    }

    override fun processDummyAbility(
        dayChange: DayChange
    ): DayChange {
        // 初日は護衛できないので狩人になっても何もしない
        return dayChange
    }

    override fun createAbilityMessage(
        village: Village,
        participant: VillageParticipant,
        ability: VillageAbility
    ) = createGuardMessage(village, ability, participant)

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

        // 2日目以降、生存していたら行使できる
        return village.days.latestDay().day > 1 && participant.isAlive()
    }

    // ===================================================================================
    //                                                                        Assist Logic
    //                                                                        ============
    private fun createGuardMessage(village: Village, ability: VillageAbility, hunter: VillageParticipant): Message {
        val target = village.participants.first(ability.targetId!!)
        val text = createGuardMessageString(target.chara)
        return Message.createPrivateAbilityMessage(text, village.days.latestDay().id, hunter, true)
    }

    private fun createGuardMessageString(targetChara: Chara): String =
        "${targetChara.name.name}を護衛します。"
}