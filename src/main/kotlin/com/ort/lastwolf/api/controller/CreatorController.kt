package com.ort.lastwolf.api.controller

import com.ort.dbflute.allcommon.CDef
import com.ort.lastwolf.api.body.CreatorKickBody
import com.ort.lastwolf.api.body.CreatorSayBody
import com.ort.lastwolf.api.view.message.MessageView
import com.ort.lastwolf.application.coordinator.VillageCoordinator
import com.ort.lastwolf.application.service.CharachipService
import com.ort.lastwolf.application.service.MessageService
import com.ort.lastwolf.application.service.PlayerService
import com.ort.lastwolf.application.service.VillageService
import com.ort.lastwolf.domain.model.charachip.Chara
import com.ort.lastwolf.domain.model.message.Message
import com.ort.lastwolf.domain.model.message.MessageContent
import com.ort.lastwolf.domain.model.message.MessageTime
import com.ort.lastwolf.fw.exception.LastwolfBusinessException
import com.ort.lastwolf.fw.security.LastwolfUser
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime
import java.time.ZoneOffset

@RestController
class CreatorController(
    private val villageService: VillageService,
    private val playerService: PlayerService,
    private val messageService: MessageService,
    private val charachipService: CharachipService,
    private val villageCoordinator: VillageCoordinator
) {

    @PostMapping("/creator/village/{villageId}/kick")
    fun kick(
        @PathVariable("villageId") villageId: Int,
        @AuthenticationPrincipal user: LastwolfUser,
        @RequestBody @Validated body: CreatorKickBody
    ) {
        val village = villageService.findVillage(villageId)
        val player = playerService.findPlayer(user)

        if (user.authority != CDef.Authority.管理者 && village.creatorPlayer.id != player.id)
            throw LastwolfBusinessException("村建てか管理者しか使えません")

        // キック
        val changedVillage = village.leaveParticipant(body.targetId!!)
        val participant = village.participants.first(body.targetId)
        if (participant.player.id == 1) return // ダミーはキックできない
        // シスメ
        val chara: Chara = charachipService.findChara(participant.chara.id)
        val updatedVillage = villageService.updateVillageDifference(village, changedVillage)
        messageService.registerLeaveMessage(updatedVillage, chara)
    }

    @PostMapping("/creator/village/{villageId}/cancel")
    fun cancel(
        @PathVariable("villageId") villageId: Int,
        @AuthenticationPrincipal user: LastwolfUser
    ) {
        val village = villageService.findVillage(villageId)
        val player = playerService.findPlayer(user)

        if (user.authority != CDef.Authority.管理者 && village.creatorPlayer.id != player.id)
            throw LastwolfBusinessException("村建てか管理者しか使えません")

        val changedVillage = village.changeStatus(CDef.VillageStatus.廃村)
        villageService.updateVillageDifference(village, changedVillage)
        val message = village.createCreatorCancelVillageMessage()
        messageService.registerMessage(village, message)
    }

    @PostMapping("/creator/village/{villageId}/say-confirm")
    fun sayConfirm(
        @PathVariable("villageId") villageId: Int,
        @AuthenticationPrincipal user: LastwolfUser,
        @RequestBody @Validated body: CreatorSayBody
    ): MessageView {
        val village = villageService.findVillage(villageId)
        val player = playerService.findPlayer(user)

        if (user.authority != CDef.Authority.管理者 && village.creatorPlayer.id != player.id)
            throw LastwolfBusinessException("村建てか管理者しか使えません")

        villageCoordinator.confirmToCreatorSay(village, body.message!!)
        return MessageView(
            message = Message(
                fromParticipantId = null,
                time = MessageTime(
                    villageDayId = village.days.latestDay().id,
                    datetime = LocalDateTime.now(),
                    unixTimeMilli = LocalDateTime.now().toInstant(ZoneOffset.ofHours(+9)).toEpochMilli()
                ),
                content = MessageContent.invoke(
                    messageType = CDef.MessageType.村建て発言.code(),
                    text = body.message,
                    isStrong = false
                )
            ),
            village = village,
            shouldHidePlayer = true
        )
    }

    @PostMapping("/creator/village/{villageId}/say")
    fun say(
        @PathVariable("villageId") villageId: Int,
        @AuthenticationPrincipal user: LastwolfUser,
        @RequestBody @Validated body: CreatorSayBody
    ) {
        val village = villageService.findVillage(villageId)
        val player = playerService.findPlayer(user)

        if (user.authority != CDef.Authority.管理者 && village.creatorPlayer.id != player.id)
            throw LastwolfBusinessException("村建てか管理者しか使えません")

        villageCoordinator.confirmToCreatorSay(village, body.message!!)
        villageCoordinator.creatorSay(village, body.message)
    }

    @PostMapping("/creator/village/{villageId}/start-rollcall")
    fun startRollCall(
        @PathVariable("villageId") villageId: Int,
        @AuthenticationPrincipal user: LastwolfUser
    ) {
        villageCoordinator.startRollCall(villageId, user)
    }

    @PostMapping("/creator/village/{villageId}/cancel-rollcall")
    fun cancelRollCall(
        @PathVariable("villageId") villageId: Int,
        @AuthenticationPrincipal user: LastwolfUser
    ) {
        villageCoordinator.cancelRollCall(villageId, user)
    }

    @PostMapping("/creator/village/{villageId}/start-village")
    fun startVillage(
        @PathVariable("villageId") villageId: Int,
        @AuthenticationPrincipal user: LastwolfUser
    ) {
        villageCoordinator.startVillage(villageId, user)
    }
}