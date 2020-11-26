package com.ort.lastwolf.api.view.village

import com.ort.lastwolf.domain.model.village.Village
import com.ort.lastwolf.domain.model.village.participant.VillageParticipants

data class VillageParticipantsView(
    val count: Int,
    val memberList: List<VillageParticipantView>
) {
    constructor(
        village: Village,
        participants: VillageParticipants,
        shouldHidePlayer: Boolean
    ) : this(
        count = participants.count,
        memberList = participants.list.map {
            VillageParticipantView(
                participant = village.participants.first(it.id),
                shouldHidePlayer = shouldHidePlayer
            )
        }
    )
}
