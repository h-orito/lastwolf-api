package com.ort.lastwolf.domain.model.myself.participant

import com.ort.lastwolf.domain.model.village.participant.VillageParticipant

data class VillageVoteSituation(
    val isAvailableVote: Boolean,
    val targetList: List<VillageParticipant>,
    val target: VillageParticipant?
)