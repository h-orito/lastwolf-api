package com.ort.lastwolf.domain.model.myself.participant

import com.fasterxml.jackson.annotation.JsonProperty
import com.ort.lastwolf.domain.model.skill.Skill
import com.ort.lastwolf.domain.model.village.participant.coming_out.ComingOut

data class VillageComingOutSituation(
    @JsonProperty("available_coming_out")
    val isAvailableComingOut: Boolean,
    val currentComingOut: ComingOut?,
    val selectableSkillList: List<Skill>
)
