package com.ort.lastwolf.api.body

import jakarta.validation.constraints.NotNull

data class VillageRollcallBody(
    @field:NotNull(message = "点呼tenko有無は必須")
    val rollcall: Boolean?,
) {
    constructor() : this(null)
}
