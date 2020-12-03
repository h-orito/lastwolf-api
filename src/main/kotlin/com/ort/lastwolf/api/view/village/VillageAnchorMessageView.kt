package com.ort.lastwolf.api.view.village

import com.ort.lastwolf.api.view.message.MessageView
import com.ort.lastwolf.domain.model.message.Message
import com.ort.lastwolf.domain.model.village.Village

data class VillageAnchorMessageView(
    val message: MessageView?
) {
    constructor(
        message: Message?,
        village: Village
    ) : this(
        message = message?.let {
            MessageView(
                message = it,
                village = village,
                shouldHidePlayer = !village.status.isSolved()
            )
        }
    )
}