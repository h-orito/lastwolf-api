package com.ort.lastwolf.api.view.village

import com.ort.lastwolf.api.view.charachip.CharaView
import com.ort.lastwolf.api.view.dead.DeadView
import com.ort.lastwolf.api.view.player.PlayerView
import com.ort.lastwolf.domain.model.skill.Skill
import com.ort.lastwolf.domain.model.skill.SkillRequest
import com.ort.lastwolf.domain.model.village.participant.VillageParticipant
import com.ort.lastwolf.domain.model.village.participant.coming_out.ComingOut
import com.ort.lastwolf.domain.model.winlose.WinLose

data class VillageParticipantView(
    val id: Int,
    val chara: CharaView,
    val player: PlayerView?,
    val dead: DeadView?,
    val skill: Skill?,
    val skillRequest: SkillRequest?,
    val winlose: WinLose?,
    val comingOut: ComingOut?,
    val doneRollCall: Boolean
) {
    constructor(
        participant: VillageParticipant,
        shouldHidePlayer: Boolean
    ) : this(
        id = participant.id,
        chara = CharaView(participant.chara),
        player = if (shouldHidePlayer) null else PlayerView(participant.player),
        dead = participant.dead?.let { DeadView(it, shouldHidePlayer) },
        skill = if (shouldHidePlayer) null else participant.skill,
        skillRequest = if (shouldHidePlayer) null else participant.skillRequest,
        winlose = participant.winlose,
        comingOut = participant.comingOut,
        doneRollCall = participant.doneRollCall
    )
}