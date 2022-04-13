package com.ort.lastwolf.api.view.message

import com.fasterxml.jackson.annotation.JsonProperty
import com.ort.lastwolf.domain.model.message.Messages
import com.ort.lastwolf.domain.model.village.Village

data class MessagesView(
    val list: List<MessageView>,
    val allRecordCount: Int?,
    val allPageCount: Int?,
    @JsonProperty("exist_pre_page")
    val isExistPrePage: Boolean?,
    @JsonProperty("exist_next_page")
    val isExistNextPage: Boolean?,
    val currentPageNum: Int?
) {
    constructor(
        messages: Messages,
        village: Village
    ) : this(
        list = messages.list.map {
            MessageView(
                message = it,
                village = village,
                shouldHidePlayer = !village.status.isSolved()
            )
        },
        allRecordCount = messages.allRecordCount,
        allPageCount = messages.allPageCount,
        isExistPrePage = messages.isExistPrePage,
        isExistNextPage = messages.isExistNextPage,
        currentPageNum = messages.currentPageNum
    )
}

