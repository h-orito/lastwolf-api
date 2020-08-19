package com.ort.lastwolf.domain.model.player.record

import com.ort.lastwolf.domain.model.player.Player
import com.ort.lastwolf.domain.model.village.Village
import com.ort.lastwolf.domain.model.village.participant.VillageParticipant

data class ParticipateVillage(
    val village: Village,
    val participant: VillageParticipant
) {
    constructor(
        player: Player,
        village: Village
    ) : this(
        village = village,
        participant = village.findMemberByPlayerId(player.id)!!
    )
}