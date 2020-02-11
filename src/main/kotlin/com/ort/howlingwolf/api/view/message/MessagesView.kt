package com.ort.howlingwolf.api.view.message

import com.ort.howlingwolf.domain.model.charachip.Charas
import com.ort.howlingwolf.domain.model.message.Messages
import com.ort.howlingwolf.domain.model.player.Players
import com.ort.howlingwolf.domain.model.village.Village

data class MessagesView(
    val list: List<MessageView>,
    val allPageCount: Int?,
    val isExistPrePage: Boolean?,
    val isExistNextPage: Boolean?,
    val currentPageNum: Int?,
    val pageNumList: List<Int>?
) {
    constructor(
        messages: Messages,
        village: Village,
        players: Players,
        charas: Charas
    ) : this(
        list = messages.list.map {
            MessageView(
                message = it,
                village = village,
                players = players,
                charas = charas,
                shouldHidePlayer = !village.status.isSolved()
            )
        },
        allPageCount = messages.allPageCount,
        isExistPrePage = messages.isExistPrePage,
        isExistNextPage = messages.isExistNextPage,
        currentPageNum = messages.currentPageNum,
        pageNumList = messages.pageNumList
    )
}

