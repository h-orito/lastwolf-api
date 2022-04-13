package com.ort.lastwolf.domain.model.myself.participant

import com.fasterxml.jackson.annotation.JsonProperty

data class VillageRollCallSituation(
    @JsonProperty("available_roll_call")
    val isAvailableRollCall: Boolean,
    val doneRollCall: Boolean
)
