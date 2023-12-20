package com.ort.lastwolf.domain.model.village

import java.time.LocalDateTime

data class VillageCreateResource(
    val villageName: String,
    val createPlayerId: Int,
    val setting: VillageSettingCreateResource
)

data class VillageSettingCreateResource(
    val time: VillageTimeCreateResource,
    val organization: VillageOrganizationCreateResource,
    val charachip: VillageCharachipCreateResource,
    val rule: VillageRuleCreateResource
)

data class VillageTimeCreateResource(
    val startDatetime: LocalDateTime,
    val noonSeconds: Int,
    val voteSeconds: Int,
    val nightSeconds: Int
)

data class VillageOrganizationCreateResource(
    val organization: String
)

data class VillageCharachipCreateResource(
    val dummyCharaId: Int,
    val charachipId: Int
)

data class VillageRuleCreateResource(
    val isAvailableSkillRequest: Boolean,
    val isOpenSkillInGrave: Boolean,
    val isVisibleGraveMessage: Boolean,
    val isAvailableSuddenlyDeath: Boolean,
    val isAvailableCommit: Boolean,
    val isAvailableDummySkill: Boolean,
    val isAvailableSameTargetGuard: Boolean,
    val isFirstDivineNowolf: Boolean,
    val silentSeconds: Int?,
    val joinPassword: String?
)
