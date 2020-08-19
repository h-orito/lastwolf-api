package com.ort.lastwolf.domain.model.charachip

import com.ort.lastwolf.domain.model.village.participant.VillageParticipants

data class Charas(
    val list: List<Chara>
) {
    fun chara(id: Int): Chara = list.first { it.id == id }

    fun chara(participants: VillageParticipants, participantId: Int): Chara = chara(participants.member(participantId).charaId)
}

