package com.ort.lastwolf.api.view.myself.participant

import com.fasterxml.jackson.annotation.JsonProperty
import com.ort.lastwolf.api.view.village.VillageParticipantView
import com.ort.lastwolf.domain.model.ability.AbilityType
import com.ort.lastwolf.domain.model.myself.participant.VillageAbilitySituation
import com.ort.lastwolf.domain.model.village.Village

data class VillageAbilitySituationView(
    val type: AbilityType,
    val targetList: List<VillageParticipantView>,
    val usable: Boolean,
    @JsonProperty("available_no_target")
    val isAvailableNoTarget: Boolean
) {
    constructor(
        situation: VillageAbilitySituation,
        village: Village,
        shouldHidePlayer: Boolean
    ) : this(
        type = situation.type,
        targetList = situation.targetList.map {
            VillageParticipantView(
                participant = village.participants.first(it.id),
                shouldHidePlayer = shouldHidePlayer
            )
        },
        usable = situation.usable,
        isAvailableNoTarget = situation.isAvailableNoTarget
    )
}
