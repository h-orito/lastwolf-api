package com.ort.lastwolf.application.service

import com.ort.dbflute.allcommon.CDef
import com.ort.lastwolf.domain.model.skill.Skill
import com.ort.lastwolf.domain.model.skill.Skills
import org.springframework.stereotype.Service

@Service
class SkillService {

    fun findSkills(): Skills {
        return Skills(
            CDef.Skill.listAll().filterNot {
                CDef.Skill.listOfSomeoneSkill().contains(it)
            }.sortedBy {
                it.order().toInt()
            }.map { Skill(it) }
        )
    }
}
