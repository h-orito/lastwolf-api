package com.ort.lastwolf.api.view.message

import com.ort.lastwolf.api.view.village.VillageParticipantView
import com.ort.lastwolf.domain.model.message.Message
import com.ort.lastwolf.domain.model.message.MessageContent
import com.ort.lastwolf.domain.model.village.Village

data class MessageView(
    val from: VillageParticipantView?,
    val time: MessageTimeView,
    val content: MessageContent
) {
    constructor(
        message: Message,
        village: Village,
        shouldHidePlayer: Boolean
    ) : this(
        from = message.fromParticipantId?.let {
            VillageParticipantView(
                village.participants.first(it),
                shouldHidePlayer
            )
        },
        time = MessageTimeView(message.time, village.days.list.first { it.id == message.time.villageDayId }),
        content = message.content
    )
}