package com.ort.lastwolf.api.view.debug

import com.ort.lastwolf.api.view.player.PlayerView
import com.ort.lastwolf.api.view.village.VillageParticipantsView
import com.ort.lastwolf.api.view.village.VillageSettingsView
import com.ort.lastwolf.domain.model.player.Player
import com.ort.lastwolf.domain.model.village.Village
import com.ort.lastwolf.domain.model.village.VillageDays
import com.ort.lastwolf.domain.model.village.VillageStatus

data class DebugVillageView(
    val id: Int,
    val name: String,
    val creatorPlayer: PlayerView,
    val status: VillageStatus,
    val setting: VillageSettingsView,
    val participants: VillageParticipantsView,
    val days: VillageDays
) {

    constructor(
        village: Village,
        createPlayer: Player
    ) : this(
        id = village.id,
        name = village.name,
        creatorPlayer = PlayerView(createPlayer),
        status = village.status,
        setting = VillageSettingsView(village.setting),
        participants = VillageParticipantsView(
            village = village,
            participants = village.participants,
            shouldHidePlayer = false
        ),
        days = village.days
    )
}