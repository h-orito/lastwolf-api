package com.ort.lastwolf.domain.model.village.vote

import com.ort.lastwolf.domain.model.village.VillageDay

data class VillageVote(
    val villageDayId: Int,
    val myselfId: Int,
    val targetId: Int
) {
    constructor(
        villageDay: VillageDay,
        myselfId: Int,
        targetId: Int
    ) : this(
        villageDayId = villageDay.id,
        myselfId = myselfId,
        targetId = targetId
    )
}