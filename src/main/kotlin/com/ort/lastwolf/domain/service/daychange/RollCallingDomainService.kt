package com.ort.lastwolf.domain.service.daychange

import com.ort.dbflute.allcommon.CDef
import com.ort.lastwolf.domain.model.daychange.DayChange
import com.ort.lastwolf.domain.model.player.Player
import com.ort.lastwolf.domain.model.village.Village
import com.ort.lastwolf.domain.service.ability.AbilityDomainService
import com.ort.lastwolf.domain.service.ability.DivineDomainService
import com.ort.lastwolf.domain.service.skill.SkillAssignDomainService
import com.ort.lastwolf.fw.LastwolfDateUtil
import com.ort.lastwolf.fw.exception.LastwolfBusinessException
import org.springframework.stereotype.Service

@Service
class RollCallingDomainService(
    private val skillAssignDomainService: SkillAssignDomainService,
    private val abilityDomainService: AbilityDomainService,
    private val divineDomainService: DivineDomainService
) {

    fun assertStartRollCall(village: Village, player: Player) {
        if (!canStartRollCall(village, player)) throw LastwolfBusinessException("点呼を開始できません")
    }

    fun assertCancelRollCall(village: Village, player: Player) {
        if (!canCancelRollCall(village, player)) throw LastwolfBusinessException("点呼を中止できません")
    }

    fun canStartRollCall(village: Village, player: Player?): Boolean {
        // 村建てか管理者でなければNG
        player ?: return false
        if (player.id != 1 && village.creatorPlayer.id != player.id) return false
        // プロローグでなければNG
        if (!village.status.isRecruiting()) return false
        // 最低開始人数以上揃っていなければNG
        return village.setting.capacity.min <= village.participants.count
    }

    fun canCancelRollCall(village: Village, player: Player?): Boolean {
        // 村建てか管理者でなければNG
        player ?: return false
        if (player.id != 1 && village.creatorPlayer.id != player.id) return false
        // 点呼中でなければNG
        return village.status.isRollCalling()
    }

    fun extendIfNeeded(dayChange: DayChange): DayChange {
        // 開始時刻になっていない場合は何もしない
        if (!shouldForward(dayChange.village)) return dayChange
        // 点呼が揃っている場合は何もしない（後続処理で次の日に進むので）
        if (doneAllRollCalling(dayChange)) return dayChange
        // 延長
        return extendRollCall(dayChange).setIsChange(dayChange)
    }

    fun addDayIfNeeded(dayChange: DayChange): DayChange {
        // 開始時刻になっていない場合は何もしない
        if (!shouldForward(dayChange.village)) return dayChange
        // 点呼が揃っていない場合は何もしない
        if (!doneAllRollCalling(dayChange)) return dayChange
        // 新しい日付追加
        return dayChange.copy(village = dayChange.village.addNewDay()).setIsChange(dayChange)
    }

    // 1日目昼→1日目夜
    fun dayChange(beforeDayChange: DayChange): DayChange {
        // 開始メッセージ追加
        var dayChange = addStartMessage(beforeDayChange)
        // 役職割り当て
        dayChange = skillAssignDomainService.assign(dayChange)
        // 役職構成メッセージ追加
        dayChange = addOrganizationMessage(dayChange)
        // 人狼系役職メッセージ追加
        dayChange = addWolfsConfirmMessage(dayChange)
        // 共有がいれば役職メッセージ追加
        dayChange = addMasonsConfirmMessageIfNeeded(dayChange)
        // 妖狐相互メッセージ追加
        dayChange = addFoxsConfirmMessageIfNeeded(dayChange)
        // ステータス変更
        dayChange = dayChange.copy(village = dayChange.village.changeStatus(CDef.VillageStatus.進行中))
        // ダミーが役職を引いていたら能力行使
        dayChange = addDummyAbilityIfNeeded(dayChange)
        // 初日白通知だったらラン白
        dayChange = addRandomNowolfAbilityIfNeeded(dayChange)

        return dayChange.setIsChange(beforeDayChange)
    }

    // ===================================================================================
    //                                                                        Assist Logic
    //                                                                        ============
    // 日付を進める必要があるか
    private fun shouldForward(village: Village): Boolean =
        !LastwolfDateUtil.currentLocalDateTime().isBefore(village.days.latestDay().endDatetime)

    private fun doneAllRollCalling(dayChange: DayChange): Boolean {
        return dayChange.village.notDummyParticipants().list.all { it.doneRollCall }
    }

    private fun extendRollCall(dayChange: DayChange): DayChange {
        return dayChange.copy(
            village = dayChange.village.extendRollCall(),
            messages = dayChange.messages.add(dayChange.village.createExtendRollCallMessage())
        )
    }

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

    private fun addWolfsConfirmMessage(dayChange: DayChange): DayChange {
        return dayChange.copy(
            messages = dayChange.messages.add(dayChange.village.createWolfsConfirmMessage())
        )
    }

    private fun addMasonsConfirmMessageIfNeeded(dayChange: DayChange): DayChange {
        return dayChange.village.createMasonsConfirmMessage()?.let {
            dayChange.copy(
                messages = dayChange.messages.add(it)
            )
        } ?: dayChange
    }

    private fun addFoxsConfirmMessageIfNeeded(dayChange: DayChange): DayChange {
        return dayChange.village.createFoxsConfirmMessage()?.let {
            dayChange.copy(
                messages = dayChange.messages.add(it)
            )
        } ?: dayChange
    }

    // ダミーが役職を引いた場合のみデフォルト能力あり
    private fun addDummyAbilityIfNeeded(beforeDayChange: DayChange): DayChange {
        var dayChange = beforeDayChange.copy()
        dayChange.village.dummyParticipant()!!.skill!!.abilityList.forEach {
            dayChange = abilityDomainService
                .detectDomainService(it)!!
                .processDummyAbility(dayChange)
        }

        return dayChange
    }

    // 初日白通知の場合ランダム白
    private fun addRandomNowolfAbilityIfNeeded(beforeDayChange: DayChange): DayChange {
        val village = beforeDayChange.village
        if (!village.setting.rules.firstDivineNowolf) return beforeDayChange

        var dayChange = beforeDayChange.copy()
        village.participants.list.filter { it.skill!!.toCdef().isHasDivineAbility }.forEach {
            dayChange = divineDomainService.divineRandomNowolf(dayChange, it)
        }
        return dayChange
    }
}