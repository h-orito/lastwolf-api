package com.ort.lastwolf.api.controller

import com.ort.lastwolf.api.view.skill.SkillsView
import com.ort.lastwolf.application.service.SkillService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class SkillController(
    val skillService: SkillService
) {

    // ===================================================================================
    //                                                                             Execute
    //                                                                           =========
    @GetMapping("/skill/list")
    fun charachipList(): SkillsView {
        return SkillsView(
            list = skillService.findSkills().list
        )
    }
}
