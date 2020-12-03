package com.ort.lastwolf.domain.model.village.participant

import com.ort.dbflute.allcommon.CDef
import com.ort.lastwolf.domain.model.camp.Camp
import com.ort.lastwolf.domain.model.charachip.Chara
import com.ort.lastwolf.domain.model.charachip.CharaImage
import com.ort.lastwolf.domain.model.charachip.CharaName
import com.ort.lastwolf.domain.model.dead.Dead
import com.ort.lastwolf.domain.model.player.Player
import com.ort.lastwolf.domain.model.skill.Skill
import com.ort.lastwolf.domain.model.skill.SkillRequest
import com.ort.lastwolf.domain.model.village.VillageDay
import com.ort.lastwolf.domain.model.village.participant.coming_out.ComingOut
import com.ort.lastwolf.domain.model.winlose.WinLose

data class VillageParticipant(
    val id: Int,
    val chara: Chara, // 本来IDで持つべきだが簡略化
    val player: Player,
    val dead: Dead?,
    val isGone: Boolean,
    val skill: Skill?,
    val skillRequest: SkillRequest,
    val winlose: WinLose?,
    val comingOut: ComingOut?,
    val doneRollCall: Boolean
) {
    // ===================================================================================
    //                                                                                read
    //                                                                           =========
    // 生存しているか
    fun isAlive(): Boolean = dead == null

    // 差分有無
    fun existsDifference(participant: VillageParticipant): Boolean {
        if (id != participant.id) return true
        if (chara.id != participant.chara.id) return true
        if (player.id != participant.player.id) return true
        if (dead?.code != participant.dead?.code) return true
        if (isGone != participant.isGone) return true
        if (skill?.code != participant.skill?.code) return true
        if (skillRequest.first.code != participant.skillRequest.first.code) return true
        if (skillRequest.second.code != participant.skillRequest.second.code) return true
        if (doneRollCall != participant.doneRollCall) return true
        if (winlose?.code != participant.winlose?.code) return true
        return false
    }

    // ===================================================================================
    //                                                                              update
    //                                                                           =========
    // 退村
    fun gone(): VillageParticipant = this.copy(isGone = true)

    // 突然死
    fun suddenlyDeath(villageDay: VillageDay): VillageParticipant = this.copy(dead = Dead(CDef.DeadReason.突然, villageDay))

    // 処刑
    fun execute(villageDay: VillageDay): VillageParticipant = this.copy(dead = Dead(CDef.DeadReason.処刑, villageDay))

    // 襲撃
    fun attack(villageDay: VillageDay): VillageParticipant = this.copy(dead = Dead(CDef.DeadReason.襲撃, villageDay))

    // 呪殺
    fun divineKill(villageDay: VillageDay): VillageParticipant = this.copy(dead = Dead(CDef.DeadReason.呪殺, villageDay))

    // 役職割り当て
    fun assignSkill(skill: Skill): VillageParticipant = this.copy(skill = skill)

    // 希望役職
    fun changeSkillRequest(first: CDef.Skill, second: CDef.Skill): VillageParticipant =
        this.copy(skillRequest = SkillRequest(Skill(first), Skill(second)))

    // 勝敗
    fun winLose(winCamp: Camp?): VillageParticipant {
        if (dead?.toCdef() == CDef.DeadReason.突然) return this.copy(winlose = WinLose(CDef.WinLose.敗北))
        winCamp ?: return this.copy(winlose = WinLose(CDef.WinLose.引分))
        val isWin = skill?.toCdef()?.campCode() == winCamp.code
        return this.copy(winlose = if (isWin) WinLose(CDef.WinLose.勝利) else WinLose(CDef.WinLose.敗北))
    }

    // 点呼
    fun rollCall(done: Boolean): VillageParticipant = this.copy(doneRollCall = done)

    companion object {
        fun createForRegister(
            charaId: Int,
            playerId: Int,
            skillRequest: SkillRequest
        ): VillageParticipant = VillageParticipant(
            id = -1, // dummy
            chara = Chara(
                id = charaId,
                name = CharaName("", ""), // dummy
                charachipId = 0, // dummy
                image = CharaImage(0, 0, "") // dummy
            ),
            player = Player(
                id = playerId,
                nickname = "",
                uid = "",
                twitterUserName = "",
                isRestrictedParticipation = false
            ),
            dead = null,
            isGone = false,
            skill = null,
            skillRequest = skillRequest,
            winlose = null,
            comingOut = null,
            doneRollCall = false
        )
    }

    // ===================================================================================
    //                                                                                権限
    //                                                                        ============
    // 役職希望可能か
    fun isAvailableSkillRequest(): Boolean = true

    // コミット可能か
    fun isAvailableCommit(dummyParticipantId: Int): Boolean {
        // 参加していなかったり死亡していたらNG
        if (!isAlive()) return false
        // ダミーはコミットできない
        if (id == dummyParticipantId) return false

        return true
    }

    fun isAvailableComingOut(): Boolean = isAlive()

    // 発言可能か
    fun isAvailableSay(isEpilogue: Boolean): Boolean {
        // 突然死した場合はエピローグ以外NG
        if (dead?.toCdef() == CDef.DeadReason.突然 && !isEpilogue) return false
        return true
    }

    fun isSayableNormalSay(isEpilogue: Boolean): Boolean {
        // ダミーはOK
        if (player.id == 1) return true
        // エピローグ以外で死亡している場合は不可
        if (!isAlive() && !isEpilogue) return false

        return true
    }

    fun isViewableWerewolfSay(): Boolean {
        return skill?.toCdef()?.isViewableWerewolfSay ?: false
    }

    fun isSayableWerewolfSay(): Boolean {
        // ダミーはOK
        if (player.id == 1) return true
        // 死亡していたら不可
        if (!isAlive()) return false
        // 囁ける役職でなければ不可
        return skill?.toCdef()?.isAvailableWerewolfSay ?: false
    }

    fun isViewableMasonSay(): Boolean {
        return skill?.toCdef()?.isViewableMasonSay ?: false
    }

    fun isSayableMasonSay(): Boolean {
        // ダミーはOK
        if (player.id == 1) return true
        // 死亡していたら不可
        if (!isAlive()) return false
        // 共有発言できる役職でなければ不可
        return skill?.toCdef()?.isAvailableMasonSay ?: false
    }

    fun isViewableGraveSay(): Boolean {
        // 突然死以外で死亡している
        return !isAlive() && CDef.DeadReason.突然.code() != dead?.code
    }

    fun isSayableGraveSay(): Boolean {
        // ダミーはOK
        if (player.id == 1) return true
        // 死亡していなかったら不可
        if (isAlive()) return false
        // 突然死は不可
        if (dead?.toCdef() == CDef.DeadReason.突然) return false
        return true
    }

    fun isSayableMonologueSay(): Boolean = true // 制約なし

    fun isViewableAttackMessage(): Boolean {
        // 生存していて囁き可能なら開放
        if (!isAlive()) return false
        return skill?.toCdef()?.isAvailableWerewolfSay ?: false
    }

    fun isViewableMasonMessage(): Boolean {
        // 生存していて共有職なら開放
        if (!isAlive()) return false
        return skill?.toCdef()?.isRecognizableEachMason ?: false
    }

    fun isViewablePsychicMessage(): Boolean {
        // 生存していて霊能者なら開放
        if (!isAlive()) return false
        return skill?.toCdef()?.isHasPsychicAbility ?: false
    }

    // 能力行使可能か
    fun canUseAbility(): Boolean = true

    // 投票可能か
    fun isAvailableVote(): Boolean = isAlive()

    fun isAvailableRollCall(): Boolean = true
}