package com.ort.lastwolf.domain.model.myself.participant

data class VillageCreatorSituation(
    val isAvailableCreatorSetting: Boolean,
    val isAvailableCreatorSay: Boolean,
    val isAvailableStartVillage: Boolean,
    val isAvailableCancelVillage: Boolean,
    val isAvailableKick: Boolean,
    val isAvailableModifySetting: Boolean,
    val isAvailableStartRollCall: Boolean,
    val isAvailableCancelRollCall: Boolean
)
