package com.ort.lastwolf.domain.service.daychange

import com.ort.dbflute.allcommon.CDef
import com.ort.lastwolf.domain.model.charachip.Charas
import com.ort.lastwolf.domain.model.daychange.DayChange
import com.ort.lastwolf.domain.model.village.Village
import com.ort.lastwolf.domain.service.ability.AbilityDomainService
import com.ort.lastwolf.domain.service.skill.SkillAssignDomainService
import com.ort.lastwolf.fw.LastwolfDateUtil
import org.springframework.stereotype.Service

@Service
class PrologueDomainService(
    private val abilityDomainService: AbilityDomainService,
    private val skillAssignDomainService: SkillAssignDomainService
) {
    fun extendIfNeeded(dayChange: DayChange): DayChange {
        // 開始時刻になっていない場合は何もしない
        if (!shouldForward(dayChange.village)) return dayChange
        // 参加人数が足りている場合は何もしない
        if (!isNotEnoughMemberCount(dayChange.village)) return dayChange
        // 延長
        return extendPrologue(dayChange).setIsChange(dayChange)
    }

    fun addDayIfNeeded(
        dayChange: DayChange
    ): DayChange {
        // 開始時刻になっていない場合は何もしない
        if (!shouldForward(dayChange.village)) return dayChange
        // 新しい日付追加
        return dayChange.copy(village = dayChange.village.addNewDay()).setIsChange(dayChange)
    }

    fun dayChange(
        beforeDayChange: DayChange,
        charas: Charas
    ): DayChange {
        // 開始メッセージ追加
        var dayChange = addStartMessage(beforeDayChange)
        // 役職割り当て
        dayChange = skillAssignDomainService.assign(dayChange)
        // 役職構成メッセージ追加
        dayChange = addOrganizationMessage(dayChange)
        // 人狼系役職メッセージ追加
        dayChange = addWolfsConfirmMessage(dayChange, charas)
        // 狂信者がいれば狂信者向けメッセージ追加
        dayChange = addFanaticMessageIfNeeded(dayChange, charas)
        // 共有がいれば役職メッセージ追加
        dayChange = addMasonsConfirmMessageIfNeeded(dayChange, charas)
        // 共鳴がいれば役職メッセージ追加
        dayChange = addSympathizersConfirmMessageIfNeeded(dayChange, charas)
        // ステータス変更
        dayChange = dayChange.copy(village = dayChange.village.changeStatus(CDef.VillageStatus.進行中))
        // デフォルト能力行使指定
        dayChange = abilityDomainService.addDefaultAbilities(dayChange)
        // ダミーキャラ発言
        dayChange = addDummyCharaFirstDayMessageIfNeeded(dayChange, charas)

        return dayChange.setIsChange(beforeDayChange)
    }

    // ===================================================================================
    //                                                                        Assist Logic
    //                                                                        ============
    // 日付を進める必要があるか
    private fun shouldForward(village: Village): Boolean =
        !LastwolfDateUtil.currentLocalDateTime().isBefore(village.day.latestDay().dayChangeDatetime)

    private fun extendPrologue(dayChange: DayChange): DayChange {
        return dayChange.copy(
            village = dayChange.village.extendPrologue(),
            messages = dayChange.messages.add(dayChange.village.createExtendPrologueMessage())
        )
    }

    private fun isNotEnoughMemberCount(village: Village) =
        village.participant.memberList.count { !it.isGone } < village.setting.capacity.min

    private fun addStartMessage(dayChange: DayChange): DayChange {
        return dayChange.copy(
            messages = dayChange.messages.add(dayChange.village.createVillageDay1Message())
        )
    }

    private fun addOrganizationMessage(dayChange: DayChange): DayChange {
        return dayChange.copy(
            messages = dayChange.messages.add(dayChange.village.createOrganizationMessage())
        )
    }

    private fun addWolfsConfirmMessage(dayChange: DayChange, charas: Charas): DayChange {
        return dayChange.copy(
            messages = dayChange.messages.add(dayChange.village.createWolfsConfirmMessage(charas))
        )
    }

    private fun addFanaticMessageIfNeeded(dayChange: DayChange, charas: Charas): DayChange {
        return dayChange.village.createFanaticConfirmMessage(charas)?.let {
            dayChange.copy(
                messages = dayChange.messages.add(it)
            )
        } ?: dayChange
    }

    private fun addMasonsConfirmMessageIfNeeded(dayChange: DayChange, charas: Charas): DayChange {
        return dayChange.village.createMasonsConfirmMessage(charas)?.let {
            dayChange.copy(
                messages = dayChange.messages.add(it)
            )
        } ?: dayChange
    }

    private fun addSympathizersConfirmMessageIfNeeded(dayChange: DayChange, charas: Charas): DayChange {
        return dayChange.village.createSympathizersConfirmMessage(charas)?.let {
            dayChange.copy(
                messages = dayChange.messages.add(it)
            )
        } ?: dayChange
    }

    private fun addDummyCharaFirstDayMessageIfNeeded(dayChange: DayChange, charas: Charas): DayChange {
        return dayChange.village.createDummyCharaFirstDayMessage(charas)?.let {
            dayChange.copy(
                messages = dayChange.messages.add(it)
            )
        } ?: dayChange
    }
}