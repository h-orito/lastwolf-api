package com.ort.lastwolf.domain.model.myself.participant

import com.fasterxml.jackson.annotation.JsonProperty

data class VillageCreatorSituation(
    @JsonProperty("available_creator_setting")
    val isAvailableCreatorSetting: Boolean,
    @JsonProperty("available_creator_say")
    val isAvailableCreatorSay: Boolean,
    @JsonProperty("available_start_village")
    val isAvailableStartVillage: Boolean,
    @JsonProperty("available_cancel_village")
    val isAvailableCancelVillage: Boolean,
    @JsonProperty("available_kick")
    val isAvailableKick: Boolean,
    @JsonProperty("available_modify_setting")
    val isAvailableModifySetting: Boolean,
    @JsonProperty("available_start_roll_call")
    val isAvailableStartRollCall: Boolean,
    @JsonProperty("available_cancel_roll_call")
    val isAvailableCancelRollCall: Boolean
)
