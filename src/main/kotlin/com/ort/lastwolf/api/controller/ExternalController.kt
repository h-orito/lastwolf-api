package com.ort.lastwolf.api.controller

import com.ort.dbflute.allcommon.CDef
import com.ort.lastwolf.api.form.VillageRecordListForm
import com.ort.lastwolf.api.view.external.LatestVillageRecordView
import com.ort.lastwolf.api.view.external.RecruitingVillagesView
import com.ort.lastwolf.api.view.external.VillageRecordsView
import com.ort.lastwolf.application.service.CharachipService
import com.ort.lastwolf.application.service.PlayerService
import com.ort.lastwolf.application.service.ReservedVillageService
import com.ort.lastwolf.application.service.VillageService
import com.ort.lastwolf.domain.model.village.VillageStatus
import com.ort.lastwolf.domain.model.village.Villages
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ExternalController(
    val villageService: VillageService,
    val reservedVillageService: ReservedVillageService,
    val charachipService: CharachipService,
    val playerService: PlayerService
) {

    @GetMapping("/recruiting-village-list")
    fun recruitingVillageList(): RecruitingVillagesView {
        val villageList = villageService.findVillages(
            villageStatusList = listOf(
                VillageStatus(CDef.VillageStatus.プロローグ),
                VillageStatus(CDef.VillageStatus.進行中),
                VillageStatus(CDef.VillageStatus.エピローグ)
            )
        ).list.sortedBy { it.id }
        val reservedVillageList = reservedVillageService.findReservedVillages().list

        val charachips = charachipService.findCharaChips()

        return RecruitingVillagesView(
            villageList = villageList,
            reservedVillageList = reservedVillageList,
            charachips = charachips
        )
    }

    @GetMapping("/village-record/list")
    fun villageRecordList(
        form: VillageRecordListForm
    ): VillageRecordsView {
        var villageIdList = villageService.findVillages(
            villageStatusList = listOf(
                VillageStatus(CDef.VillageStatus.エピローグ),
                VillageStatus(CDef.VillageStatus.終了)
            )
        ).list.map { it.id }
        form.vid?.let { vid -> villageIdList = villageIdList.filter { vid.contains(it) } }
        if (villageIdList.isEmpty()) return VillageRecordsView(listOf())
        val villageList = villageService.findVillagesAsDetail(villageIdList).list.sortedBy { it.id }
        val charas = charachipService.findCharas(villageList.map { it.setting.charachip.charachipId }.distinct())
        val players = playerService.findPlayers(
            playerIdList = villageList.flatMap {
                (it.participant.memberList + it.spectator.memberList).map { member -> member.playerId!! }
            }.distinct()
        )
        return VillageRecordsView(
            villages = Villages(villageList),
            charas = charas,
            players = players
        )
    }

    @GetMapping("/village-record/latest-vid")
    fun latestvillageRecord(): LatestVillageRecordView {
        val maxVillageId = villageService.findVillages(
            villageStatusList = listOf(
                VillageStatus(CDef.VillageStatus.エピローグ),
                VillageStatus(CDef.VillageStatus.終了)
            )
        ).list.map { it.id }.max() ?: 0
        return LatestVillageRecordView(
            vid = maxVillageId
        )
    }
}