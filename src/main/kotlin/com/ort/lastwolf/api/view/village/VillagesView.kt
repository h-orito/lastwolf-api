package com.ort.lastwolf.api.view.village

import com.ort.lastwolf.domain.model.village.Villages

data class VillagesView(
    val list: List<SimpleVillageView>
) {
    constructor(
        villages: Villages
    ) : this(
        list = villages.list.map { SimpleVillageView(it) }
    )
}
