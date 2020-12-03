package com.ort.lastwolf.api.view.myself.participant

import com.ort.lastwolf.domain.model.myself.participant.VillageAbilitySituations
import com.ort.lastwolf.domain.model.village.Village

data class VillageAbilitySituationsView(
    val list: List<VillageAbilitySituationView>
) {

    constructor(
        situation: VillageAbilitySituations,
        village: Village,
        shouldHidePlayer: Boolean
    ) : this(
        list = situation.list.map {
            VillageAbilitySituationView(
                situation = it,
                village = village,
                shouldHidePlayer = shouldHidePlayer
            )
        }
    )
}
