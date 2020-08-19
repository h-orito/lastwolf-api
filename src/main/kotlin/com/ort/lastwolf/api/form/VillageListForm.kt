package com.ort.lastwolf.api.form

data class VillageListForm(
    val village_status: List<String>?
) {
    constructor() : this(null)
}
