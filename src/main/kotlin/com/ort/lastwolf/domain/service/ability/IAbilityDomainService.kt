package com.ort.lastwolf.domain.service.ability

import com.ort.lastwolf.domain.model.ability.AbilityType
import com.ort.lastwolf.domain.model.daychange.DayChange
import com.ort.lastwolf.domain.model.message.Message
import com.ort.lastwolf.domain.model.village.Village
import com.ort.lastwolf.domain.model.village.ability.VillageAbilities
import com.ort.lastwolf.domain.model.village.ability.VillageAbility
import com.ort.lastwolf.domain.model.village.participant.VillageParticipant

interface IAbilityDomainService {

    fun getAbilityType(): AbilityType

    fun getSelectableTargetList(
        village: Village,
        participant: VillageParticipant?,
        abilities: VillageAbilities
    ): List<VillageParticipant>

    fun processDummyAbility(
        dayChange: DayChange
    ): DayChange

    fun createAbilityMessage(
        village: Village,
        participant: VillageParticipant,
        ability: VillageAbility
    ): Message

    fun isAvailableNoTarget(village: Village): Boolean

    fun isUsable(
        village: Village,
        participant: VillageParticipant,
        abilities: VillageAbilities
    ): Boolean
}