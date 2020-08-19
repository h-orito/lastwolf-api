package com.ort.lastwolf.domain.model.ability

import com.ort.lastwolf.domain.model.skill.Skill

data class AbilityTypes(
    val list: List<AbilityType>
) {
    constructor(
        skill: Skill
    ) : this(
        list = skill.getAbilities().list
    )
}
