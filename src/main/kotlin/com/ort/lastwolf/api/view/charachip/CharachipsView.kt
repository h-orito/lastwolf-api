package com.ort.lastwolf.api.view.charachip

import com.ort.lastwolf.domain.model.charachip.Charachips
import com.ort.lastwolf.domain.model.charachip.Charas

data class CharachipsView(
    val list: List<CharachipView>
) {
    constructor(
        charachips: Charachips,
        charas: Charas
    ) : this(
        list = charachips.list.map { charachip ->
            CharachipView(
                charachip = charachip,
                charas = charas
            )
        }
    )
}