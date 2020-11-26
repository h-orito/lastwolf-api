package com.ort.lastwolf.domain.service.daychange

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
        // 延長
        return extendPrologue(dayChange).setIsChange(dayChange)
    }

    // ===================================================================================
    //                                                                        Assist Logic
    //                                                                        ============
    // 日付を進める必要があるか
    private fun shouldForward(village: Village): Boolean =
        !LastwolfDateUtil.currentLocalDateTime().isBefore(village.days.latestDay().endDatetime)

    private fun extendPrologue(dayChange: DayChange): DayChange {
        return dayChange.copy(
            village = dayChange.village.extendPrologue(),
            messages = dayChange.messages.add(dayChange.village.createExtendPrologueMessage())
        )
    }
}