package com.ort.lastwolf.domain.model.village

import com.ort.dbflute.allcommon.CDef
import com.ort.lastwolf.domain.model.noonnight.NoonNight
import java.time.LocalDateTime

data class VillageDay(
    val id: Int,
    val day: Int,
    val noonNight: NoonNight,
    val isEpilogue: Boolean,
    val startDatetime: LocalDateTime,
    val endDatetime: LocalDateTime
) {

    fun isNoonTime(): Boolean = noonNight.isNoonTime()
    fun isVoteTime(): Boolean = noonNight.isVoteTime()
    fun isNightTime(): Boolean = noonNight.isNightTime()

    // 次の村日付（idと更新日時はダミー値）
    // 1日目昼（事件前夜）→1日目夜→2日目昼→2日目投票→2日目夜
    fun createNextDay(toNextVote: Boolean = false, isEpilogue: Boolean = false): VillageDay {
        if (day == 1 && noonNight.toCdef() == CDef.Noonnight.昼) {
            return VillageDay(
                id = 0,
                day = 1,
                noonNight = NoonNight(CDef.Noonnight.夜),
                isEpilogue = isEpilogue,
                startDatetime = LocalDateTime.now(),
                endDatetime = LocalDateTime.now()
            )
        }
        val nextNoonNight = noonNight.next(toNextVote)
        return VillageDay(
            id = 0,
            day = if (nextNoonNight.toCdef() == CDef.Noonnight.昼) day + 1 else day,
            noonNight = nextNoonNight,
            isEpilogue = isEpilogue,
            startDatetime = LocalDateTime.now(),
            endDatetime = LocalDateTime.now()
        )
    }


    fun existsDifference(villageDay: VillageDay): Boolean {
        return day != villageDay.day
            || noonNight.code != villageDay.noonNight.code
            || isEpilogue != villageDay.isEpilogue
            || startDatetime != villageDay.startDatetime
            || endDatetime != villageDay.endDatetime
    }
}