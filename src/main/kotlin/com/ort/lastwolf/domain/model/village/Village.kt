package com.ort.lastwolf.domain.model.village

import com.ort.dbflute.allcommon.CDef
import com.ort.lastwolf.domain.model.camp.Camp
import com.ort.lastwolf.domain.model.message.Message
import com.ort.lastwolf.domain.model.player.Player
import com.ort.lastwolf.domain.model.skill.Skill
import com.ort.lastwolf.domain.model.skill.SkillRequest
import com.ort.lastwolf.domain.model.skill.Skills
import com.ort.lastwolf.domain.model.village.participant.VillageParticipant
import com.ort.lastwolf.domain.model.village.participant.VillageParticipants
import com.ort.lastwolf.domain.model.village.setting.VillageSettings
import com.ort.lastwolf.fw.LastwolfDateUtil
import com.ort.lastwolf.fw.exception.LastwolfBusinessException

data class Village(
    val id: Int,
    val name: String,
    val creatorPlayer: Player,
    val status: VillageStatus,
    val winCamp: Camp?,
    val setting: VillageSettings,
    val participants: VillageParticipants,
    val days: VillageDays
) {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    private val initialMessage: String = "村が作成されました。"

    private val firstNightMessage: String =
        "村が開始されました。\n能力者は進行欄で能力を行使してください。\n行使せずに朝を迎えた場合、突然死してしまいます。"

    private val creatorCancelMessage: String = "村建ての操作により廃村しました。"

    private val extendPrologueMessage: String = "村人が揃っていないため、開始予定時刻を1時間繰り下げました。"

    private val extendRollCallMessage: String = "点呼が揃っていないため、開始予定時刻を10分繰り下げました。"

    // ===================================================================================
    //                                                                             message
    //                                                                           =========
    /** 村作成時のメッセージ */
    fun createVillagePrologueMessage(): Message =
        Message.createPublicSystemMessage(initialMessage, days.latestDay().id)

    /** 1日目のメッセージ */
    fun createVillageDay1Message(): Message =
        Message.createPublicSystemMessage(firstNightMessage, days.latestDay().id)

    /** 村建て廃村メッセージ */
    fun createCreatorCancelVillageMessage(): Message =
        Message.createPublicSystemMessage(creatorCancelMessage, days.latestDay().id, true)

    /** 構成メッセージ */
    fun createOrganizationMessage(): Message {
        val skillCountMap = setting.organizations.mapToSkillCount(participants.count)
        val text = CDef.Skill.listAll().sortedBy { Integer.parseInt(it.order()) }.mapNotNull { cdefSkill ->
            val skill = Skill(cdefSkill)
            val count = skillCountMap[cdefSkill]
            if (count == null || count == 0) null else "${skill.name}が${count}人"
        }.joinToString(
            separator = "、\n",
            prefix = "この村には\n",
            postfix = "\nいるようだ。"
        )
        return Message.createPublicSystemMessage(text, days.latestDay().id)
    }

    /** 人狼系の役職相互確認メッセージ */
    fun createWolfsConfirmMessage(): Message {
        val text = createRecognizeSkillMessageText(Skills.wolfs.list)
        return Message.createAttackPrivateMessage(text, days.latestDay().id)
    }

    /** 共有の役職相互確認メッセージ */
    fun createMasonsConfirmMessage(): Message? {
        // 共有がいなければなし
        if (participants.list.none { it.skill!!.toCdef().isRecognizableEachMason }) return null
        // 共有が存在する
        val text = createRecognizeSkillMessageText(Skills.masons.list)
        return Message.createMasonPrivateMessage(text, days.latestDay().id)
    }

    /** 狂信者の役職確認メッセージ */
    fun createFanaticConfirmMessage(): Message? {
        // 狂信者がいなければなし
        if (participants.list.none { it.skill!!.canRecognizeWolf() }) return null
        // 襲撃役職を一括りにして人狼とする
        val text = participants.list.filter { it.skill!!.hasAttackAbility() }.joinToString(
            separator = "、",
            prefix = "この村の人狼は",
            postfix = "のようだ。"
        ) {
            it.chara.name.fullName()
        }
        return Message.createFanaticPrivateMessage(text, days.latestDay().id)
    }

    /** 妖狐の役職相互確認メッセージ */
    fun createFoxsConfirmMessage(): Message? {
        // 妖狐がいなければなし
        if (participants.list.none { it.skill!!.toCdef().isRecognizableEachFox }) return null
        // 妖狐が存在する
        val text = createRecognizeSkillMessageText(Skills.foxs.list)
        return Message.createFoxPrivateMessage(text, days.latestDay().id)
    }

    private fun createRecognizeSkillMessageText(skills: List<Skill>): String {
        return skills.mapNotNull { skill ->
            val list = participants.filterBySkill(skill).list
            if (list.isEmpty()) null
            else "${skill.name}は${list.joinToString(separator = "、") { it.chara.name.fullName() }}"
        }.joinToString(
            separator = "、\n",
            prefix = "この村の",
            postfix = "のようだ。"
        )
    }

    fun createExtendPrologueMessage(): Message =
        Message.createPublicSystemMessage(extendPrologueMessage, days.latestDay().id)

    fun createExtendRollCallMessage(): Message =
        Message.createPublicSystemMessage(extendRollCallMessage, days.latestDay().id)

    // ===================================================================================
    //                                                                                read
    //                                                                           =========
    fun dummyParticipant(): VillageParticipant? =
        participants.list.firstOrNull { it.chara.id == setting.charachip.dummyCharaId }

    fun notDummyParticipants(): VillageParticipants {
        val notDummyMembers = participants.list.filter { it.chara.id != setting.charachip.dummyCharaId }
        return VillageParticipants(
            count = notDummyMembers.size,
            list = notDummyMembers
        )
    }

    fun todayDeadParticipants(): VillageParticipants {
        val deadTodayMemberList = participants.list.filter {
            !it.isAlive() && it.dead?.villageDay?.id == days.latestDay().id
        }
        return VillageParticipants(
            count = deadTodayMemberList.size,
            list = deadTodayMemberList
        )
    }

    // 差分があるか
    fun existsDifference(village: Village): Boolean {
        return status.code != village.status.code
                || winCamp?.code != village.winCamp?.code
                || participants.existsDifference(village.participants)
                || days.existsDifference(village.days)
                || setting.existsDifference(village.setting)
    }

    // 決着がついたか
    fun isSettled(): Boolean {
        val wolfCount = wolfCount()
        return wolfCount <= 0 || villagerCount() <= wolfCount
    }

    // ゲームマスター制で、ゲームマスターか
    fun isGameMaster(player: Player?): Boolean {
        return setting.rules.creatorGameMaster && creatorPlayer.id == player?.id
    }

    // ===================================================================================
    //                                                                                 権限
    //                                                                           =========
    /** 村として参加可能か */
    fun isAvailableParticipate(player: Player): Boolean {
        // プロローグでない
        if (!status.isRecruiting()) return false
        // 既に最大人数まで参加している
        if (participants.count >= setting.capacity.max) return false
        // GM制で参加しようとしているのがGM
        if (setting.rules.creatorGameMaster && creatorPlayer.id == player.id) return false

        return true
    }

    /**
     * 村としての参加チェック
     * @param charaId charaId
     * @param password 入村パスワード
     */
    fun assertParticipate(
        charaId: Int,
        password: String?
    ) {
        // 既に参加しているキャラはNG
        if (isAlreadyParticipateCharacter(charaId)) throw LastwolfBusinessException("既に参加されているキャラクターです")
        // パスワードが合っているかチェック
        assertPassword(password)
    }

    /** 村として退村可能か */
    fun isAvailableLeave(): Boolean = status.isRecruiting()

    /** 村として役職希望可能か */
    fun isAvailableSkillRequest(): Boolean {
        // プロローグでない
        if (!status.isRecruiting()) return false
        // 役職希望設定
        return setting.rules.availableSkillRequest
    }

    fun isAvailableRollCall(): Boolean = status.isRollCalling()

    fun isAvailableStart(): Boolean {
        return status.isRollCalling()
                && participants.list.count { it.doneRollCall } >= participants.count - 1
    }

    /**
     * 役職希望変更チェック
     * @param first 第1役職希望
     * @param second 第2役職希望
     */
    fun assertSkillRequest(first: CDef.Skill, second: CDef.Skill) {
        if (setting.organizations.allRequestableSkillList()
                .none { it.code == first.code() }
        ) throw LastwolfBusinessException("役職希望変更できません")
        if (setting.organizations.allRequestableSkillList()
                .none { it.code == second.code() }
        ) throw LastwolfBusinessException("役職希望変更できません")
    }

    /** 村としてコミットできるか */
    fun isAvailableCommit(): Boolean {
        // コミットできない設定ならNG
        if (!setting.rules.availableCommit) return false
        // 進行中以外はNG
        if (!status.isProgress()) return false
        // 投票中はNG
        if (days.latestDay().isVoteTime()) return false
        return true
    }

    fun isAvailableComingOut(): Boolean = status.isProgress()

    /** 村の状況として発言できるか */
    fun isAvailableSay(): Boolean = !status.toCdef().isFinishedVillage // 終了していたら不可

    /** 村として通常発言できるか */
    fun isSayableNormalSay(): Boolean {
        // エピローグはOK
        if (days.latestDay().isEpilogue) return true
        // 昼以外は不可
        if (!days.latestDay().isNoonTime()) return false
        // 終了していたら不可
        return !status.toCdef().isFinishedVillage
    }

    /** 村として囁き発言を見られるか */
    fun isViewableWerewolfSay(): Boolean = status.isSolved()

    /** 村として囁き発言できるか */
    fun isSayableWerewolfSay(): Boolean {
        // 夜以外は不可
        if (!days.latestDay().isNightTime()) return false
        // 進行中以外は不可
        return status.isProgress()
    }

    /** 村として共有発言を見られるか */
    fun isViewableMasonSay(): Boolean = status.isSolved()

    /** 村として共有発言できるか */
    fun isSayableMasonSay(): Boolean {
        // 夜以外は不可
        if (!days.latestDay().isNightTime()) return false
        // 進行中以外は不可
        return status.isProgress()
    }

    /** 村として墓下発言を見られるか */
    fun isViewableGraveSay(): Boolean = status.isSolved()

    /** 村として墓下発言できるか */
    fun isSayableGraveSay(): Boolean {
        // 夜以外は不可
        if (!days.latestDay().isNightTime()) return false
        // 進行中以外は不可
        return status.isProgress()
    }

    /** 村として独り言を見られるか */
    fun isViewableMonologueSay(): Boolean = status.isSolved() // 終了していたら全て見られる

    /** 村として独り言発言できるか */
    fun isSayableMonologueSay(): Boolean {
        // 夜以外は不可
        if (!days.latestDay().isNightTime()) return false
        // 進行中以外は不可
        return status.isProgress()
    }

    /** 村として襲撃メッセージを見られるか */
    fun isViewableAttackMessage(): Boolean = status.isSolved() // 終了していたら全て見られる

    /** 村として共有メッセージを見られるか */
    fun isViewableMasonMessage(): Boolean = status.isSolved() // 終了していたら全て見られる

    /** 村として妖狐メッセージを見られるか */
    fun isViewableFoxMessage(): Boolean = status.isSolved() // 終了していたら全て見られる

    /** 村として狂信者メッセージを見られるか */
    fun isViewableFanaticMessage(): Boolean = status.isSolved()

    /** 村として白黒霊能結果を見られるか */
    fun isViewablePsychicMessage(): Boolean = status.isSolved()// 終了していたら全て見られる

    /** 村として能力を行使できるか */
    fun canUseAbility(): Boolean = status.isProgress()

    /** 村として投票できるか */
    fun isAvailableVote(): Boolean {
        if (!status.isProgress()) return false
        if (days.latestDay().day <= 1) return false
        return days.latestDay().noonNight.isVoteTime()
    }

    // ===================================================================================
    //                                                                              update
    //                                                                        ============
    // 最新日の更新日時を今にし、新たに村日付を追加
    fun addNewDay(toNextVote: Boolean = false, isEpilogue: Boolean = false): Village {
        // 最新日の更新日時を今にする
        val latestDay = days.latestDay()
        val now = LastwolfDateUtil.currentLocalDateTime()
        val dayList = days.list.map {
            if (latestDay.id == it.id) it.copy(endDatetime = now)
            else it
        }
        // 新たな村日付
        var newDay = latestDay.createNextDay(toNextVote, isEpilogue)
        val intervalSeconds = setting.time.getIntervalSeconds(newDay.noonNight)
        newDay = newDay.copy(endDatetime = now.plusSeconds(intervalSeconds.toLong()))
        return this.copy(
            days = VillageDays(
                list = dayList + newDay
            )
        )
    }

    // 入村
    fun participate(
        playerId: Int,
        charaId: Int,
        firstRequestSkill: CDef.Skill = CDef.Skill.おまかせ,
        secondRequestSkill: CDef.Skill = CDef.Skill.おまかせ
    ): Village =
        this.copy(
            participants = participants.addParticipant(
                charaId = charaId,
                playerId = playerId,
                skillRequest = SkillRequest(Skill(firstRequestSkill), Skill(secondRequestSkill))
            )
        )

    // 希望役職変更
    fun changeSkillRequest(participantId: Int, first: CDef.Skill, second: CDef.Skill): Village =
        this.copy(participants = participants.changeSkillRequest(participantId, first, second))

    fun rollCall(participantId: Int, done: Boolean): Village =
        this.copy(participants = participants.rollCall(participantId, done))

    // 全員おまかせに変更
    fun changeAllSkillRequestLeftover(): Village =
        this.copy(
            participants = participants.copy(
                list = participants.list.map { it.changeSkillRequest(CDef.Skill.おまかせ, CDef.Skill.おまかせ) }
            )
        )

    // 退村
    fun leaveParticipant(participantId: Int): Village =
        this.copy(participants = this.participants.leave(participantId))

    // 突然死
    fun suddenlyDeathParticipant(participantId: Int): Village =
        this.copy(participants = this.participants.suddenlyDeath(participantId, days.latestDay()))

    // 処刑
    fun executeParticipant(participantId: Int): Village =
        this.copy(participants = this.participants.execute(participantId, days.latestDay()))

    // 襲撃
    fun attackParticipant(participantId: Int): Village =
        this.copy(participants = this.participants.attack(participantId, days.latestDay()))

    // 呪殺
    fun divineKillParticipant(participantId: Int): Village =
        this.copy(participants = this.participants.divineKill(participantId, days.latestDay()))

    // 後追い
    fun suicideParticipant(participantId: Int): Village =
        this.copy(participants = this.participants.suicide(participantId, days.latestDay()))

    // 役職割り当て
    fun assignSkill(participants: VillageParticipants): Village {
        return this.copy(participants = participants)
    }

    // ステータス変更
    fun changeStatus(cdefVillageStatus: CDef.VillageStatus): Village =
        this.copy(status = VillageStatus(cdefVillageStatus))

    private fun judgeWinCamp(): Camp? {
        if (!this.isSettled()) return null
        if (isFoxAlive()) return Camp(CDef.Camp.妖狐陣営)
        if (wolfCount() > 0) return Camp(CDef.Camp.人狼陣営)
        return Camp(CDef.Camp.村人陣営)
    }

    // 点呼開始
    fun startRollCall(): Village = this.changeStatus(CDef.VillageStatus.点呼中)

    // 点呼中止
    fun cancelRollCall(): Village {
        return this
            .changeStatus(CDef.VillageStatus.募集中)
            .copy(
                participants = participants.copy(
                    list = participants.list.map { it.rollCall(false) }
                )
            )
    }

    // エピローグ遷移
    fun toEpilogue(isDraw: Boolean = false): Village {
        return this
            .changeStatus(CDef.VillageStatus.決着) // エピローグへ
            .winLose(isDraw, judgeWinCamp()) // 勝敗陣営設定
            .latestDayToEpilogue() // エピローグは固定で24時間にするので、最新日を差し替える
    }

    // 最新の日を24時間にする
    fun latestDayToEpilogue(): Village = this.copy(days = this.days.toLatestDayEpilogue())

    fun extendPrologue(): Village = this.copy(
        setting = setting.extendPrologue(),
        days = days.extendPrologue()
    )

    fun extendRollCall(): Village = this.copy(
        setting = setting.extendRollCall(),
        days = days.extendRollCall()
    )

    // ===================================================================================
    //                                                                        Assist Logic
    //                                                                        ============
    private fun isAlreadyParticipateCharacter(charaId: Int): Boolean {
        return participants.list.any { it.chara.id == charaId }
    }

    private fun assertPassword(password: String?) {
        if (!setting.password.joinPasswordRequired) return
        if (setting.password.joinPassword != password) throw LastwolfBusinessException("入村パスワードが誤っています")
    }

    private fun villagerCount(): Int =
        participants.filterAlive().list.count { !it.skill!!.toCdef().isCountWolf && !it.skill.toCdef().isNoCount }

    private fun wolfCount(): Int = participants.filterAlive().list.count { it.skill!!.toCdef().isCountWolf }

    private fun isFoxAlive(): Boolean = participants.filterAlive().list.any { it.skill!!.toCdef().isNoCount }

    // 勝利陣営設定
    private fun winLose(isDraw: Boolean, winCamp: Camp?): Village {
        if (isDraw) {
            return this.copy(
                participants = this.participants.winLose(null)
            )
        }
        if (!isSettled()) return this
        return this.copy(
            winCamp = winCamp, // 村自体の勝利陣営
            participants = this.participants.winLose(winCamp) // 個人ごとの勝敗
        )
    }

    // ===================================================================================
    //                                                                    companion object
    //                                                                             =======
    companion object {

        fun createForRegister(
            resource: VillageCreateResource
        ): Village {
            return Village(
                id = 1, // dummy
                name = resource.villageName,
                creatorPlayer = Player(
                    id = resource.createPlayerId,
                    uid = "dummy",
                    nickname = "dummy",
                    twitterUserName = "dummy",
                    isRestrictedParticipation = false
                ),
                status = VillageStatus(CDef.VillageStatus.募集中),
                setting = VillageSettings.createForRegister(
                    resource.setting
                ),
                participants = VillageParticipants(
                    count = 0,
                    list = listOf()
                ),
                days = VillageDays(
                    list = listOf()
                ),
                winCamp = null
            )
        }

        fun createForUpdate(
            village: Village,
            resource: VillageCreateResource
        ): Village {
            return Village(
                id = village.id,
                name = resource.villageName,
                creatorPlayer = village.creatorPlayer,
                status = village.status,
                setting = VillageSettings.createForRegister(
                    resource.setting
                ),
                participants = village.participants,
                days = village.days.copy(
                    list = village.days.list.map {
                        if (it.day == 1 && it.isNoonTime()) it.copy(endDatetime = resource.setting.time.startDatetime)
                        else it
                    }
                ),
                winCamp = null
            )
        }
    }
}
