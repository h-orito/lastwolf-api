package com.ort.lastwolf.api.view.charachip

import com.ort.lastwolf.domain.model.charachip.CharaName

data class CharaNameView(
    val name: String,
    val shortName: String,
    val fullName: String
) {
    constructor(
        charaName: CharaName
    ) : this(
        name = charaName.name,
        shortName = charaName.shortName,
        fullName = charaName.fullName()
    )
}