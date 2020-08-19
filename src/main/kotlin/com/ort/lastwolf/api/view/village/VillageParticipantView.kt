package com.ort.lastwolf.api.view.village

import com.ort.lastwolf.api.view.charachip.CharaView
import com.ort.lastwolf.api.view.dead.DeadView
import com.ort.lastwolf.api.view.player.PlayerView
import com.ort.lastwolf.domain.model.charachip.Charas
import com.ort.lastwolf.domain.model.player.Players
import com.ort.lastwolf.domain.model.skill.Skill
import com.ort.lastwolf.domain.model.skill.SkillRequest
import com.ort.lastwolf.domain.model.village.Village
import com.ort.lastwolf.domain.model.village.participant.coming_out.ComingOuts

data class VillageParticipantView(
    val id: Int,
    val chara: CharaView,
    val player: PlayerView?,
    val dead: DeadView?,
    val isSpectator: Boolean,
    val skill: Skill?,
    val skillRequest: SkillRequest?,
    val isWin: Boolean?,
    val commingOuts: ComingOuts
) {
    constructor(
        village: Village,
        villageParticipantId: Int,
        players: Players,
        charas: Charas,
        shouldHidePlayer: Boolean
    ) : this(
        id = village.memberById(villageParticipantId).id,
        chara = CharaView(charas.chara(village.memberById(villageParticipantId).charaId)),
        player = if (shouldHidePlayer || village.memberById(villageParticipantId).playerId == null) null
        else PlayerView(players.list.find { it.id == village.memberById(villageParticipantId).playerId }!!),
        dead = village.memberById(villageParticipantId).dead?.let { DeadView(it, shouldHidePlayer) },
        isSpectator = village.memberById(villageParticipantId).isSpectator,
        skill = if (shouldHidePlayer) null else village.memberById(villageParticipantId).skill,
        skillRequest = if (shouldHidePlayer) null else village.memberById(villageParticipantId).skillRequest,
        isWin = village.memberById(villageParticipantId).isWin,
        commingOuts = village.memberById(villageParticipantId).commigOuts
    )
}