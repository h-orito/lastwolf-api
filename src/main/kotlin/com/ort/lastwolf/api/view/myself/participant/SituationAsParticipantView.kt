package com.ort.lastwolf.api.view.myself.participant

import com.ort.lastwolf.domain.model.charachip.Charas
import com.ort.lastwolf.domain.model.myself.participant.SituationAsParticipant
import com.ort.lastwolf.domain.model.myself.participant.VillageComingOutSituation
import com.ort.lastwolf.domain.model.myself.participant.VillageCommitSituation
import com.ort.lastwolf.domain.model.myself.participant.VillageCreatorSituation
import com.ort.lastwolf.domain.model.myself.participant.VillageSkillRequestSituation
import com.ort.lastwolf.domain.model.player.Players
import com.ort.lastwolf.domain.model.village.Village

data class SituationAsParticipantView(
    val participate: VillageParticipateSituationView,
    val skillRequest: VillageSkillRequestSituation,
    val commit: VillageCommitSituation,
    val comingOut: VillageComingOutSituation,
    val say: VillageSaySituationView,
    val ability: VillageAbilitySituationsView,
    val vote: VillageVoteSituationView,
    val creator: VillageCreatorSituation
) {

    constructor(
        situation: SituationAsParticipant,
        village: Village,
        players: Players,
        charas: Charas
    ) : this(
        participate = VillageParticipateSituationView(
            situation = situation.participate,
            village = village,
            players = players,
            charas = charas
        ),
        skillRequest = situation.skillRequest,
        commit = situation.commit,
        comingOut = situation.comingOut,
        say = VillageSaySituationView(
            situation = situation.say,
            village = village,
            players = players,
            charas = charas,
            shouldHidePlayer = !village.status.isSolved()
        ),
        ability = VillageAbilitySituationsView(
            situation = situation.ability,
            village = village,
            players = players,
            charas = charas,
            shouldHidePlayer = !village.status.isSolved()
        ),
        vote = VillageVoteSituationView(
            situation = situation.vote,
            village = village,
            players = players,
            charas = charas,
            shouldHidePlayer = !village.status.isSolved()
        ),
        creator = situation.creator
    )
}