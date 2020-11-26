package com.ort.lastwolf.api.view.village

import com.ort.lastwolf.api.view.player.PlayerView
import com.ort.lastwolf.domain.model.camp.Camp
import com.ort.lastwolf.domain.model.village.Village
import com.ort.lastwolf.domain.model.village.VillageDays
import com.ort.lastwolf.domain.model.village.VillageStatus
import com.ort.lastwolf.domain.model.village.participant.VillageParticipants

data class SimpleVillageView(
    val id: Int,
    val name: String,
    val creatorPlayer: PlayerView,
    val status: VillageStatus,
    val winCamp: Camp?,
    val setting: VillageSettingsView,
    val participants: VillageParticipants,
    val days: VillageDays
) {
    constructor(
        village: Village
    ) : this(
        id = village.id,
        name = village.name,
        creatorPlayer = PlayerView(village.creatorPlayer),
        status = village.status,
        winCamp = village.winCamp,
        setting = VillageSettingsView(village.setting),
        participants = VillageParticipants(
            count = village.participants.count,
            list = listOf()
        ),
        days = village.days
    )
}