package com.ort.firewolf.domain.model.village.setting

import com.ort.dbflute.allcommon.CDef

data class VillageRules(
    val openVote: Boolean = false,
    val availableSkillRequest: Boolean = true,
    val availableSpectate: Boolean = false,
    val openSkillInGrave: Boolean = false,
    val visibleGraveMessage: Boolean = false,
    val availableSuddenlyDeath: Boolean = true,
    val availableCommit: Boolean = false,
    val autoGenerated: Boolean = false,
    val availableDummySkill: Boolean = false,
    val messageRestrict: VillageMessageRestricts = VillageMessageRestricts()
) {
    companion object {
        operator fun invoke(
            openVote: Boolean?,
            availableSkillRequest: Boolean?,
            availableSpectate: Boolean?,
            openSkillInGrave: Boolean?,
            visibleGraveMessage: Boolean?,
            availableSuddenlyDeath: Boolean?,
            availableCommit: Boolean?,
            autoGenerated: Boolean?,
            availableDummySkill: Boolean?,
            messageRestrict: VillageMessageRestricts?
        ): VillageRules {
            val defaultRules = VillageRules()
            return VillageRules(
                openVote = openVote ?: defaultRules.openVote,
                availableSkillRequest = availableSkillRequest ?: defaultRules.availableSkillRequest,
                availableSpectate = availableSpectate ?: defaultRules.availableSpectate,
                openSkillInGrave = openSkillInGrave ?: defaultRules.openSkillInGrave,
                visibleGraveMessage = visibleGraveMessage ?: defaultRules.visibleGraveMessage,
                availableSuddenlyDeath = availableSuddenlyDeath ?: defaultRules.availableSuddenlyDeath,
                availableCommit = availableCommit ?: defaultRules.availableCommit,
                autoGenerated = autoGenerated ?: defaultRules.autoGenerated,
                availableDummySkill = availableDummySkill ?: defaultRules.availableDummySkill,
                messageRestrict = messageRestrict ?: defaultRules.messageRestrict
            )
        }
    }

    fun isValidSkillRequest(firstRequest: CDef.Skill, secondRequest: CDef.Skill): Boolean {
        if (availableSkillRequest) return true
        return firstRequest == CDef.Skill.おまかせ && secondRequest == CDef.Skill.おまかせ
    }

    fun existsDifference(rules: VillageRules): Boolean {
        return openVote != rules.openVote
            || availableSkillRequest != rules.availableSkillRequest
            || availableSpectate != rules.availableSpectate
            || openSkillInGrave != rules.openSkillInGrave
            || visibleGraveMessage != rules.visibleGraveMessage
            || availableSuddenlyDeath != rules.availableSuddenlyDeath
            || availableCommit != rules.availableCommit
            || availableDummySkill != rules.availableDummySkill
            || messageRestrict.existsDifference(rules.messageRestrict)
    }
}