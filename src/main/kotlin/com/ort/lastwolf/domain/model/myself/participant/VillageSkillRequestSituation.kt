package com.ort.lastwolf.domain.model.myself.participant

import com.fasterxml.jackson.annotation.JsonProperty
import com.ort.lastwolf.domain.model.skill.Skill
import com.ort.lastwolf.domain.model.skill.SkillRequest

data class VillageSkillRequestSituation(
    @JsonProperty("available_skill_request")
    val isAvailableSkillRequest: Boolean,
    val selectableSkillList: List<Skill> = listOf(),
    val skillRequest: SkillRequest?
)
