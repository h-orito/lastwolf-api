package com.ort.lastwolf.api.view.village

import com.ort.lastwolf.api.view.message.MessageView
import com.ort.lastwolf.domain.model.charachip.Charas
import com.ort.lastwolf.domain.model.message.Message
import com.ort.lastwolf.domain.model.player.Players
import com.ort.lastwolf.domain.model.village.Village

data class VillageAnchorMessageView(
    val message: MessageView?
) {
    constructor(
        message: Message?,
        village: Village,
        players: Players,
        charas: Charas
    ) : this(
        message = if (message == null) null else MessageView(
            message = message,
            village = village,
            players = players,
            charas = charas,
            shouldHidePlayer = !village.status.isSolved()
        )
    )
}