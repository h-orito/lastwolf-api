package com.ort.lastwolf.domain.model.myself.participant

import com.ort.lastwolf.domain.model.charachip.Chara
import com.ort.lastwolf.domain.model.village.participant.VillageParticipant

data class VillageParticipateSituation(
    val isParticipating: Boolean,
    val isAvailableParticipate: Boolean,
    val isAvailableSpectate: Boolean,
    val selectableCharaList: List<Chara>,
    val isAvailableLeave: Boolean,
    val myself: VillageParticipant?
)