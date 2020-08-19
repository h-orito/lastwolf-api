package com.ort.lastwolf.api.view.charachip

import com.ort.lastwolf.domain.model.charachip.Chara
import com.ort.lastwolf.domain.model.charachip.CharaDefaultMessage
import com.ort.lastwolf.domain.model.charachip.CharaFace
import com.ort.lastwolf.domain.model.charachip.CharaSize

data class CharaView(
    val id: Int,
    val charaName: CharaNameView,
    val charachipId: Int,
    val defaultMessage: CharaDefaultMessage,
    val display: CharaSize,
    val faceList: List<CharaFace>
) {

    constructor(
        chara: Chara
    ) : this(
        id = chara.id,
        charaName = CharaNameView(chara.charaName),
        charachipId = chara.charachipId,
        defaultMessage = chara.defaultMessage,
        display = chara.display,
        faceList = chara.faceList
    )
}