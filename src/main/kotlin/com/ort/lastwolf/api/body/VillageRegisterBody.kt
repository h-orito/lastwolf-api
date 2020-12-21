package com.ort.lastwolf.api.body

import java.time.LocalDateTime
import javax.validation.Valid
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

    @field:NotNull
    val noonSeconds: Int?,

    @field:NotNull
    val voteSeconds: Int?,

    @field:NotNull
    val nightSeconds: Int?
) {
    constructor() : this(null, null, null, null)
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
    val availableSkillRequest: Boolean?,

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
    val availableSameTargetGuard: Boolean?,

    @field:NotNull
    val firstDivineNowolf: Boolean?,

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
        null
    )
}
