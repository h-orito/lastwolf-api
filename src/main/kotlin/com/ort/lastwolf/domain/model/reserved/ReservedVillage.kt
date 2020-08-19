package com.ort.lastwolf.domain.model.reserved

import com.ort.lastwolf.fw.LastwolfDateUtil
import java.time.LocalDateTime

data class ReservedVillage(
    val id: Int,
    val villageCreateDatetime: LocalDateTime,
    val villageStartDatetime: LocalDateTime,
    val organization: String,
    val silentHours: Int,
    val availableDummySkill: Boolean
) {
    fun shouldCreate(): Boolean = villageCreateDatetime.isBefore(LastwolfDateUtil.currentLocalDateTime())
}