package com.ort.lastwolf.domain.service.daychange

import com.ort.dbflute.allcommon.CDef
import com.ort.lastwolf.domain.model.daychange.DayChange
import com.ort.lastwolf.domain.model.village.Village
import com.ort.lastwolf.domain.service.camp.CampDomainService
import com.ort.lastwolf.fw.LastwolfDateUtil
import org.springframework.stereotype.Service

@Service
class EpilogueDomainService(
    private val campDomainService: CampDomainService
) {

    fun transitionToDrawEpilogue(dayChange: DayChange): DayChange {
        // 決着がついた
        return epilogueVillage(dayChange, true).setIsChange(dayChange)
    }

    fun transitionToEpilogueIfNeeded(dayChange: DayChange): DayChange {
        // まだ決着がついていない
        if (!dayChange.village.isSettled()) return dayChange
        // 決着がついた
        return epilogueVillage(dayChange, false).setIsChange(dayChange)
    }

    fun addDayIfNeeded(dayChange: DayChange): DayChange {
        // 日付更新の必要がなかったら終了
        if (!shouldForward(dayChange.village)) return dayChange
        // 日付追加
        return addNewDay(dayChange).setIsChange(dayChange)
    }

    fun toFinished(dayChange: DayChange): DayChange {
        // ステータス変更
        val village = dayChange.village.changeStatus(CDef.VillageStatus.終了)
        return dayChange.copy(village = village).setIsChange(dayChange)
    }

    // ===================================================================================
    //                                                                        Assist Logic
    //                                                                        ============
    private fun epilogueVillage(beforeDayChange: DayChange, isDraw: Boolean): DayChange {
        // ステータス変更、勝利陣営設定、勝敗設定
        var dayChange = toEpilogueVillage(beforeDayChange, isDraw)
        // エピローグ遷移メッセージ、参加者一覧メッセージ登録
        dayChange = addMessageToEpilogue(dayChange)

        return dayChange.setIsChange(beforeDayChange)
    }

    private fun toEpilogueVillage(dayChange: DayChange, isDraw: Boolean): DayChange {
        return dayChange.copy(village = dayChange.village.toEpilogue(isDraw))
    }

    private fun addMessageToEpilogue(dayChange: DayChange): DayChange {
        val latestDayId = dayChange.village.days.latestDay().id
        return dayChange.copy(
            messages = dayChange.messages
                // エピローグ遷移メッセージ登録
                .add(campDomainService.createWinCampMessage(dayChange.village.winCamp?.toCdef(), latestDayId))
        )
    }

    // 日付を進める必要があるか
    private fun shouldForward(village: Village): Boolean {
        // 更新日時を過ぎている
        return !LastwolfDateUtil.currentLocalDateTime().isBefore(village.days.latestDay().endDatetime)
    }

    // 日付追加
    private fun addNewDay(dayChange: DayChange): DayChange {
        return dayChange.copy(
            village = dayChange.village.addNewDay()
        )
    }
}