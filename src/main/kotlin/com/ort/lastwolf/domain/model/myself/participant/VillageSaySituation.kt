package com.ort.lastwolf.domain.model.myself.participant

import com.fasterxml.jackson.annotation.JsonProperty
import com.ort.lastwolf.domain.model.message.MessageType

data class VillageSaySituation(
    @JsonProperty("available_say")
    val isAvailableSay: Boolean,
    val selectableMessageTypeList: List<MessageType> = listOf(),
    val defaultMessageType: MessageType?
)
