package com.ort.lastwolf.api.view.myself.participant

import com.ort.lastwolf.domain.model.myself.participant.SituationAsParticipant
import com.ort.lastwolf.domain.model.myself.participant.VillageComingOutSituation
import com.ort.lastwolf.domain.model.myself.participant.VillageCommitSituation
import com.ort.lastwolf.domain.model.myself.participant.VillageCreatorSituation
import com.ort.lastwolf.domain.model.myself.participant.VillageRollCallSituation
import com.ort.lastwolf.domain.model.myself.participant.VillageSaySituation
import com.ort.lastwolf.domain.model.myself.participant.VillageSkillRequestSituation
import com.ort.lastwolf.domain.model.village.Village

data class SituationAsParticipantView(
    val participate: VillageParticipateSituationView,
    val rollCall: VillageRollCallSituation,
    val skillRequest: VillageSkillRequestSituation,
    val commit: VillageCommitSituation,
    val comingOut: VillageComingOutSituation,
    val say: VillageSaySituation,
    val ability: VillageAbilitySituationsView,
    val vote: VillageVoteSituationView,
    val creator: VillageCreatorSituation
) {

    constructor(
        situation: SituationAsParticipant,
        village: Village
    ) : this(
        participate = VillageParticipateSituationView(
            situation = situation.participate,
            village = village
        ),
        rollCall = situation.rollCall,
        skillRequest = situation.skillRequest,
        commit = situation.commit,
        comingOut = situation.comingOut,
        say = situation.say,
        ability = VillageAbilitySituationsView(
            situation = situation.ability,
            village = village,
            shouldHidePlayer = !village.status.isSolved()
        ),
        vote = VillageVoteSituationView(
            situation = situation.vote,
            village = village,
            shouldHidePlayer = !village.status.isSolved()
        ),
        creator = situation.creator
    )
}