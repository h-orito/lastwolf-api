package com.ort.lastwolf.domain.model.village.setting

import com.ort.lastwolf.domain.model.village.VillageSettingCreateResource

data class VillageSettings(
    val capacity: PersonCapacity,
    val time: VillageTime,
    val charachip: VillageCharachip,
    val organizations: VillageOrganizations,
    val rules: VillageRules,
    val password: VillagePassword
) {

    companion object {

        fun createForRegister(
            resource: VillageSettingCreateResource
        ): VillageSettings {
            val org = VillageOrganizations.invoke(resource.organization.organization)
            return VillageSettings(
                capacity = PersonCapacity(
                    min = org.organization.keys.min(),
                    max = org.organization.keys.max()
                ),
                time = VillageTime(
                    startDatetime = resource.time.startDatetime,
                    noonSeconds = resource.time.noonSeconds,
                    voteSeconds = resource.time.voteSeconds,
                    nightSeconds = resource.time.nightSeconds
                ),
                charachip = VillageCharachip(
                    dummyCharaId = resource.charachip.dummyCharaId,
                    charachipId = resource.charachip.charachipId
                ),
                organizations = org,
                rules = VillageRules(
                    availableSkillRequest = resource.rule.isAvailableSkillRequest,
                    openSkillInGrave = resource.rule.isOpenSkillInGrave,
                    availableSuddenlyDeath = resource.rule.isAvailableSuddenlyDeath,
                    availableCommit = resource.rule.isAvailableCommit,
                    availableDummySkill = resource.rule.isAvailableDummySkill
                ),
                password = VillagePassword(
                    joinPasswordRequired = !resource.rule.joinPassword.isNullOrEmpty(),
                    joinPassword = resource.rule.joinPassword
                )
            )
        }
    }

    fun existsDifference(setting: VillageSettings): Boolean {
        return capacity.existsDifference(setting.capacity)
            || time.existsDifference(setting.time)
            || organizations.existsDifference(setting.organizations)
            || rules.existsDifference(setting.rules)
            || password.existsDifference(setting.password)
    }

    fun extendPrologue(): VillageSettings {
        return this.copy(time = time.extendPrologue())
    }

    fun extendRollCall(): VillageSettings {
        return this.copy(time = time.extendRollCall())
    }
}