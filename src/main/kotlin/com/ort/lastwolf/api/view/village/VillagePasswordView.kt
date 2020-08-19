package com.ort.lastwolf.api.view.village

import com.ort.lastwolf.domain.model.village.setting.VillagePassword

data class VillagePasswordView(
    val joinPasswordRequired: Boolean
) {
    constructor(
        villagePassword: VillagePassword
    ) : this(
        joinPasswordRequired = villagePassword.joinPasswordRequired
    )
}