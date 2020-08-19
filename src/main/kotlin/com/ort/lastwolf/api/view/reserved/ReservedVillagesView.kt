package com.ort.lastwolf.api.view.reserved

import com.ort.lastwolf.domain.model.reserved.ReservedVillages

data class ReservedVillagesView(
    val list: List<ReservedVillageView>
) {
    constructor(
        reservedVillages: ReservedVillages
    ) : this(
        list = reservedVillages.list.map { ReservedVillageView(it) }
    )
}