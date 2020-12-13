package com.ort.lastwolf.domain.model.village

import com.ort.dbflute.allcommon.CDef

data class VillageDays(
    val list: List<VillageDay>
) {
    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    private val extendHours: Long = 24L

    // ===================================================================================
    //                                                                             Execute
    //                                                                           =========
    fun first(id: Int): VillageDay = list.first { it.id == id }

    fun latestDay(): VillageDay {
        return list.last()
    }

    fun yesterday(): VillageDay {
        check(list.size >= 2) { "no exists yesterday" }
        return list[list.size - 2]
    }

    fun latestNoonDay(): VillageDay {
        return list.last { it.isNoonTime() }
    }

    fun prologueDay(): VillageDay {
        return list.find { it.day == 1 && it.noonNight.code == CDef.Noonnight.昼.code() }!!
    }

    fun existsDifference(villageDays: VillageDays): Boolean {
        if (list.size != villageDays.list.size) return true
        return list.any { day1 ->
            villageDays.list.none { day2 -> !day1.existsDifference(day2) }
        }
    }

    fun extendPrologue(): VillageDays {
        return this.copy(list = list.map {
            if (it.id == latestDay().id) latestDay().copy(endDatetime = latestDay().endDatetime.plusHours(1L))
            else it
        })
    }

    fun extendRollCall(): VillageDays {
        return this.copy(list = list.map {
            if (it.id == latestDay().id) latestDay().copy(endDatetime = latestDay().endDatetime.plusMinutes(10L))
            else it
        })
    }

    fun toLatestDayEpilogue(): VillageDays {
        return this.copy(list = list.map {
            if (it.day == latestDay().day && it.noonNight.code == latestDay().noonNight.code) {
                latestDay().copy(
                    endDatetime = yesterday().endDatetime.plusHours(extendHours),
                    isEpilogue = true
                )
            } else {
                it
            }
        })
    }
}
