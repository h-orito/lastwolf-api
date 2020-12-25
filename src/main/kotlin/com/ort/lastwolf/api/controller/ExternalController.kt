package com.ort.lastwolf.api.controller

import com.ort.dbflute.allcommon.CDef
import com.ort.lastwolf.api.view.external.RecruitingVillagesView
import com.ort.lastwolf.application.service.CharachipService
import com.ort.lastwolf.application.service.VillageService
import com.ort.lastwolf.domain.model.village.VillageStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ExternalController(
    val villageService: VillageService,
    val charachipService: CharachipService
) {

    @GetMapping("/recruiting-village-list")
    fun recruitingVillageList(): RecruitingVillagesView {
        val villageList = villageService.findVillages(
            villageStatusList = listOf(
                VillageStatus(CDef.VillageStatus.募集中),
                VillageStatus(CDef.VillageStatus.点呼中),
                VillageStatus(CDef.VillageStatus.進行中),
                VillageStatus(CDef.VillageStatus.決着)
            )
        ).list.sortedBy { it.id }

        val charachips = charachipService.findCharaChips()

        return RecruitingVillagesView(
            villageList = villageList,
            charachips = charachips
        )
    }
}