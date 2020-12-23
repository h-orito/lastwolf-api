package com.ort.lastwolf.application.coordinator

import com.ort.dbflute.allcommon.CDef
import com.ort.lastwolf.application.service.AbilityService
import com.ort.lastwolf.application.service.CharachipService
import com.ort.lastwolf.application.service.ComingOutService
import com.ort.lastwolf.application.service.CommitService
import com.ort.lastwolf.application.service.MessageService
import com.ort.lastwolf.application.service.PlayerService
import com.ort.lastwolf.application.service.VillageService
import com.ort.lastwolf.application.service.VoteService
import com.ort.lastwolf.domain.model.ability.AbilityType
import com.ort.lastwolf.domain.model.charachip.Chara
import com.ort.lastwolf.domain.model.charachip.Charas
import com.ort.lastwolf.domain.model.commit.Commit
import com.ort.lastwolf.domain.model.message.Message
import com.ort.lastwolf.domain.model.message.MessageContent
import com.ort.lastwolf.domain.model.myself.participant.SituationAsParticipant
import com.ort.lastwolf.domain.model.player.Player
import com.ort.lastwolf.domain.model.skill.Skill
import com.ort.lastwolf.domain.model.skill.SkillRequest
import com.ort.lastwolf.domain.model.village.Village
import com.ort.lastwolf.domain.model.village.VillageCreateResource
import com.ort.lastwolf.domain.model.village.ability.VillageAbilities
import com.ort.lastwolf.domain.model.village.ability.VillageAbility
import com.ort.lastwolf.domain.model.village.participant.VillageParticipant
import com.ort.lastwolf.domain.model.village.vote.VillageVote
import com.ort.lastwolf.domain.model.village.vote.VillageVotes
import com.ort.lastwolf.domain.service.ability.AbilityDomainService
import com.ort.lastwolf.domain.service.coming_out.ComingOutDomainService
import com.ort.lastwolf.domain.service.commit.CommitDomainService
import com.ort.lastwolf.domain.service.creator.CreatorDomainService
import com.ort.lastwolf.domain.service.daychange.RollCallingDomainService
import com.ort.lastwolf.domain.service.participate.ParticipateDomainService
import com.ort.lastwolf.domain.service.roll_call.RollCallDomainService
import com.ort.lastwolf.domain.service.say.SayDomainService
import com.ort.lastwolf.domain.service.skill.SkillRequestDomainService
import com.ort.lastwolf.domain.service.village.VillageSettingDomainService
import com.ort.lastwolf.domain.service.vote.VoteDomainService
import com.ort.lastwolf.fw.exception.LastwolfBusinessException
import com.ort.lastwolf.fw.security.LastwolfUser
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class VillageCoordinator(
    // application service
    private val dayChangeCoordinator: DayChangeCoordinator,
    private val villageService: VillageService,
    private val playerService: PlayerService,
    private val messageService: MessageService,
    private val charachipService: CharachipService,
    private val abilityService: AbilityService,
    private val voteService: VoteService,
    private val commitService: CommitService,
    private val comingOutService: ComingOutService,
    // domain service
    private val participateDomainService: ParticipateDomainService,
    private val rollCallingDomainService: RollCallingDomainService,
    private val rollCallDomainService: RollCallDomainService,
    private val skillRequestDomainService: SkillRequestDomainService,
    private val commitDomainService: CommitDomainService,
    private val sayDomainService: SayDomainService,
    private val abilityDomainService: AbilityDomainService,
    private val voteDomainService: VoteDomainService,
    private val creatorDomainService: CreatorDomainService,
    private val villageSettingDomainService: VillageSettingDomainService,
    private val comingOutDomainService: ComingOutDomainService
) {

    /**
     * 村参加者取得
     * @param village village
     * @param user user
     * @return 村参加者
     */
    fun findParticipant(village: Village, user: LastwolfUser?): VillageParticipant? {
        user ?: return null
        val player: Player = playerService.findPlayer(user)
        return village.participants.findByPlayerId(player.id)
    }

    /**
     * 村登録確認
     * @param paramVillage village
     * @param user user
     */
    fun confirmVillage(paramVillage: Village, user: LastwolfUser) {
        // 作成できない状況ならエラー
        val player: Player = playerService.findPlayer(user)
        player.assertCreateVillage(user)
    }

    /**
     * 村登録
     * @param paramVillage village
     * @param user user
     * @return 村ID
     */
    @Transactional(rollbackFor = [Exception::class, LastwolfBusinessException::class])
    fun registerVillage(paramVillage: Village, user: LastwolfUser): Int {
        // 作成できない状況ならエラー
        val player: Player = playerService.findPlayer(user)
        player.assertCreateVillage(user)
        // 村を登録
        val village: Village = registerVillage(paramVillage)

        return village.id
    }

    /**
     * 村設定変更確認
     */
    fun assertModifySetting(
        village: Village,
        player: Player,
        resource: VillageCreateResource
    ) {
        if (!creatorDomainService.convertToSituation(village, player).isAvailableModifySetting) {
            throw LastwolfBusinessException("設定を変更できません")
        }
        villageSettingDomainService.assertModify(village, resource)
    }

    /**
     * 村設定変更
     */
    @Transactional(rollbackFor = [Exception::class, LastwolfBusinessException::class])
    fun modifySetting(
        village: Village,
        player: Player,
        resource: VillageCreateResource
    ) {
        assertModifySetting(village, player, resource)
        // 変更なしの場合もある
        villageSettingDomainService.createModifyMessage(village, resource)?.let { message ->
            messageService.registerMessage(village, message)
            var changedVillage = Village.createForUpdate(village, resource)
            if (!resource.setting.rule.isAvailableSkillRequest) {
                changedVillage = changedVillage.changeAllSkillRequestLeftover()
            }
            villageService.updateVillageDifference(village, changedVillage)
        }
    }

    /** 点呼開始 */
    fun startRollCall(villageId: Int, user: LastwolfUser) {
        val village = villageService.findVillage(villageId)
        val player = playerService.findPlayer(user)
        // 開始できない状況ならエラー
        rollCallingDomainService.assertStartRollCall(village, player)
        // 開始
        val changedVillage = village.startRollCall()
        villageService.updateVillageDifference(village, changedVillage)
        // メッセージ
        messageService.registerMessage(
            village,
            Message.createPublicSystemMessage("点呼が開始されました。\n参加者は進行欄の「準備完了」ボタンを押してください。", village.days.latestDay().id, true)
        )
    }

    /** 点呼をやめる */
    fun cancelRollCall(villageId: Int, user: LastwolfUser) {
        val village = villageService.findVillage(villageId)
        val player = playerService.findPlayer(user)
        // 中止できない状況ならエラー
        rollCallingDomainService.assertCancelRollCall(village, player)
        // 開始
        val changedVillage = village.cancelRollCall()
        villageService.updateVillageDifference(village, changedVillage)
        // メッセージ
        messageService.registerMessage(
            village,
            Message.createPublicSystemMessage("点呼が中止され、全員の準備完了状態が解除されました。", village.days.latestDay().id, true)
        )
    }


    /**
     * 村に参加できるかチェック
     * @param villageId villageId
     * @param user user
     * @param charaId charaId
     * @param firstRequestSkill 役職第1希望
     * @param secondRequestSkill 役職第2希望
     * @param password 入村パスワード
     */
    fun assertParticipate(
        villageId: Int,
        user: LastwolfUser,
        charaId: Int,
        firstRequestSkill: CDef.Skill = CDef.Skill.おまかせ,
        secondRequestSkill: CDef.Skill = CDef.Skill.おまかせ,
        password: String?
    ) {
        // 参加できない状況ならエラー
        val village: Village = villageService.findVillage(villageId)
        val player: Player = playerService.findPlayer(user)

        participateDomainService.assertParticipate(
            player,
            village,
            charaId,
            firstRequestSkill,
            secondRequestSkill,
            password
        )
    }

    /**
     * 村に参加
     * @param villageId villageId
     * @param playerId playerId
     * @param charaId charaId
     * @param firstRequestSkill 役職第1希望
     * @param secondRequestSkill 役職第2希望
     */
    @Transactional(rollbackFor = [Exception::class, LastwolfBusinessException::class])
    fun participate(
        villageId: Int,
        playerId: Int,
        charaId: Int,
        firstRequestSkill: CDef.Skill = CDef.Skill.おまかせ,
        secondRequestSkill: CDef.Skill = CDef.Skill.おまかせ
    ) {
        // 村参加者登録
        var village: Village = villageService.findVillage(villageId)
        val changedVillage: Village = village.participate(
            playerId = playerId,
            charaId = charaId,
            firstRequestSkill = firstRequestSkill,
            secondRequestSkill = secondRequestSkill
        )
        village = villageService.updateVillageDifference(village, changedVillage)
        val participant: VillageParticipant = village.participants.firstByPlayerId(playerId)
        val chara: Chara = charachipService.findChara(charaId)
        // {N}人目、{キャラ名} とユーザー入力の発言
        messageService.registerParticipateMessage(
            village = village,
            participant = participant,
            chara = chara
        )
    }

    /**
     * 役職希望変更
     * @param villageId villageId
     * @param user user
     * @param firstRequestSkill 第1希望
     * @param secondRequestSkill 第2希望
     */
    fun changeSkillRequest(villageId: Int, user: LastwolfUser, firstRequestSkill: String, secondRequestSkill: String) {
        // 役職希望変更できない状況ならエラー
        val village: Village = villageService.findVillage(villageId)
        val participant: VillageParticipant? = findParticipant(village, user)
        skillRequestDomainService.assertSkillRequest(village, participant, firstRequestSkill, secondRequestSkill)
        // 役職希望変更
        val changedVillage: Village = village.changeSkillRequest(
            participant!!.id,
            CDef.Skill.codeOf(firstRequestSkill)!!,
            CDef.Skill.codeOf(secondRequestSkill)!!
        )
        villageService.updateVillageDifference(village, changedVillage)
    }

    /**
     * 点呼
     */
    @Transactional(rollbackFor = [Exception::class, LastwolfBusinessException::class])
    fun rollCall(villageId: Int, user: LastwolfUser, doRollcall: Boolean) {
        // 点呼できない状況ならエラー
        val village: Village = villageService.findVillage(villageId)
        val participant: VillageParticipant? = findParticipant(village, user)
        rollCallDomainService.assertRollCall(village, participant)
        // 点呼
        villageService.updateVillageDifference(
            village,
            village.rollCall(participant!!.id, doRollcall)
        )
    }

    /**
     * 退村
     * @param villageId villageId
     * @param user user
     */
    @Transactional(rollbackFor = [Exception::class, LastwolfBusinessException::class])
    fun leave(villageId: Int, user: LastwolfUser) {
        // 退村できない状況ならエラー
        val village: Village = villageService.findVillage(villageId)
        val participant: VillageParticipant? = findParticipant(village, user)
        participateDomainService.assertLeave(village, participant)
        // 退村
        val updatedVillage: Village = villageService.updateVillageDifference(
            village,
            village.leaveParticipant(participant!!.id)
        )
        // 退村メッセージ
        messageService.registerLeaveMessage(updatedVillage, participant.chara)
    }

    /**
     * 村を開始
     * @param villageId villageId
     * @param user user
     */
    @Transactional(rollbackFor = [Exception::class, LastwolfBusinessException::class])
    fun startVillage(villageId: Int, user: LastwolfUser) {
        // 開始できない状況ならエラー
        val village: Village = villageService.findVillage(villageId)
        val player = playerService.findPlayer(user)
        creatorDomainService.assertStartVillage(village, player)
        // 開始
        dayChangeCoordinator.startVillage(villageId)
    }

    /**
     * 発言できるか確認
     */
    fun confirmToSay(villageId: Int, user: LastwolfUser, messageText: String, messageType: String, isStrong: Boolean) {
        val messageContent: MessageContent = MessageContent.invoke(messageType, messageText, isStrong)
        // 発言できない状況ならエラー
        assertSay(villageId, user, messageContent)
    }

    fun confirmToCreatorSay(village: Village, messageText: String) {
        val messageContent: MessageContent = MessageContent.invoke(CDef.MessageType.村建て発言.code(), messageText, false)
        // 発言できない状況ならエラー
        sayDomainService.assertCreatorSay(village, messageContent)
    }

    /**
     * 発言
     */
    @Transactional(rollbackFor = [Exception::class, LastwolfBusinessException::class])
    fun say(
        villageId: Int,
        user: LastwolfUser,
        messageText: String,
        messageType: String,
        isStrong: Boolean
    ) {
        val messageContent: MessageContent = MessageContent.invoke(messageType, messageText, isStrong)
        // 発言できない状況ならエラー
        assertSay(villageId, user, messageContent)
        // 発言
        val village: Village = villageService.findVillage(villageId)
        val participant: VillageParticipant = findParticipant(village, user)!!
        val message: Message = Message.createSayMessage(participant, village.days.latestDay().id, messageContent)
        messageService.registerMessage(village, message)
    }

    @Transactional(rollbackFor = [Exception::class, LastwolfBusinessException::class])
    fun creatorSay(village: Village, messageText: String) {
        val messageContent: MessageContent = MessageContent.invoke(CDef.MessageType.村建て発言.code(), messageText, false)
        // 発言できない状況ならエラー
        sayDomainService.assertCreatorSay(village, messageContent)
        // 発言
        val message: Message = Message.createCreatorSayMessage(messageText, village.days.latestDay().id)
        messageService.registerMessage(village, message)
    }

    /**
     * 能力セット
     *
     * @param villageId villageId
     * @param user user
     * @param targetId 対象村参加者ID
     * @param abilityTypeCode 能力種別
     */
    @Transactional(rollbackFor = [Exception::class, LastwolfBusinessException::class])
    fun setAbility(villageId: Int, user: LastwolfUser, targetId: Int?, abilityTypeCode: String) {
        // 能力セットできない状況ならエラー
        val village: Village = villageService.findVillage(villageId)
        val participant: VillageParticipant? = findParticipant(village, user)
        val abilityType = AbilityType(abilityTypeCode)
        val abilities = abilityService.findVillageAbilities(villageId)
        abilityDomainService.assertAbility(village, participant, targetId, abilityType, abilities)
        // 能力セット
        val villageAbility = VillageAbility(village.days.latestDay().id, participant!!.id, targetId, abilityType)
        abilityService.updateAbility(village, villageAbility)
        messageService.registerAbilitySetMessage(village, participant, villageAbility)
    }

    /**
     * 投票セット
     *
     * @param villageId villageId
     * @param user user
     * @param targetId 対象村参加者ID
     */
    @Transactional(rollbackFor = [Exception::class, LastwolfBusinessException::class])
    fun setVote(villageId: Int, user: LastwolfUser, targetId: Int) {
        // 投票セットできない状況ならエラー
        val village: Village = villageService.findVillage(villageId)
        val participant: VillageParticipant? = findParticipant(village, user)
        voteDomainService.assertVote(village, participant, targetId)
        // 投票
        val villageVote = VillageVote(
            village.days.latestDay(),
            participant!!.id,
            targetId
        )
        voteService.updateVote(village, villageVote)
        // 投票コミット
        dayChangeCoordinator.dayChangeIfNeeded(villageId)
    }

    /**
     * コミットセット
     *
     * @param villageId villageId
     * @param user user
     * @param doCommit コミットするか
     */
    @Transactional(rollbackFor = [Exception::class, LastwolfBusinessException::class])
    fun setCommit(villageId: Int, user: LastwolfUser, doCommit: Boolean) {
        // コミットできない状況ならエラー
        val village: Village = villageService.findVillage(villageId)
        val participant: VillageParticipant? = findParticipant(village, user)
        commitDomainService.assertCommit(village, participant)
        // コミット
        val commit = Commit(village.days.latestDay().id, participant!!.id, doCommit)
        commitService.updateCommit(village, commit)
        // messageService.registerCommitMessage(village, participant.chara, doCommit)
        // 日付更新
        if (doCommit) dayChangeCoordinator.dayChangeIfNeeded(village.id)
    }

    /**
     * カミングアウトセット
     *
     * @param villageId villageId
     * @param user user
     * @param skill skill
     */
    @Transactional(rollbackFor = [Exception::class, LastwolfBusinessException::class])
    fun setComingOut(villageId: Int, user: LastwolfUser, skill: Skill?) {
        // カミングアウトできない状況ならエラー
        val village: Village = villageService.findVillage(villageId)
        val participant: VillageParticipant? = findParticipant(village, user)
        comingOutDomainService.assertComingOut(village, participant)
        // カミングアウト
        if (skill == null) {
            comingOutService.deleteComingOut(participant!!.id)
        } else comingOutService.registerComingOut(
            participant!!.id,
            skill
        )
        messageService.registerComingOutMessage(village, participant.chara, skill)
    }

    /**
     * 参加状況や可能なアクションを取得
     * @param village village
     * @param user user
     * @param charas charas
     */
    fun findActionSituation(
        village: Village,
        user: LastwolfUser?,
        charas: Charas
    ): SituationAsParticipant {
        val player: Player? = if (user == null) null else playerService.findPlayer(user)
        val participant: VillageParticipant? = findParticipant(village, user)
        val skillRequest: SkillRequest? = participant?.skillRequest
        val votes: VillageVotes = voteService.findVillageVotes(village.id)
        val commit: Commit? = commitService.findCommit(village, participant)
        val abilities: VillageAbilities = abilityService.findVillageAbilities(village.id)

        return SituationAsParticipant(
            participate = participateDomainService.convertToSituation(
                village, participant, player, charas
            ),
            rollCall = rollCallDomainService.convertToSituation(village, participant),
            skillRequest = skillRequestDomainService.convertToSituation(village, participant, skillRequest),
            commit = commitDomainService.convertToSituation(village, participant, commit),
            comingOut = comingOutDomainService.convertToSituation(village, participant),
            say = sayDomainService.convertToSituation(village, participant),
            ability = abilityDomainService.convertToSituationList(village, participant, abilities),
            vote = voteDomainService.convertToSituation(village, participant, votes),
            creator = creatorDomainService.convertToSituation(village, player)
        )
    }

    // ===================================================================================
    //                                                                        Assist Logic
    //                                                                        ============
    private fun registerVillage(paramVillage: Village): Village {
        // 村を登録
        val village: Village = villageService.registerVillage(paramVillage)
        // 村作成時のシステムメッセージを登録
        messageService.registerInitialMessage(village)
        // ダミーキャラを参加させる
        participateDummyChara(village.id, village)

        return village
    }

    private fun participateDummyChara(villageId: Int, village: Village) {
        val dummyPlayerId = 1 // 固定
        this.participate(
            villageId = villageId,
            playerId = dummyPlayerId,
            charaId = village.setting.charachip.dummyCharaId
        )
    }

    private fun assertSay(villageId: Int, user: LastwolfUser?, messageContent: MessageContent) {
        val village: Village = villageService.findVillage(villageId)
        val participant: VillageParticipant? = findParticipant(village, user)
        val latestDayMessageList: List<Message> =
            messageService.findParticipateDayMessageList(villageId, village.days.latestDay(), participant)
        sayDomainService.assertSay(village, participant, participant?.chara, latestDayMessageList, messageContent)
    }
}
