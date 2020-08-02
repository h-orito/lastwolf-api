package com.ort.firewolf.application.coordinator

import com.ort.dbflute.allcommon.CDef
import com.ort.firewolf.application.service.AbilityService
import com.ort.firewolf.application.service.CharachipService
import com.ort.firewolf.application.service.CommitService
import com.ort.firewolf.application.service.MessageService
import com.ort.firewolf.application.service.PlayerService
import com.ort.firewolf.application.service.ReservedVillageService
import com.ort.firewolf.application.service.TweetService
import com.ort.firewolf.application.service.VillageService
import com.ort.firewolf.application.service.VoteService
import com.ort.firewolf.domain.model.ability.AbilityType
import com.ort.firewolf.domain.model.charachip.Chara
import com.ort.firewolf.domain.model.charachip.Charas
import com.ort.firewolf.domain.model.commit.Commit
import com.ort.firewolf.domain.model.message.Message
import com.ort.firewolf.domain.model.message.MessageContent
import com.ort.firewolf.domain.model.myself.participant.SituationAsParticipant
import com.ort.firewolf.domain.model.player.Player
import com.ort.firewolf.domain.model.player.Players
import com.ort.firewolf.domain.model.skill.SkillRequest
import com.ort.firewolf.domain.model.village.Village
import com.ort.firewolf.domain.model.village.ability.VillageAbilities
import com.ort.firewolf.domain.model.village.ability.VillageAbility
import com.ort.firewolf.domain.model.village.participant.VillageParticipant
import com.ort.firewolf.domain.model.village.vote.VillageVote
import com.ort.firewolf.domain.model.village.vote.VillageVotes
import com.ort.firewolf.domain.service.ability.AbilityDomainService
import com.ort.firewolf.domain.service.commit.CommitDomainService
import com.ort.firewolf.domain.service.participate.ParticipateDomainService
import com.ort.firewolf.domain.service.say.SayDomainService
import com.ort.firewolf.domain.service.skill.SkillRequestDomainService
import com.ort.firewolf.domain.service.vote.VoteDomainService
import com.ort.firewolf.fw.exception.FirewolfBusinessException
import com.ort.firewolf.fw.security.FirewolfUser
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
    private val reservedVillageService: ReservedVillageService,
    private val tweetService: TweetService,
    // domain service
    private val participateDomainService: ParticipateDomainService,
    private val skillRequestDomainService: SkillRequestDomainService,
    private val commitDomainService: CommitDomainService,
    private val sayDomainService: SayDomainService,
    private val abilityDomainService: AbilityDomainService,
    private val voteDomainService: VoteDomainService
) {

    /**
     * 村参加者取得
     * @param village village
     * @param user user
     * @return 村参加者
     */
    fun findParticipant(village: Village, user: FirewolfUser?): VillageParticipant? {
        user ?: return null
        val player: Player = playerService.findPlayer(user)
        return this.findParticipant(village, player.id)
    }

    fun findParticipant(village: Village, playerId: Int): VillageParticipant? {
        val participant: VillageParticipant? = village.participant.memberList.find { it.playerId == playerId && !it.isGone }
        return participant ?: village.spectator.memberList.find { it.playerId == playerId && !it.isGone }
    }

    /**
     * 村登録確認
     * @param paramVillage village
     * @param user user
     */
    fun confirmVillage(paramVillage: Village, user: FirewolfUser) {
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
    @Transactional(rollbackFor = [Exception::class, FirewolfBusinessException::class])
    fun registerVillage(paramVillage: Village, user: FirewolfUser): Int {
        // 作成できない状況ならエラー
        val player: Player = playerService.findPlayer(user)
        player.assertCreateVillage(user)
        // 村を登録
        val village: Village = registerVillage(paramVillage)

        return village.id
    }

    private fun registerVillage(paramVillage: Village): Village {
        // 村を登録
        val village: Village = villageService.registerVillage(paramVillage)
        // 村作成時のシステムメッセージを登録
        messageService.registerInitialMessage(village)
        // ダミーキャラを参加させる
        val chara: Chara = charachipService.findChara(village.setting.charachip.dummyCharaId)
        participateDummyChara(village.id, village, chara)

        return village
    }

    /**
     * 村に参加できるかチェック
     * @param villageId villageId
     * @param user user
     * @param charaId charaId
     * @param message 入村発言
     * @param isSpectate 見学か
     * @param firstRequestSkill 役職第1希望
     * @param secondRequestSkill 役職第2希望
     * @param password 入村パスワード
     */
    fun assertParticipate(
        villageId: Int,
        user: FirewolfUser,
        charaId: Int,
        message: String,
        isSpectate: Boolean,
        firstRequestSkill: CDef.Skill = CDef.Skill.おまかせ,
        secondRequestSkill: CDef.Skill = CDef.Skill.おまかせ,
        password: String?
    ) {
        // 参加できない状況ならエラー
        val village: Village = villageService.findVillage(villageId)
        val player: Player = playerService.findPlayer(user)
        val charas: Charas = charachipService.findCharas(village.setting.charachip.charachipId)

        if (isSpectate) {
            participateDomainService.assertSpectate(
                player,
                village,
                charaId,
                charas.list.size,
                password
            )
        } else {
            participateDomainService.assertParticipate(
                player,
                village,
                charaId,
                firstRequestSkill,
                secondRequestSkill,
                password
            )
        }
        // 参加発言
        val messageContent = MessageContent.invoke(
            CDef.MessageType.通常発言.code(),
            message,
            CDef.FaceType.通常.code()
        )
        val chara = charas.chara(charaId)
        sayDomainService.assertParticipateSay(village, chara, messageContent)
    }

    /**
     * 村に参加
     * @param villageId villageId
     * @param playerId playerId
     * @param charaId charaId
     * @param message 入村時発言
     * @param isSpectate 見学か
     * @param firstRequestSkill 役職第1希望
     * @param secondRequestSkill 役職第2希望
     */
    @Transactional(rollbackFor = [Exception::class, FirewolfBusinessException::class])
    fun participate(
        villageId: Int,
        playerId: Int,
        charaId: Int,
        message: String,
        isSpectate: Boolean,
        firstRequestSkill: CDef.Skill = CDef.Skill.おまかせ,
        secondRequestSkill: CDef.Skill = CDef.Skill.おまかせ
    ) {
        // 村参加者登録
        var village: Village = villageService.findVillage(villageId)
        val changedVillage: Village = village.participate(
            playerId = playerId,
            charaId = charaId,
            firstRequestSkill = firstRequestSkill,
            secondRequestSkill = secondRequestSkill,
            isSpectate = isSpectate
        )
        village = villageService.updateVillageDifference(village, changedVillage)
        val participant: VillageParticipant = findParticipant(village, playerId)!!
        val chara: Chara = charachipService.findChara(charaId)
        // {N}人目、{キャラ名} とユーザー入力の発言
        messageService.registerParticipateMessage(
            village = village,
            participant = village.participant.member(participant.id),
            chara = chara,
            message = message,
            isSpectate = isSpectate
        )
    }

    /**
     * 役職希望変更
     * @param villageId villageId
     * @param user user
     * @param firstRequestSkill 第1希望
     * @param secondRequestSkill 第2希望
     */
    fun changeSkillRequest(villageId: Int, user: FirewolfUser, firstRequestSkill: String, secondRequestSkill: String) {
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
     * 退村
     * @param villageId villageId
     * @param user user
     */
    @Transactional(rollbackFor = [Exception::class, FirewolfBusinessException::class])
    fun leave(villageId: Int, user: FirewolfUser) {
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
        val chara: Chara = charachipService.findChara(participant.charaId)
        messageService.registerLeaveMessage(updatedVillage, chara)
    }

    /**
     * 発言できるか確認
     *
     * @param villageId villageId
     * @param user user
     * @param messageText 発言内容
     * @param messageType 発言種別
     * @param faceType 表情種別
     */
    fun confirmToSay(villageId: Int, user: FirewolfUser, messageText: String, messageType: String, faceType: String) {
        val messageContent: MessageContent = MessageContent.invoke(messageType, messageText, faceType)
        // 発言できない状況ならエラー
        assertSay(villageId, user, messageContent)
    }

    /**
     * 発言
     *
     * @param villageId villageId
     * @param user user
     * @param messageText 発言内容
     * @param messageType 発言種別
     * @param faceType 表情種別
     */
    @Transactional(rollbackFor = [Exception::class, FirewolfBusinessException::class])
    fun say(villageId: Int, user: FirewolfUser, messageText: String, messageType: String, faceType: String) {
        val messageContent: MessageContent = MessageContent.invoke(messageType, messageText, faceType)
        // 発言できない状況ならエラー
        assertSay(villageId, user, messageContent)
        // 発言
        val village: Village = villageService.findVillage(villageId)
        val participant: VillageParticipant = findParticipant(village, user)!!
        val message: Message = Message.createSayMessage(participant, village.day.latestDay().id, messageContent)
        messageService.registerSayMessage(villageId, message)
    }

    /**
     * 能力セット
     *
     * @param villageId villageId
     * @param user user
     * @param targetId 対象村参加者ID
     * @param abilityTypeCode 能力種別
     */
    @Transactional(rollbackFor = [Exception::class, FirewolfBusinessException::class])
    fun setAbility(villageId: Int, user: FirewolfUser, targetId: Int?, abilityTypeCode: String) {
        // 能力セットできない状況ならエラー
        val village: Village = villageService.findVillage(villageId)
        val participant: VillageParticipant? = findParticipant(village, user)
        val abilityType = AbilityType(abilityTypeCode)
        abilityDomainService.assertAbility(village, participant, targetId, abilityType)
        // 能力セット
        val villageAbility = VillageAbility(village.day.latestDay().id, participant!!.id, targetId, abilityType)
        abilityService.updateAbility(villageAbility)
        val charas: Charas = charachipService.findCharas(village.setting.charachip.charachipId)
        messageService.registerAbilitySetMessage(village, participant, targetId, abilityType, charas)
    }

    /**
     * 投票セット
     *
     * @param villageId villageId
     * @param user user
     * @param targetId 対象村参加者ID
     */
    @Transactional(rollbackFor = [Exception::class, FirewolfBusinessException::class])
    fun setVote(villageId: Int, user: FirewolfUser, targetId: Int) {
        // 投票セットできない状況ならエラー
        val village: Village = villageService.findVillage(villageId)
        val participant: VillageParticipant? = findParticipant(village, user)
        voteDomainService.assertVote(village, participant, targetId)
        // 投票
        val villageVote = VillageVote(
            village.day.latestDay().id,
            participant!!.id,
            targetId
        )
        voteService.updateVote(villageVote)
    }

    /**
     * コミットセット
     *
     * @param villageId villageId
     * @param user user
     * @param doCommit コミットするか
     */
    @Transactional(rollbackFor = [Exception::class, FirewolfBusinessException::class])
    fun setCommit(villageId: Int, user: FirewolfUser, doCommit: Boolean) {
        // コミットできない状況ならエラー
        val village: Village = villageService.findVillage(villageId)
        val participant: VillageParticipant? = findParticipant(village, user)
        commitDomainService.assertCommit(village, participant)
        // コミット
        val commit = Commit(village.day.latestDay().id, participant!!.id, doCommit)
        commitService.updateCommit(commit)
        val chara: Chara = charachipService.findChara(participant.charaId)
        messageService.registerCommitMessage(village, chara, doCommit)
        // 日付更新
        if (doCommit) dayChangeCoordinator.dayChangeIfNeeded(village)
    }

    /**
     * 参加状況や可能なアクションを取得
     * @param village village
     * @param user user
     * @param players players
     * @param charas charas
     */
    fun findActionSituation(
        village: Village,
        user: FirewolfUser?,
        players: Players,
        charas: Charas
    ): SituationAsParticipant {
        val player: Player? = if (user == null) null else playerService.findPlayer(user)
        val participant: VillageParticipant? = findParticipant(village, user)
        val skillRequest: SkillRequest? = if (participant == null) null else village.participant.member(participant.id).skillRequest
        val abilities: VillageAbilities = abilityService.findVillageAbilities(village.id)
        val votes: VillageVotes = voteService.findVillageVotes(village.id)
        val commit: Commit? = commitService.findCommit(village, participant)
        val latestDayMessageList: List<Message> =
            messageService.findParticipateDayMessageList(village.id, village.day.latestDay(), participant)

        return SituationAsParticipant(
            participate = participateDomainService.convertToSituation(
                village, participant, player, charas
            ),
            skillRequest = skillRequestDomainService.convertToSituation(village, participant, skillRequest),
            commit = commitDomainService.convertToSituation(village, participant, commit),
            say = sayDomainService.convertToSituation(village, participant, charas, latestDayMessageList),
            ability = abilityDomainService.convertToSituationList(village, participant, abilities),
            vote = voteDomainService.convertToSituation(village, participant, votes)
        )
    }

    // ===================================================================================
    //                                                                        Assist Logic
    //                                                                        ============
    private fun participateDummyChara(villageId: Int, village: Village, dummyChara: Chara) {
        val dummyPlayerId = 1 // 固定
        val message: String = dummyChara.defaultMessage.joinMessage ?: "人狼なんているわけないじゃん。みんな大げさだなあ"
        this.participate(
            villageId = villageId,
            playerId = dummyPlayerId,
            charaId = village.setting.charachip.dummyCharaId,
            message = message,
            isSpectate = false
        )
    }

    private fun assertSay(villageId: Int, user: FirewolfUser?, messageContent: MessageContent) {
        val village: Village = villageService.findVillage(villageId)
        val participant: VillageParticipant? = findParticipant(village, user)
        val chara: Chara? = if (participant == null) null else charachipService.findChara(participant.charaId)
        val latestDayMessageList: List<Message> =
            messageService.findParticipateDayMessageList(villageId, village.day.latestDay(), participant)
        sayDomainService.assertSay(village, participant, chara, latestDayMessageList, messageContent)
    }
}