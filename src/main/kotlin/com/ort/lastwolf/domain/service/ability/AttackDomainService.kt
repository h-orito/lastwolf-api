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
class AttackDomainService : IAbilityDomainService {

    override fun getAbilityType(): AbilityType = AbilityType(CDef.AbilityType.襲撃)

    override fun getSelectableTargetList(
        village: Village,
        participant: VillageParticipant?,
        abilities: VillageAbilities
    ): List<VillageParticipant> {
        participant ?: return listOf()

        // すでに指定していたらもう使えない
        if (abilities.filterByType(getAbilityType()).filterLatestday(village).list.isNotEmpty()) {
            return listOf()
        }

        return if (village.days.latestDay().day == 1) {
            // ダミーキャラ固定
            listOf(village.dummyParticipant()!!)
        } else {
            // 襲撃対象に選べる & 生存している
            village.participants.filterAlive().list.filter {
                !it.skill!!.toCdef().isNotSelectableAttack
            }
        }
    }

    override fun processDummyAbility(
        dayChange: DayChange
    ): DayChange {
        // ダミーは人狼を引くことがないので何もしない
        return dayChange
    }

    override fun createAbilityMessage(
        village: Village,
        participant: VillageParticipant,
        ability: VillageAbility
    ): Message = createAttackMessage(village, participant, ability)

    fun attack(
        dayChange: DayChange
    ): DayChange {
        val latestDay = dayChange.village.days.latestDay()

        var village = dayChange.village.copy()
        dayChange.abilities
            .filterByType(getAbilityType())
            .filterYesterday(village).list
            .find { it.targetId != null }
            ?.let { ability ->
                // 襲撃成功したら死亡
                if (isAttackSuccess(dayChange, ability.targetId!!)) {
                    village = village.attackParticipant(ability.targetId, latestDay)
                }
            } ?: return dayChange

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
        if (abilities.filterByType(getAbilityType()).filterLatestday(village).list.isNotEmpty()) {
            return false
        }
        // 生存していたら行使できる
        return participant.isAlive()
    }

    // ===================================================================================
    //                                                                        Assist Logic
    //                                                                        ============
    private fun isAttackSuccess(dayChange: DayChange, targetId: Int): Boolean {
        // 対象が既に死亡していたら失敗
        if (!dayChange.village.participants.first(targetId).isAlive()) return false
        // 対象が護衛されていたら失敗
        if (dayChange.abilities.list.any { villageAbility ->
                villageAbility.abilityType.code == CDef.AbilityType.護衛.code()
                    && villageAbility.targetId == targetId
                    && villageAbility.villageDayId == dayChange.village.days.yesterday().id
                    && dayChange.village.participants.first(villageAbility.myselfId).isAlive()
            }) {
            return false
        }
        // 対象が襲撃を耐える役職なら失敗
        return !dayChange.village.participants.first(targetId).skill!!.toCdef().isNoDeadByAttack
    }

    private fun createAttackMessage(
        village: Village,
        wolf: VillageParticipant,
        ability: VillageAbility
    ): Message {
        val target = village.participants.first(ability.targetId!!)
        val text = createAttackMessageString(wolf.chara, target.chara)
        return Message.createAttackPrivateMessage(text, village.days.latestDay().id, true)
    }

    private fun createAttackMessageString(chara: Chara, targetChara: Chara): String =
        "${chara.name.name}が${targetChara.name.name}を襲撃します。"
}