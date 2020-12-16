package com.ort.lastwolf.domain.model.village.setting

import com.ort.dbflute.allcommon.CDef

data class VillageRules(
    val availableSkillRequest: Boolean = true,
    val openSkillInGrave: Boolean = false,
    val availableSuddenlyDeath: Boolean = true,
    val availableCommit: Boolean = false,
    val availableDummySkill: Boolean = false,
    val availableSameTargetGuard: Boolean = false,
    val firstDivineNowolf: Boolean = false
) {
    companion object {
        operator fun invoke(
            availableSkillRequest: Boolean?,
            openSkillInGrave: Boolean?,
            availableSuddenlyDeath: Boolean?,
            availableCommit: Boolean?,
            availableDummySkill: Boolean?,
            availableSameTargetGuard: Boolean?,
            firstDivineNowolf: Boolean?
        ): VillageRules {
            val defaultRules = VillageRules()
            return VillageRules(
                availableSkillRequest = availableSkillRequest ?: defaultRules.availableSkillRequest,
                openSkillInGrave = openSkillInGrave ?: defaultRules.openSkillInGrave,
                availableSuddenlyDeath = availableSuddenlyDeath ?: defaultRules.availableSuddenlyDeath,
                availableCommit = availableCommit ?: defaultRules.availableCommit,
                availableDummySkill = availableDummySkill ?: defaultRules.availableDummySkill,
                availableSameTargetGuard = availableSameTargetGuard ?: defaultRules.availableSameTargetGuard,
                firstDivineNowolf = firstDivineNowolf ?: defaultRules.firstDivineNowolf
            )
        }
    }

    fun isValidSkillRequest(firstRequest: CDef.Skill, secondRequest: CDef.Skill): Boolean {
        if (availableSkillRequest) return true
        return firstRequest == CDef.Skill.おまかせ && secondRequest == CDef.Skill.おまかせ
    }

    fun existsDifference(rules: VillageRules): Boolean {
        return availableSkillRequest != rules.availableSkillRequest
            || openSkillInGrave != rules.openSkillInGrave
            || availableSuddenlyDeath != rules.availableSuddenlyDeath
            || availableCommit != rules.availableCommit
            || availableDummySkill != rules.availableDummySkill
            || availableSameTargetGuard != rules.availableSameTargetGuard
            || firstDivineNowolf != rules.firstDivineNowolf
    }
}