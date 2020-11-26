package com.ort.lastwolf.api.controller

import com.ort.lastwolf.api.body.PlayerUpdateNicknameBody
import com.ort.lastwolf.api.view.player.MyselfPlayerView
import com.ort.lastwolf.api.view.player.PlayerRecordsView
import com.ort.lastwolf.application.coordinator.PlayerCoordinator
import com.ort.lastwolf.application.service.CharachipService
import com.ort.lastwolf.application.service.PlayerService
import com.ort.lastwolf.application.service.VillageService
import com.ort.lastwolf.domain.model.player.Player
import com.ort.lastwolf.domain.model.village.Villages
import com.ort.lastwolf.fw.security.LastwolfUser
import org.slf4j.LoggerFactory
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController


@RestController
class PlayerController(
    private val playerCoordinator: PlayerCoordinator,
    private val charachipService: CharachipService,
    private val playerService: PlayerService,
    private val villageService: VillageService
) {

    // ===================================================================================
    //                                                                             Execute
    //                                                                           =========
    @GetMapping("/my-player")
    fun myPlayer(
        @AuthenticationPrincipal user: LastwolfUser
    ): MyselfPlayerView {
        val player: Player = playerService.findPlayer(user)
        val participantVillages: Villages = villageService.findVillages(
            player.participateProgressVillageIdList + player.participateFinishedVillageIdList
        )
        val createVillages: Villages = villageService.findVillages(
            player.createProgressVillageIdList + player.createFinishedVillageIdList
        )
        return MyselfPlayerView(
            player,
            participantVillages,
            createVillages,
            user
        )
    }

    @PostMapping("/player/nickname")
    fun updateNickname(
        @AuthenticationPrincipal user: LastwolfUser,
        @RequestBody @Validated body: PlayerUpdateNicknameBody
    ) {
        playerService.updateNickname(user, body.nickname!!, body.twitterUserName!!)
    }

    private val logger = LoggerFactory.getLogger(PlayerController::class.java)
    @GetMapping("/player/{playerId}/record")
    fun stats(
        @PathVariable("playerId") playerId: Int
    ): PlayerRecordsView {
        val player: Player = playerService.findPlayer(playerId)
        val playerRecords = playerCoordinator.findPlayerRecords(player)
        return PlayerRecordsView(playerRecords)
    }
}