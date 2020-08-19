package com.ort.lastwolf.api.body

import javax.validation.constraints.NotNull

data class CreatorKickBody(
    @field:NotNull
    val targetId: Int?
) {
    constructor() : this(null)
}
