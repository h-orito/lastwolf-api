package com.ort.lastwolf.api.body

import javax.validation.constraints.NotNull

data class CreatorSayBody(
    @field:NotNull
    val message: String?
) {
    constructor() : this(null)
}
