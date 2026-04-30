package com.ort.lastwolf.domain.model.message

data class Messages(
    val list: List<Message>,
    val allRecordCount: Int? = null,
    val allPageCount: Int? = null,
    val isExistPrePage: Boolean? = null,
    val isExistNextPage: Boolean? = null,
    val currentPageNum: Int? = null,
) {
    fun add(message: Message): Messages = this.copy(list = list + message)

    fun existsDifference(messages: Messages): Boolean = list.size != messages.list.size
}
