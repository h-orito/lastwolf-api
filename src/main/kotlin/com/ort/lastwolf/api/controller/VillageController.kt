package com.ort.lastwolf.api.controller

import com.ort.dbflute.allcommon.CDef
import com.ort.lastwolf.api.body.VillageAbilityBody
import com.ort.lastwolf.api.body.VillageChangeSkillBody
import com.ort.lastwolf.api.body.VillageCharachipCreateBody
import com.ort.lastwolf.api.body.VillageComingOutBody
import com.ort.lastwolf.api.body.VillageCommitBody
import com.ort.lastwolf.api.body.VillageOrganizationCreateBody
import com.ort.lastwolf.api.body.VillageParticipateBody
import com.ort.lastwolf.api.body.VillageRegisterBody
import com.ort.lastwolf.api.body.VillageRollcallBody
import com.ort.lastwolf.api.body.VillageRuleCreateBody
import com.ort.lastwolf.api.body.VillageSayBody
import com.ort.lastwolf.api.body.VillageSettingRegisterBody
import com.ort.lastwolf.api.body.VillageTimeCreateBody
import com.ort.lastwolf.api.body.VillageVoteBody
import com.ort.lastwolf.api.body.validator.VillageRegisterBodyValidator
import com.ort.lastwolf.api.form.VillageListForm
import com.ort.lastwolf.api.form.VillageMessageForm
import com.ort.lastwolf.api.view.message.MessageView
import com.ort.lastwolf.api.view.message.MessagesView
import com.ort.lastwolf.api.view.myself.participant.SituationAsParticipantView
import com.ort.lastwolf.api.view.village.VillageRegisterView
import com.ort.lastwolf.api.view.village.VillageView
import com.ort.lastwolf.api.view.village.VillagesView
import com.ort.lastwolf.application.coordinator.DayChangeCoordinator
import com.ort.lastwolf.application.coordinator.MessageCoordinator
import com.ort.lastwolf.application.coordinator.VillageCoordinator
import com.ort.lastwolf.application.service.CharachipService
import com.ort.lastwolf.application.service.PlayerService
import com.ort.lastwolf.application.service.VillageService
import com.ort.lastwolf.domain.model.charachip.Charas
import com.ort.lastwolf.domain.model.message.Message
import com.ort.lastwolf.domain.model.message.MessageContent
import com.ort.lastwolf.domain.model.message.MessageTime
import com.ort.lastwolf.domain.model.message.Messages
import com.ort.lastwolf.domain.model.player.Player
import com.ort.lastwolf.domain.model.skill.Skill
import com.ort.lastwolf.domain.model.village.Village
import com.ort.lastwolf.domain.model.village.VillageCharachipCreateResource
import com.ort.lastwolf.domain.model.village.VillageCreateResource
import com.ort.lastwolf.domain.model.village.VillageOrganizationCreateResource
import com.ort.lastwolf.domain.model.village.VillageRuleCreateResource
import com.ort.lastwolf.domain.model.village.VillageSettingCreateResource
import com.ort.lastwolf.domain.model.village.VillageStatus
import com.ort.lastwolf.domain.model.village.VillageTimeCreateResource
import com.ort.lastwolf.domain.model.village.Villages
import com.ort.lastwolf.fw.exception.LastwolfBusinessException
import com.ort.lastwolf.fw.security.LastwolfUser
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.InitBinder
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime
import java.time.ZoneOffset


@RestController
class VillageController(
    val villageRegisterBodyValidator: VillageRegisterBodyValidator,

    val villageCoordinator: VillageCoordinator,
    val messageCoordinator: MessageCoordinator,
    val daychangeCoordinator: DayChangeCoordinator,

    val villageService: VillageService,
    val playerService: PlayerService,
    val charachipService: CharachipService
) {

    @InitBinder("villageRegisterBody")
    fun initBinder(binder: WebDataBinder) {
        binder.addValidators(villageRegisterBodyValidator)
    }

    // ===================================================================================
    //                                                                             Execute
    //                                                                           =========
    /**
     * 村一覧取得
     * @param user user
     * @param form 検索パラメータ
     */
    @GetMapping("/village/list")
    fun villageList(
        @AuthenticationPrincipal user: LastwolfUser?,
        @Validated form: VillageListForm
    ): VillagesView {
        val villageStatusList = form.village_status?.map { VillageStatus(CDef.VillageStatus.codeOf(it)) } ?: listOf()
        val villages: Villages = villageService.findVillages(
            villageStatusList = villageStatusList
        )
        return VillagesView(villages)
    }

    /**
     * 村情報取得
     * @param villageId villageId
     */
    @GetMapping("/village/{villageId}")
    fun village(@PathVariable("villageId") villageId: Int): VillageView {
        val village: Village = villageService.findVillage(villageId)
        return VillageView(village)
    }

    /**
     * 発言取得
     * @param villageId villageId
     * @param day 日付
     * @param noonnight 昼夜
     * @param user user
     */
    @GetMapping("/village/{villageId}/message-list")
    fun message(
        @PathVariable("villageId") villageId: Int,
        @AuthenticationPrincipal user: LastwolfUser?,
        @Validated form: VillageMessageForm
    ): MessagesView {
        val village: Village = villageService.findVillage(villageId, false)
        val messageTypeList = form.message_type_list?.mapNotNull { CDef.MessageType.codeOf(it) }
        val messages: Messages = messageCoordinator.findMessageList(
            village = village,
            user = user,
            from = form.from,
            pageSize = form.page_size,
            pageNum = form.page_num,
            keyword = form.keyword,
            messageTypeList = messageTypeList,
            participantIdList = form.participant_id_list?.filterNotNull() // [null]で来る問題に対応
        )
        return MessagesView(
            messages = messages,
            village = village
        )
    }

    /**
     * 村作成
     * @param user user
     * @param body 村設定
     */
    @PostMapping("/village")
    fun registerVillage(
        @AuthenticationPrincipal user: LastwolfUser,
        @RequestBody @Validated villageRegisterBody: VillageRegisterBody
    ): VillageRegisterView {
        val player: Player = playerService.findPlayer(user)
        val village: Village = Village.createForRegister(
            resource = convertToVillageCreateResource(villageRegisterBody, player)
        )
        val villageId: Int = villageCoordinator.registerVillage(village, user)
        return VillageRegisterView(villageId = villageId)
    }

    /**
     * 村作成確認
     * @param user user
     * @param body 村設定
     */
    @PostMapping("/village/confirm")
    fun confirmRegisterVillage(
        @AuthenticationPrincipal user: LastwolfUser,
        @RequestBody @Validated villageRegisterBody: VillageRegisterBody
    ) {
        val player: Player = playerService.findPlayer(user)
        val village: Village = Village.createForRegister(
            resource = convertToVillageCreateResource(villageRegisterBody, player)
        )
        villageCoordinator.confirmVillage(village, user)
    }

    /**
     * 村参加状況取得
     * @param villageId villageId
     * @param user user
     * @return 参加状況
     */
    @GetMapping("/village/{villageId}/situation")
    fun getParticipateSituation(
        @PathVariable("villageId") villageId: Int,
        @AuthenticationPrincipal user: LastwolfUser?
    ): SituationAsParticipantView {
        val village: Village = villageService.findVillage(villageId)
        val charas: Charas = charachipService.findCharas(village.setting.charachip.charachipId)
        return SituationAsParticipantView(
            situation = villageCoordinator.findActionSituation(village, user, charas),
            village = village
        )
    }

    /**
     * 村参加確認
     * @param villageId villageId
     * @param user user
     * @param body 村参加に必要な情報
     */
    @PostMapping("/village/{villageId}/participate-confirm")
    fun participateConfirm(
        @PathVariable("villageId") villageId: Int,
        @AuthenticationPrincipal user: LastwolfUser,
        @RequestBody @Validated body: VillageParticipateBody
    ) {
        villageCoordinator.assertParticipate(
            villageId = villageId,
            user = user,
            charaId = body.charaId!!,
            firstRequestSkill = CDef.Skill.codeOf(body.firstRequestSkill),
            secondRequestSkill = CDef.Skill.codeOf(body.secondRequestSkill),
            password = body.joinPassword
        )
    }

    /**
     * 村に参加
     * @param villageId villageId
     * @param user user
     * @param body 村設定
     */
    @PostMapping("/village/{villageId}/participate")
    fun participateVillage(
        @PathVariable("villageId") villageId: Int,
        @AuthenticationPrincipal user: LastwolfUser,
        @RequestBody @Validated body: VillageParticipateBody
    ) {
        villageCoordinator.assertParticipate(
            villageId = villageId,
            user = user,
            charaId = body.charaId!!,
            firstRequestSkill = CDef.Skill.codeOf(body.firstRequestSkill),
            secondRequestSkill = CDef.Skill.codeOf(body.secondRequestSkill),
            password = body.joinPassword
        )
        val player = playerService.findPlayer(user)
        villageCoordinator.participate(
            villageId = villageId,
            playerId = player.id,
            charaId = body.charaId,
            firstRequestSkill = CDef.Skill.codeOf(body.firstRequestSkill),
            secondRequestSkill = CDef.Skill.codeOf(body.secondRequestSkill)
        )
    }

    /**
     * 希望役職変更
     * @param villageId villageId
     * @param user user
     * @param body 役職
     */
    @PostMapping("/village/{villageId}/change-skill")
    fun changeSkill(
        @PathVariable("villageId") villageId: Int,
        @AuthenticationPrincipal user: LastwolfUser,
        @RequestBody @Validated body: VillageChangeSkillBody
    ) {
        villageCoordinator.changeSkillRequest(villageId, user, body.firstRequestSkill!!, body.secondRequestSkill!!)
    }

    /**
     * 退村
     * @param villageId villageId
     * @param user user
     */
    @PostMapping("/village/{villageId}/leave")
    fun leave(
        @PathVariable("villageId") villageId: Int,
        @AuthenticationPrincipal user: LastwolfUser
    ) {
        villageCoordinator.leave(villageId, user)
    }

    /**
     * 発言確認
     * @param villageId villageId
     * @param user user
     * @param body 発言内容
     */
    @PostMapping("/village/{villageId}/say-confirm")
    @ResponseBody
    fun sayConfirm(
        @PathVariable("villageId") villageId: Int,
        @AuthenticationPrincipal user: LastwolfUser,
        @RequestBody @Validated body: VillageSayBody
    ): MessageView {
        villageCoordinator.confirmToSay(villageId, user, body.message!!, body.messageType!!, body.strong!!)
        val village = villageService.findVillage(villageId)
        val participant = villageCoordinator.findParticipant(village, user)
        return MessageView(
            message = Message(
                fromParticipantId = participant!!.id,
                time = MessageTime(
                    villageDayId = village.days.latestDay().id,
                    datetime = LocalDateTime.now(),
                    unixTimeMilli = LocalDateTime.now().toInstant(ZoneOffset.ofHours(+9)).toEpochMilli()
                ),
                content = MessageContent.invoke(
                    messageType = body.messageType,
                    text = body.message,
                    isStrong = false
                )
            ),
            village = village,
            shouldHidePlayer = true
        )
    }

    /**
     * 点呼
     * @param villageId villageId
     * @param user user
     * @param body 点呼/取り消し
     */
    @PostMapping("/village/{villageId}/rollcall")
    fun rollcall(
        @PathVariable("villageId") villageId: Int,
        @AuthenticationPrincipal user: LastwolfUser,
        @RequestBody @Validated body: VillageRollcallBody
    ) {
        villageCoordinator.rollCall(villageId, user, body.rollcall!!)
    }

    /**
     * 発言
     * @param villageId villageId
     * @param user user
     * @param body 発言内容
     */
    @PostMapping("/village/{villageId}/say")
    fun say(
        @PathVariable("villageId") villageId: Int,
        @AuthenticationPrincipal user: LastwolfUser,
        @RequestBody @Validated body: VillageSayBody
    ) {
        villageCoordinator.say(villageId, user, body.message!!, body.messageType!!, body.strong!!)
    }

    /**
     * 能力セット
     *
     * @param villageId villageId
     * @param user user
     * @param body 能力セット内容
     */
    @PostMapping("/village/{villageId}/ability")
    fun ability(
        @PathVariable("villageId") villageId: Int,
        @AuthenticationPrincipal user: LastwolfUser,
        @RequestBody @Validated body: VillageAbilityBody
    ) {
        villageCoordinator.setAbility(villageId, user, body.targetId, body.abilityType!!)
    }

    /**
     * 投票セット
     * @param villageId villageId
     * @param user user
     * @param body 投票内容
     */
    @PostMapping("/village/{villageId}/vote")
    fun vote(
        @PathVariable("villageId") villageId: Int,
        @AuthenticationPrincipal user: LastwolfUser,
        @RequestBody @Validated body: VillageVoteBody
    ) {
        villageCoordinator.setVote(villageId, user, body.targetId!!)
    }

    /**
     * コミットセット
     * @param villageId villageId
     * @param user user
     * @param body コミット/取り消し
     */
    @PostMapping("/village/{villageId}/commit")
    fun commit(
        @PathVariable("villageId") villageId: Int,
        @AuthenticationPrincipal user: LastwolfUser,
        @RequestBody @Validated body: VillageCommitBody
    ) {
        villageCoordinator.setCommit(villageId, user, body.commit!!)
    }

    /**
     * カミングアウトセット
     * @param villageId villageId
     * @param user user
     * @param body co内容
     */
    @PostMapping("/village/{villageId}/comingout")
    fun comingout(
        @PathVariable("villageId") villageId: Int,
        @AuthenticationPrincipal user: LastwolfUser,
        @RequestBody @Validated body: VillageComingOutBody
    ) {
        val skill = if (body.skillCode.isNullOrEmpty()) null
        else Skill(CDef.Skill.codeOf(body.skillCode))
        villageCoordinator.setComingOut(
            villageId,
            user,
            skill
        )
    }

    /**
     * 村設定変更確認
     * @param villageId villageId
     * @param user user
     * @param body 村設定内容
     */
    @PostMapping("/village/{villageId}/setting/confirm")
    fun settingConfirm(
        @PathVariable("villageId") villageId: Int,
        @AuthenticationPrincipal user: LastwolfUser,
        @RequestBody @Validated villageRegisterBody: VillageRegisterBody
    ) {
        val village = villageService.findVillage(villageId)
        val player = playerService.findPlayer(user)

        if (user.authority != CDef.Authority.管理者 && village.creatorPlayer.id != player.id)
            throw LastwolfBusinessException("村建てか管理者しか使えません")

        val createResource = convertToVillageCreateResource(villageRegisterBody, player).copy(
            createPlayerId = village.creatorPlayer.id // 管理者に上書きされるのを防ぐ
        )
        villageCoordinator.assertModifySetting(village, player, createResource)
    }

    /**
     * 村設定変更
     * @param villageId villageId
     * @param user user
     * @param body 村設定内容
     */
    @PostMapping("/village/{villageId}/setting")
    fun setting(
        @PathVariable("villageId") villageId: Int,
        @AuthenticationPrincipal user: LastwolfUser,
        @RequestBody @Validated villageRegisterBody: VillageRegisterBody
    ) {
        val village = villageService.findVillage(villageId)
        val player = playerService.findPlayer(user)

        if (user.authority != CDef.Authority.管理者 && village.creatorPlayer.id != player.id)
            throw LastwolfBusinessException("村建てか管理者しか使えません")

        val createResource = convertToVillageCreateResource(villageRegisterBody, player)
        villageCoordinator.modifySetting(village, player, createResource)
    }

    @PostMapping("/village/{villageId}/daychange-check")
    fun checkDaychange(
        @PathVariable("villageId") villageId: Int
    ) {
        daychangeCoordinator.dayChangeIfNeeded(villageId)
    }

    // ===================================================================================
    //                                                                        Assist Logic
    //                                                                        ============
    private fun convertToVillageCreateResource(
        body: VillageRegisterBody,
        player: Player
    ): VillageCreateResource {
        return VillageCreateResource(
            villageName = body.villageName!!,
            createPlayerId = player.id,
            setting = convertToVillageSettingCreateResource(body.setting!!)
        )
    }

    private fun convertToVillageSettingCreateResource(
        body: VillageSettingRegisterBody
    ): VillageSettingCreateResource {
        return VillageSettingCreateResource(
            time = convertToVillageTimeCreateResource(body.time!!),
            organization = convertToVillageOrganizationCreateResource(body.organization!!),
            charachip = convertToVillageCharachipCreateResource(body.charachip!!),
            rule = convertToVillageRuleCreateResource(body.rule!!)

        )
    }

    private fun convertToVillageTimeCreateResource(
        body: VillageTimeCreateBody
    ): VillageTimeCreateResource = VillageTimeCreateResource(
        startDatetime = body.startDatetime!!,
        noonSeconds = body.noonSeconds!!,
        voteSeconds = body.voteSeconds!!,
        nightSeconds = body.nightSeconds!!
    )

    private fun convertToVillageOrganizationCreateResource(
        body: VillageOrganizationCreateBody
    ): VillageOrganizationCreateResource = VillageOrganizationCreateResource(
        organization = body.organization!!
    )

    private fun convertToVillageCharachipCreateResource(
        body: VillageCharachipCreateBody
    ): VillageCharachipCreateResource = VillageCharachipCreateResource(
        charachipId = body.charachipId!!,
        dummyCharaId = body.dummyCharaId!!
    )

    private fun convertToVillageRuleCreateResource(
        body: VillageRuleCreateBody
    ): VillageRuleCreateResource = VillageRuleCreateResource(
        isAvailableSkillRequest = body.availableSkillRequest!!,
        isOpenSkillInGrave = body.openSkillInGrave!!,
        isVisibleGraveMessage = body.visibleGraveMessage!!,
        isAvailableSuddenlyDeath = body.availableSuddenlyDeath!!,
        isAvailableCommit = body.availableCommit!!,
        isAvailableDummySkill = body.availableDummySkill!!,
        isAvailableSameTargetGuard = body.availableSameTargetGuard!!,
        isFirstDivineNowolf = body.firstDivineNowolf!!,
        joinPassword = body.joinPassword
    )
}