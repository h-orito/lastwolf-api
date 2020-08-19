package com.ort.lastwolf.api.view.player

import com.ort.lastwolf.api.view.village.VillageParticipantView
import com.ort.lastwolf.api.view.village.VillageView
import com.ort.lastwolf.domain.model.charachip.Charas
import com.ort.lastwolf.domain.model.player.Player
import com.ort.lastwolf.domain.model.player.Players
import com.ort.lastwolf.domain.model.village.Village
import com.ort.lastwolf.domain.model.village.participant.VillageParticipant

data class ParticipateVillageView(
    val village: VillageView,
    val participant: VillageParticipantView
) {

    constructor(
        village: Village,
        participant: VillageParticipant,
        charas: Charas,
        players: Players,
        createPlayer: Player
    ) : this(
        village = VillageView(village, charas, players, createPlayer),
        participant = VillageParticipantView(village, participant.id, players, charas, false)
    )
}
