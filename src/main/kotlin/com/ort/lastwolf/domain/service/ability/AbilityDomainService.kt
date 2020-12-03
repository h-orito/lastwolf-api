package com.ort.lastwolf.domain.service.ability

import com.ort.dbflute.allcommon.CDef
import com.ort.lastwolf.domain.model.ability.AbilityType
import com.ort.lastwolf.domain.model.ability.AbilityTypes
import com.ort.lastwolf.domain.model.myself.participant.VillageAbilitySituation
import com.ort.lastwolf.domain.model.myself.participant.VillageAbilitySituations
import com.ort.lastwolf.domain.model.village.Village
import com.ort.lastwolf.domain.model.village.ability.VillageAbilities
import com.ort.lastwolf.domain.model.village.ability.VillageAbility
import com.ort.lastwolf.domain.model.village.participant.VillageParticipant
import com.ort.lastwolf.fw.exception.LastwolfBadRequestException
import com.ort.lastwolf.fw.exception.LastwolfBusinessException
import org.springframework.stereotype.Service

@Service
class AbilityDomainService(
    private val attackDomainService: AttackDomainService,
    private val divineDomainService: DivineDomainService,
    private val guardDomainService: GuardDomainService
) {

    // 選択可能な対象
    fun getSelectableTargetList(
        village: Village,
        participant: VillageParticipant?,
        abilityType: AbilityType,
        abilities: VillageAbilities
    ): List<VillageParticipant> {
        if (!canUseAbility(village, participant)) return listOf()
        return detectDomainService(abilityType)?.getSelectableTargetList(village, participant, abilities) ?: listOf()
    }

    fun assertAbility(
        village: Village,
        participant: VillageParticipant?,
        targetId: Int?,
        abilityType: AbilityType,
        abilities: VillageAbilities
    ) {
        participant?.skill ?: throw LastwolfBadRequestException("役職なし")
        // その能力を持っていない
        if (AbilityTypes(participant.skill).list.none { it.code == abilityType.code }) {
            throw LastwolfBadRequestException("${abilityType.name}の能力を持っていません")
        }
        // 使えない状況
        if (!isUsable(village, participant, abilityType, abilities)) throw LastwolfBusinessException("${abilityType.name}能力を使えない状態です")
        // 対象指定がおかしい
        if (targetId == null && !canNoTarget(village, abilityType)) throw LastwolfBusinessException("対象指定が必要です")
        if (targetId != null && getSelectableTargetList(village, participant, abilityType, abilities).none { it.id == targetId }) {
            throw LastwolfBusinessException("指定できない対象を指定しています")
        }
    }

    fun isUsable(
        village: Village,
        participant: VillageParticipant?,
        abilityType: AbilityType,
        abilities: VillageAbilities
    ): Boolean {
        participant ?: return false
        // 夜でないと使えない
        if (!village.days.latestDay().isNightTime()) return false
        // 進行中でないと使えない
        if (!village.status.isProgress()) return false
        return detectDomainService(abilityType)?.isUsable(village, participant, abilities) ?: false
    }

    fun canNoTarget(village: Village, abilityType: AbilityType): Boolean =
        detectDomainService(abilityType)?.isAvailableNoTarget(village) ?: false

    fun convertToSituationList(
        village: Village,
        participant: VillageParticipant?,
        abilities: VillageAbilities
    ): VillageAbilitySituations {
        participant?.skill ?: return VillageAbilitySituations(listOf())
        val abilityTypes = AbilityTypes(participant.skill)
        return VillageAbilitySituations(
            list = abilityTypes.list.map { convertToSituation(village, participant, it, abilities) }
        )
    }

    fun createAbilityMessage(
        village: Village,
        participant: VillageParticipant,
        ability: VillageAbility
    ) = detectDomainService(ability.abilityType)!!.createAbilityMessage(village, participant, ability)

    fun detectDomainService(abilityType: AbilityType): IAbilityDomainService? {
        return when (abilityType.code) {
            CDef.AbilityType.襲撃.code() -> attackDomainService
            CDef.AbilityType.占い.code() -> divineDomainService
            CDef.AbilityType.護衛.code() -> guardDomainService
            else -> null
        }
    }

    // ===================================================================================
    //                                                                        Assist Logic
    //                                                                        ============
    private fun canUseAbility(village: Village, participant: VillageParticipant?): Boolean {
        // 村として可能か
        if (!village.canUseAbility()) return false
        // 参加者として可能か
        participant ?: return false
        return participant.canUseAbility()
    }

    private fun convertToSituation(
        village: Village,
        participant: VillageParticipant?,
        abilityType: AbilityType,
        abilities: VillageAbilities
    ): VillageAbilitySituation {
        return VillageAbilitySituation(
            type = abilityType,
            targetList = this.getSelectableTargetList(village, participant, abilityType, abilities),
            usable = this.isUsable(village, participant, abilityType, abilities),
            isAvailableNoTarget = this.canNoTarget(village, abilityType)
        )
    }
}