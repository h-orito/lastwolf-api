package com.ort.lastwolf.domain.model.myself.participant

import com.fasterxml.jackson.annotation.JsonProperty

data class VillageCommitSituation(
    @JsonProperty("available_commit")
    val isAvailableCommit: Boolean,
    @JsonProperty("committing")
    val isCommitting: Boolean
)
