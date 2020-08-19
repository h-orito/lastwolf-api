package com.ort.lastwolf.api.view.myself.participant

import com.ort.lastwolf.domain.model.charachip.Charas
import com.ort.lastwolf.domain.model.myself.participant.VillageAbilitySituations
import com.ort.lastwolf.domain.model.player.Players
import com.ort.lastwolf.domain.model.village.Village

data class VillageAbilitySituationsView(
    val list: List<VillageAbilitySituationView>
) {

    constructor(
        situation: VillageAbilitySituations,
        village: Village,
        players: Players,
        charas: Charas,
        shouldHidePlayer: Boolean
    ) : this(
        list = situation.list.map {
            VillageAbilitySituationView(
                it,
                village = village,
                players = players,
                charas = charas,
                shouldHidePlayer = shouldHidePlayer
            )
        }
    )
}
