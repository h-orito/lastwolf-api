package com.ort.lastwolf.api.view.village

import com.ort.lastwolf.domain.model.village.setting.VillageTime
import java.time.LocalDateTime

data class VillageTimeView(
    val startDatetime: LocalDateTime,
    val noonSeconds: Int,
    val voteSeconds: Int,
    val nightSeconds: Int
) {
    constructor(
        villageTime: VillageTime
    ) : this(
        startDatetime = villageTime.startDatetime,
        noonSeconds = villageTime.noonSeconds,
        voteSeconds = villageTime.voteSeconds,
        nightSeconds = villageTime.nightSeconds
    )
}