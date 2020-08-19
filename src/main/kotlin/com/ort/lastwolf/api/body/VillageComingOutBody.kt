package com.ort.lastwolf.api.body

data class VillageComingOutBody(
    val skillCode: List<String>?
) {
    constructor() : this(null)
}
