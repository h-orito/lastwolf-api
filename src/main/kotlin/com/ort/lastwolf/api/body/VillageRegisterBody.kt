package com.ort.lastwolf.api.body

import java.time.LocalDateTime
import javax.validation.Valid
import javax.validation.constraints.Max
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class VillageRegisterBody(
    @field:NotNull
    @get:Size(max = 40)
    val villageName: String?,

    @field:NotNull
    @Valid
    val setting: VillageSettingRegisterBody?
) {
    constructor() : this(null, null)
}

data class VillageSettingRegisterBody(
    @field:NotNull
    @Valid
    val time: VillageTimeCreateBody?,

    @field:NotNull
    @Valid
    val organization: VillageOrganizationCreateBody?,

    @field:NotNull
    @Valid
    val charachip: VillageCharachipCreateBody?,

    @field:NotNull
    @Valid
    val rule: VillageRuleCreateBody?
) {
    constructor() : this(null, null, null, null)
}

data class VillageTimeCreateBody(
    @field:NotNull
    val startDatetime: LocalDateTime?,

    val silentHours: Int?
) {
    constructor() : this(null, null)
}

data class VillageOrganizationCreateBody(
    @field:NotNull
    val organization: String?
) {
    constructor() : this(null)
}

data class VillageCharachipCreateBody(
    @field:NotNull
    val dummyCharaId: Int?,

    @field:NotNull
    val charachipId: Int?
) {
    constructor() : this(null, null)
}

data class VillageRuleCreateBody(
    @field:NotNull
    val openVote: Boolean?,

    @field:NotNull
    val availableSkillRequest: Boolean?,

    @field:NotNull
    val availableSpectate: Boolean?,

    @field:NotNull
    val openSkillInGrave: Boolean?,

    @field:NotNull
    val visibleGraveMessage: Boolean?,

    @field:NotNull
    val availableSuddenlyDeath: Boolean?,

    @field:NotNull
    val availableCommit: Boolean?,

    @field:NotNull
    val availableDummySkill: Boolean?,

    @field:NotNull
    @Valid
    val restrictList: List<VillageMessageRestrictCreateBody>?,

    @get:Size(max = 20)
    val joinPassword: String?
) {
    constructor() : this(
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null
    )
}

data class VillageMessageRestrictCreateBody(
    @field:NotNull
    val type: String?,

    @field:NotNull
    @field:Max(100, message = "回数は100回以下にしてください")
    val count: Int?,

    @field:NotNull
    @field:Max(200, message = "文字数は200以下にしてください")
    val length: Int?
) {
    constructor() : this(null, null, null)
}