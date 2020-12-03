package com.ort.lastwolf.api.body

import javax.validation.constraints.NotNull

data class VillageParticipateBody(
    @field:NotNull(message = "charaIdは必須")
    val charaId: Int?,

    @field:NotNull(message = "firstRequestSkillは必須")
    val firstRequestSkill: String?,

    @field:NotNull(message = "secondRequestSkillは必須")
    val secondRequestSkill: String?,

    val joinPassword: String?
) {
    constructor() : this(null, null, null, null) {}
}
