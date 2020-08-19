package com.ort.lastwolf.api.form

data class VillageRecordListForm(
    val vid: List<Int>?
) {
    constructor() : this(null)
}
