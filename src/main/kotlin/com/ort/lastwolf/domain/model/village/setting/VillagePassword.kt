package com.ort.lastwolf.domain.model.village.setting

data class VillagePassword(
    val joinPasswordRequired: Boolean,
    val joinPassword: String?,
) {
    companion object {
        operator fun invoke(joinPassword: String?): VillagePassword =
            VillagePassword(
                joinPasswordRequired = !joinPassword.isNullOrEmpty(),
                joinPassword = joinPassword,
            )
    }

    fun existsDifference(password: VillagePassword): Boolean = joinPassword != password.joinPassword
}
