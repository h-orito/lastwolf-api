package com.ort.lastwolf.api.view.player

import com.ort.lastwolf.api.view.village.VillageParticipantView
import com.ort.lastwolf.api.view.village.VillageView
import com.ort.lastwolf.domain.model.village.Village
import com.ort.lastwolf.domain.model.village.participant.VillageParticipant

data class ParticipateVillageView(
    val village: VillageView,
    val participant: VillageParticipantView
) {

    constructor(
        village: Village,
        participant: VillageParticipant
    ) : this(
        village = VillageView(village),
        participant = VillageParticipantView(participant, false)
    )
}
