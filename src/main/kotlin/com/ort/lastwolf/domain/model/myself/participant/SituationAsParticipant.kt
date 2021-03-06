package com.ort.lastwolf.domain.model.myself.participant

data class SituationAsParticipant(
    val participate: VillageParticipateSituation,
    val rollCall: VillageRollCallSituation,
    val skillRequest: VillageSkillRequestSituation,
    val commit: VillageCommitSituation,
    val comingOut: VillageComingOutSituation,
    val say: VillageSaySituation,
    val ability: VillageAbilitySituations,
    val vote: VillageVoteSituation,
    val creator: VillageCreatorSituation
)