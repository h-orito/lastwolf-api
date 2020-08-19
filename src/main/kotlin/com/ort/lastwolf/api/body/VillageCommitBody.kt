package com.ort.lastwolf.api.body

import javax.validation.constraints.NotNull

data class VillageCommitBody(
    @field:NotNull(message = "コミット有無は必須")
    val commit: Boolean?
) {
    constructor() : this(null)
}
