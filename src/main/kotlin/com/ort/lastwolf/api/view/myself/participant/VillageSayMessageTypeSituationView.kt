package com.ort.lastwolf.api.view.myself.participant

import com.ort.lastwolf.api.view.village.VillageParticipantView
import com.ort.lastwolf.domain.model.charachip.Charas
import com.ort.lastwolf.domain.model.message.MessageType
import com.ort.lastwolf.domain.model.myself.participant.VillageSayMessageTypeSituation
import com.ort.lastwolf.domain.model.myself.participant.VillageSayRestrictSituation
import com.ort.lastwolf.domain.model.player.Players
import com.ort.lastwolf.domain.model.village.Village

data class VillageSayMessageTypeSituationView(
    val messageType: MessageType,
    val restrict: VillageSayRestrictSituation,
    // 秘話用
    val targetList: List<VillageParticipantView>
) {
    constructor(
        situation: VillageSayMessageTypeSituation,
        village: Village,
        players: Players,
        charas: Charas,
        shouldHidePlayer: Boolean
    ) : this(
        messageType = situation.messageType,
        restrict = situation.restrict,
        targetList = situation.targetList.map {
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
