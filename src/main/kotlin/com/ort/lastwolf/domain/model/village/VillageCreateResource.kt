package com.ort.lastwolf.domain.model.village

import com.ort.lastwolf.domain.model.message.MessageType
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
    val silentHours: Int?
)

data class VillageOrganizationCreateResource(
    val organization: String
)

data class VillageCharachipCreateResource(
    val dummyCharaId: Int,
    val charachipId: Int
)

data class VillageRuleCreateResource(
    val isOpenVote: Boolean,
    val isAvailableSkillRequest: Boolean,
    val isAvailableSpectate: Boolean,
    val isOpenSkillInGrave: Boolean,
    val isVisibleGraveMessage: Boolean,
    val isAvailableSuddenlyDeath: Boolean,
    val isAvailableCommit: Boolean,
    val isAvailableDummySkill: Boolean,
    val restrictList: List<VillageMessageRestrictCreateResource>,
    val joinPassword: String?
)

data class VillageMessageRestrictCreateResource(
    val type: MessageType,
    val count: Int,
    val length: Int
)