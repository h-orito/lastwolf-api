package com.ort.lastwolf.domain.service.village

import com.ort.lastwolf.domain.model.message.Message
import com.ort.lastwolf.domain.model.village.Village
import com.ort.lastwolf.domain.model.village.VillageCharachipCreateResource
import com.ort.lastwolf.domain.model.village.VillageCreateResource
import com.ort.lastwolf.domain.model.village.VillageOrganizationCreateResource
import com.ort.lastwolf.domain.model.village.VillageRuleCreateResource
import com.ort.lastwolf.domain.model.village.VillageTimeCreateResource
import com.ort.lastwolf.domain.model.village.setting.VillageCharachip
import com.ort.lastwolf.domain.model.village.setting.VillageOrganizations
import com.ort.lastwolf.domain.model.village.setting.VillageRules
import com.ort.lastwolf.domain.model.village.setting.VillageTime
import com.ort.lastwolf.fw.exception.LastwolfBusinessException
import org.springframework.stereotype.Service

@Service
class VillageSettingDomainService {

    fun assertModify(
        village: Village,
        resource: VillageCreateResource
    ) {
        assertOrganization(village.participants.count, resource.setting.organization)
        assertCharachip(village.setting.charachip, resource.setting.charachip)
    }

    fun createModifyMessage(village: Village, resource: VillageCreateResource): Message? {
        val list = mutableListOf<String>()
        if (village.name != resource.villageName) list.add("村の名前")
        village.setting.let { setting ->
            addTimeModifyMessage(list, setting.time, resource.setting.time)
            addOrganizationModifyMessage(list, setting.organizations, resource.setting.organization)
            addRuleModifyMessage(list, setting.rules, resource.setting.rule)
            addPasswordModifyMessage(list, setting.password.joinPassword, resource.setting.rule.joinPassword)
        }
        val message = list.map { "・${it}" }.joinToString(
            separator = "\n",
            prefix = "村の設定が変更されました。\n変更項目\n"
        )
        if (list.isEmpty()) return null
        return Message.createPublicSystemMessage(
            text = message,
            villageDayId = village.days.latestDay().id
        )
    }

    private fun addTimeModifyMessage(
        list: MutableList<String>,
        time: VillageTime,
        resourceTime: VillageTimeCreateResource
    ) {
        if (time.startDatetime != resourceTime.startDatetime) list.add("開始日時")
        if (time.noonSeconds != resourceTime.noonSeconds) list.add("昼時間")
        if (time.voteSeconds != resourceTime.voteSeconds) list.add("投票時間")
        if (time.nightSeconds != resourceTime.nightSeconds) list.add("夜時間")
    }

    private fun addOrganizationModifyMessage(
        list: MutableList<String>,
        organizations: VillageOrganizations,
        resourceOrganization: VillageOrganizationCreateResource
    ) {
        val org = organizations.toString()
        val resourceOrg = VillageOrganizations(resourceOrganization.organization).toString()
        if (org != resourceOrg) list.add("編成")
    }

    private fun addRuleModifyMessage(
        list: MutableList<String>,
        rules: VillageRules,
        resourceRules: VillageRuleCreateResource
    ) {
        if (!rules.availableSkillRequest && resourceRules.isAvailableSkillRequest) list.add("役職希望有無")
        if (rules.availableSkillRequest && !resourceRules.isAvailableSkillRequest) list.add("役職希望有無（全員おまかせに変更されます）")
        if (rules.openSkillInGrave != resourceRules.isOpenSkillInGrave) list.add("墓下役職公開有無")
        if (rules.availableSuddenlyDeath != resourceRules.isAvailableSuddenlyDeath) list.add("突然死有無")
        if (rules.availableCommit != resourceRules.isAvailableCommit) list.add("時短希望有無")
        if (rules.availableDummySkill != resourceRules.isAvailableDummySkill) list.add("ダミー役欠け有無")
        if (rules.availableSameTargetGuard != resourceRules.isAvailableSameTargetGuard) list.add("連続護衛有無")
        if (rules.firstDivineNowolf != resourceRules.isFirstDivineNowolf) list.add("初日白通知有無")
    }

    private fun addPasswordModifyMessage(
        list: MutableList<String>,
        password: String?,
        resourcePassword: String?
    ) {
        if (password.isNullOrEmpty() && resourcePassword.isNullOrEmpty()) return
        if (password != resourcePassword) list.add("参加パスワード")
    }

    // ===================================================================================
    //                                                                        Assist Logic
    //                                                                        ============
    private fun assertOrganization(participantCount: Int, organization: VillageOrganizationCreateResource) {
        // 現在の人数が編成の上限人数を超えていたらNG
        val resourceOrg = VillageOrganizations.invoke(organization.organization)
        val capacity = resourceOrg.organization.keys.max()!!
        if (capacity < participantCount) {
            throw LastwolfBusinessException("現在の参加人数（${participantCount}人）が定員を超えています")
        }
    }

    private fun assertCharachip(charachip: VillageCharachip, resourceCharachip: VillageCharachipCreateResource) {
        if (charachip.charachipId != resourceCharachip.charachipId
            || charachip.dummyCharaId != resourceCharachip.dummyCharaId
        ) {
            throw LastwolfBusinessException("キャラチップとダミーキャラは変更できません")
        }
    }
}