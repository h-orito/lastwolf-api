package com.ort.lastwolf.domain.model.myself.participant

import com.ort.lastwolf.domain.model.skill.Skill
import com.ort.lastwolf.domain.model.village.participant.coming_out.ComingOut

data class VillageComingOutSituation(
    val isAvailableComingOut: Boolean,
    val currentComingOut: ComingOut?,
    val selectableSkillList: List<Skill>
)
