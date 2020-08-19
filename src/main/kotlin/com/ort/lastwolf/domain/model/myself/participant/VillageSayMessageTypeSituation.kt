package com.ort.lastwolf.domain.model.myself.participant

import com.ort.lastwolf.domain.model.message.MessageType
import com.ort.lastwolf.domain.model.village.participant.VillageParticipant

data class VillageSayMessageTypeSituation(
    val messageType: MessageType,
    val restrict: VillageSayRestrictSituation,
    // 秘話用
    val targetList: List<VillageParticipant>
)