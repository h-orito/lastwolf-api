package com.ort.lastwolf.api.view.village

import com.ort.lastwolf.domain.model.village.setting.VillageTime
import java.time.LocalDateTime
import java.time.LocalTime

data class VillageTimeView(
    val termType: String,
    val startDatetime: LocalDateTime,
    val dayChangeIntervalSeconds: Int,
    val silentHours: Int?,
    val sayableStart: LocalTime,
    val sayableEnd: LocalTime
) {
    constructor(
        villageTime: VillageTime
    ) : this(
        termType = villageTime.termType,
        startDatetime = villageTime.startDatetime,
        dayChangeIntervalSeconds = villageTime.dayChangeIntervalSeconds,
        silentHours = villageTime.silentHours,
        sayableStart = if (villageTime.silentHours == null) villageTime.startDatetime.toLocalTime()
        else villageTime.startDatetime.plusHours(villageTime.silentHours.toLong()).toLocalTime(),
        sayableEnd = villageTime.startDatetime.toLocalTime()
    )
}