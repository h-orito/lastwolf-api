package com.ort.lastwolf.domain.model.myself.participant

import com.ort.lastwolf.domain.model.ability.AbilityType
import com.ort.lastwolf.domain.model.village.participant.VillageParticipant

data class VillageAbilitySituation(
    val type: AbilityType,
    val targetList: List<VillageParticipant>,
    val usable: Boolean,
    val isAvailableNoTarget: Boolean
)
