package com.ort.lastwolf.domain.model.village.setting

import com.ort.dbflute.allcommon.CDef
import com.ort.lastwolf.domain.model.noonnight.NoonNight
import java.time.LocalDateTime

data class VillageTime(
    val createDatetime: LocalDateTime,
    val startDatetime: LocalDateTime,
    val noonSeconds: Int,
    val voteSeconds: Int,
    val nightSeconds: Int
) {
    companion object {
        private const val NOON_SECONDS_MAX = 60 * 60
        private const val NOON_SECONDS_MIN = 60 * 3
        private const val VOTE_SECONDS_MAX = 60 * 10
        private const val VOTE_SECONDS_MIN = 60 * 1
        private const val NIGHT_SECONDS_MAX = 60 * 20
        private const val NIGHT_SECONDS_MIN = 60 * 2

        operator fun invoke(
            createDatetime: LocalDateTime?,
            startDatetime: LocalDateTime?,
            noonSeconds: Int?,
            voteSeconds: Int?,
            nightSeconds: Int?
        ): VillageTime {
            requireNotNull(createDatetime)
            requireNotNull(startDatetime)
            requireNotNull(noonSeconds)
            require(NOON_SECONDS_MIN <= noonSeconds && noonSeconds <= NOON_SECONDS_MAX)
            requireNotNull(voteSeconds)
            require(VOTE_SECONDS_MIN <= voteSeconds && voteSeconds <= VOTE_SECONDS_MAX)
            requireNotNull(nightSeconds)
            require(NIGHT_SECONDS_MIN <= nightSeconds && nightSeconds <= NIGHT_SECONDS_MAX)
            return VillageTime(
                createDatetime = createDatetime,
                startDatetime = startDatetime,
                noonSeconds = noonSeconds,
                voteSeconds = voteSeconds,
                nightSeconds = nightSeconds
            )
        }
    }

    fun existsDifference(time: VillageTime): Boolean {
        return startDatetime != time.startDatetime
            || noonSeconds != time.noonSeconds
            || voteSeconds != time.voteSeconds
            || nightSeconds != time.nightSeconds
    }

    fun extendPrologue(): VillageTime {
        return this.copy(
            startDatetime = startDatetime.plusHours(1L)
        )
    }

    fun extendRollCall(): VillageTime {
        return this.copy(
            startDatetime = startDatetime.plusMinutes(10L)
        )
    }

    fun getIntervalSeconds(noonNight: NoonNight): Int {
        return when (noonNight.toCdef()) {
            CDef.Noonnight.昼 -> noonSeconds
            CDef.Noonnight.投票1回目 -> voteSeconds
            CDef.Noonnight.投票2回目 -> voteSeconds
            CDef.Noonnight.投票3回目 -> voteSeconds
            CDef.Noonnight.夜 -> nightSeconds
        }
    }
}