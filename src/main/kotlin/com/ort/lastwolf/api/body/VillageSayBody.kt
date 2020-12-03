package com.ort.lastwolf.api.body

import javax.validation.constraints.NotNull

data class VillageSayBody(
    @field:NotNull(message = "発言内容は必須")
    val message: String?,

    @field:NotNull(message = "発言種別は必須")
    val messageType: String?,

    @field:NotNull(message = "強調発言かは必須")
    val strong: Boolean?
) {
    constructor() : this(null, null, null)
}
