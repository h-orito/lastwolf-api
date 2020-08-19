package com.ort.lastwolf.domain.model.village.ability

import com.ort.lastwolf.domain.model.ability.AbilityType

data class VillageAbility(
    val villageDayId: Int,
    val myselfId: Int,
    val targetId: Int?,
    val abilityType: AbilityType
) {
}