package com.ort.lastwolf.domain.model.myself.participant

import com.ort.lastwolf.domain.model.message.MessageType

data class VillageSaySituation(
    val isAvailableSay: Boolean,
    val selectableMessageTypeList: List<MessageType> = listOf(),
    val defaultMessageType: MessageType?
)
