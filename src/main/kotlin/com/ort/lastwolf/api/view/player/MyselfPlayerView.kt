package com.ort.lastwolf.api.view.player

import com.fasterxml.jackson.annotation.JsonProperty
import com.ort.lastwolf.api.view.village.VillagesView
import com.ort.lastwolf.domain.model.player.Player
import com.ort.lastwolf.domain.model.village.Villages
import com.ort.lastwolf.fw.security.LastwolfUser

data class MyselfPlayerView(
    val id: Int,
    val nickname: String,
    val twitterUserName: String,
    @JsonProperty("available_create_village")
    val isAvailableCreateVillage: Boolean,
    val participateProgressVillages: VillagesView,
    val participateFinishedVillages: VillagesView,
    val createProgressVillages: VillagesView,
    val createFinishedVillages: VillagesView
) {
    constructor(
        player: Player,
        participantVillages: Villages,
        createVillages: Villages,
        user: LastwolfUser
    ) : this(
        id = player.id,
        nickname = player.nickname,
        twitterUserName = player.twitterUserName,
        isAvailableCreateVillage = player.isAvailableCreateVillage(user),
        participateProgressVillages = VillagesView(Villages(participantVillages.list.filter {
            !it.status.isSolved()
        })),
        participateFinishedVillages = VillagesView(Villages(participantVillages.list.filter {
            it.status.isSolved()
        })),
        createProgressVillages = VillagesView(Villages(createVillages.list.filter {
            !it.status.isSolved()
        })),
        createFinishedVillages = VillagesView(Villages(createVillages.list.filter {
            it.status.isSolved()
        }))
    )
}