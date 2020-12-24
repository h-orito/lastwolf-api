package com.ort.lastwolf.api.view.external

import com.ort.lastwolf.domain.model.charachip.Charachips
import com.ort.lastwolf.domain.model.village.Village
import java.time.format.DateTimeFormatter

data class RecruitingVillagesView(
    val villageList: List<RecruitingVillageView>
) {

    constructor(
        villageList: List<Village>,
        charachips: Charachips
    ) : this(
        villageList = villageList.map { RecruitingVillageView(it, charachips) }
    )
}

data class RecruitingVillageView(
    val id: Int,
    val name: String,
    val status: String,
    val participantCount: Int,
    val participantCapacity: Int,
    val dayChangeTime: String,
    val startDatetime: String,
    val charachipName: String,
    val url: String,
    val organization: String
) {
    constructor(
        village: Village,
        charachips: Charachips
    ) : this(
        id = village.id,
        name = village.name,
        status = village.status.name,
        participantCount = village.participants.count,
        participantCapacity = village.setting.capacity.max,
        dayChangeTime = village.days.latestDay().endDatetime.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")),
        startDatetime = village.setting.time.startDatetime.format(DateTimeFormatter.ofPattern("uuuu/MM/dd HH:mm")),
        charachipName = charachips.list.first { it.id == village.setting.charachip.charachipId }.name,
        url = "https://lastwolf.netlify.app/village?id=${village.id}",
        organization = village.setting.organizations.organization[village.setting.capacity.max] ?: ""
    )
}