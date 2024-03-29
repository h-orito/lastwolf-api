package com.ort.lastwolf.api.view.myself.participant

import com.fasterxml.jackson.annotation.JsonProperty
import com.ort.lastwolf.api.view.village.VillageParticipantView
import com.ort.lastwolf.domain.model.myself.participant.VillageVoteSituation
import com.ort.lastwolf.domain.model.village.Village

data class VillageVoteSituationView(
    @JsonProperty("available_vote")
    val isAvailableVote: Boolean,
    val targetList: List<VillageParticipantView>,
    val target: VillageParticipantView?
) {
    constructor(
        situation: VillageVoteSituation,
        village: Village,
        shouldHidePlayer: Boolean
    ) : this(
        isAvailableVote = situation.isAvailableVote,
        targetList = situation.targetList.map {
            VillageParticipantView(
                participant = village.participants.first(it.id),
                shouldHidePlayer = shouldHidePlayer
            )
        },
        target = situation.target?.let {
            VillageParticipantView(
                participant = village.participants.first(it.id),
                shouldHidePlayer = shouldHidePlayer
            )
        }
    )
}
