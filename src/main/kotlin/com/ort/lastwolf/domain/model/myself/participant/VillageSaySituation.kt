package com.ort.lastwolf.domain.model.myself.participant

import com.ort.lastwolf.domain.model.charachip.CharaFace
import com.ort.lastwolf.domain.model.message.MessageType

data class VillageSaySituation(
    val isAvailableSay: Boolean,
    val selectableMessageTypeList: List<VillageSayMessageTypeSituation> = listOf(),
    val selectableFaceTypeList: List<CharaFace>,
    val defaultMessageType: MessageType?
)
