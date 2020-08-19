package com.ort.lastwolf.api.view.player

import com.ort.lastwolf.domain.model.player.Player

data class PlayerView(
    val id: Int,
    val nickname: String,
    val twitterUserName: String,
    val otherSiteName: String?,
    val introduction: String?
) {
    constructor(
        player: Player
    ) : this(
        id = player.id,
        nickname = player.nickname,
        twitterUserName = player.twitterUserName,
        otherSiteName = player.otherSiteName,
        introduction = player.introduction
    )
}