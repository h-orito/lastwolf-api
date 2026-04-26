package com.ort.lastwolf.api.body

import jakarta.validation.constraints.NotNull

data class CreatorSayBody(
    @field:NotNull
    val message: String?
) {
    constructor() : this(null)
}
