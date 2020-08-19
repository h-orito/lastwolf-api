package com.ort.lastwolf.api.view.village

import com.ort.lastwolf.api.view.player.PlayerView
import com.ort.lastwolf.domain.model.camp.Camp
import com.ort.lastwolf.domain.model.charachip.Charas
import com.ort.lastwolf.domain.model.player.Player
import com.ort.lastwolf.domain.model.player.Players
import com.ort.lastwolf.domain.model.village.Village
import com.ort.lastwolf.domain.model.village.VillageDays
import com.ort.lastwolf.domain.model.village.VillageStatus

data class VillageView(
    val id: Int,
    val name: String,
    val creatorPlayer: PlayerView,
    val status: VillageStatus,
    val winCamp: Camp?,
    val setting: VillageSettingsView,
    val participant: VillageParticipantsView,
    val spectator: VillageParticipantsView,
    val day: VillageDays,
    val isSilentTime: Boolean
) {

    constructor(
        village: Village,
        charas: Charas,
        players: Players,
        createPlayer: Player
    ) : this(
        id = village.id,
        name = village.name,
        creatorPlayer = PlayerView(createPlayer),
        status = village.status,
        winCamp = village.winCamp,
        setting = VillageSettingsView(village.setting),
        participant = VillageParticipantsView(
            village = village,
            participants = village.participant,
            charas = charas,
            players = players,
            shouldHidePlayer = !village.status.isSolved()
        ),
        spectator = VillageParticipantsView(
            village = village,
            participants = village.spectator,
            charas = charas,
            players = players,
            shouldHidePlayer = !village.status.isSolved()
        ),
        day = village.day,
        isSilentTime = village.isSilentTime()
    )
}