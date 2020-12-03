package com.ort.lastwolf.api.view.charachip

import com.ort.lastwolf.domain.model.charachip.Chara
import com.ort.lastwolf.domain.model.charachip.CharaImage

data class CharaView(
    val id: Int,
    val name: CharaNameView,
    val charachipId: Int,
    val image: CharaImage
) {

    constructor(
        chara: Chara
    ) : this(
        id = chara.id,
        name = CharaNameView(chara.name),
        charachipId = chara.charachipId,
        image = chara.image
    )
}