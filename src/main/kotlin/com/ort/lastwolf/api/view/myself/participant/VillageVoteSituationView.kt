package com.ort.lastwolf.api.view.myself.participant

import com.ort.lastwolf.api.view.village.VillageParticipantView
import com.ort.lastwolf.domain.model.charachip.Charas
import com.ort.lastwolf.domain.model.myself.participant.VillageVoteSituation
import com.ort.lastwolf.domain.model.player.Players
import com.ort.lastwolf.domain.model.village.Village

data class VillageVoteSituationView(
    val isAvailableVote: Boolean,
    val targetList: List<VillageParticipantView>,
    val target: VillageParticipantView?
) {
    constructor(
        situation: VillageVoteSituation,
        village: Village,
        players: Players,
        charas: Charas,
        shouldHidePlayer: Boolean
    ) : this(
        isAvailableVote = situation.isAvailableVote,
        targetList = situation.targetList.map {
            VillageParticipantView(
                village = village,
                villageParticipantId = it.id,
                players = players,
                charas = charas,
                shouldHidePlayer = shouldHidePlayer
            )
        },
        target = situation.target?.let {
            VillageParticipantView(
                village = village,
                villageParticipantId = it.id,
                players = players,
                charas = charas,
                shouldHidePlayer = shouldHidePlayer
            )
        }
    )
}
