package com.ort.lastwolf.api.controller

import com.ort.dbflute.exbhv.CharaBhv
import com.ort.dbflute.exbhv.PlayerBhv
import com.ort.dbflute.exbhv.VillageDayBhv
import com.ort.dbflute.exbhv.VillageSettingBhv
import com.ort.dbflute.exentity.Player
import com.ort.dbflute.exentity.VillageSetting
import com.ort.lastwolf.api.body.AdminDummyLoginBody
import com.ort.lastwolf.api.body.AdminParticipateBody
import com.ort.lastwolf.api.view.debug.DebugVillageView
import com.ort.lastwolf.application.coordinator.VillageCoordinator
import com.ort.lastwolf.application.service.PlayerService
import com.ort.lastwolf.application.service.VillageService
import com.ort.lastwolf.application.service.VoteService
import com.ort.lastwolf.domain.model.village.Village
import com.ort.lastwolf.domain.model.village.participant.VillageParticipant
import com.ort.lastwolf.domain.model.village.vote.VillageVote
import com.ort.lastwolf.fw.LastwolfDateUtil
import com.ort.lastwolf.fw.exception.LastwolfBusinessException
import com.ort.lastwolf.fw.security.LastwolfUser
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController


/**
 * デバッグ用なのでDDDに拘らない
 */
@RestController
class DebugController(
    val charaBhv: CharaBhv,
    val playerBhv: PlayerBhv,
    val villageDayBhv: VillageDayBhv,
    val villageSettingBhv: VillageSettingBhv,

    val villageCoordinator: VillageCoordinator,

    val villageService: VillageService,
    val playerService: PlayerService,
    val voteService: VoteService
) {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    @Value("\${env: null}")
    private var env: String? = null

    // ===================================================================================
    //                                                                             Execute
    //                                                                           =========
    /**
     * 人数指定で参加させる
     * @param villageId villageId
     * @param user user
     * @param body body
     */
    @PostMapping("/admin/village/{villageId}/participate")
    fun participateVillage(
        @PathVariable("villageId") villageId: Int,
        @AuthenticationPrincipal user: LastwolfUser,
        @RequestBody @Validated body: AdminParticipateBody
    ) {
        if ("local" != env) throw LastwolfBusinessException("この環境では使用できません")

        val village = villageService.findVillage(villageId)

        // 参戦していないキャラを人数分探す
        val charaList = charaBhv.selectList { cb ->
            cb.query().setCharaGroupId_Equal(village.setting.charachip.charachipId)
            cb.query().notExistsVillagePlayer { villagePlayerCB ->
                villagePlayerCB.query().setVillageId_Equal(villageId)
                villagePlayerCB.query().setIsGone_Equal(false)
            }
            cb.fetchFirst(body.participateCount!!)
        }
        var playerId = 2
        for (chara in charaList) {
            // 希望役職をランダムに取得
            val randomSkill = village.setting.organizations.allRequestableSkillList().shuffled().first()
            val randomSkill2 = village.setting.organizations.allRequestableSkillList().shuffled().first()
            // 入村
            villageCoordinator.participate(
                villageId = villageId,
                playerId = playerId,
                charaId = chara.charaId,
                firstRequestSkill = randomSkill.toCdef(),
                secondRequestSkill = randomSkill2.toCdef()
            )
            playerId++
        }
    }

    /**
     * 指定村参加者IDでログインする
     * @param villageId villageId
     * @param user user
     * @param body body
     */
    @PostMapping("/admin/village/{villageId}/dummy-login")
    fun dummyLogin(
        @PathVariable("villageId") villageId: Int,
        @AuthenticationPrincipal user: LastwolfUser,
        @RequestBody @Validated body: AdminDummyLoginBody
    ) {
        if ("local" != env) throw LastwolfBusinessException("この環境では使用できません")

        // 現在接続しているユーザのuidと、指定されたプレイヤーのuidを入れ替える
        val currentPlayer = playerBhv.selectEntityWithDeletedCheck {
            it.query().setUid_Equal(user.uid)
        }
        val toPlayer = playerBhv.selectEntityWithDeletedCheck {
            it.query().setPlayerId_Equal(body.targetId!!)
        }
        val current = currentPlayer.uid
        val to = toPlayer.uid
        updatePlayerUid(currentPlayer.playerId, "dummy_uid")
        updatePlayerUid(toPlayer.playerId, current)
        updatePlayerUid(currentPlayer.playerId, to)
    }

    /**
     * 最新日の残り時間を10秒にする
     * @param villageId villageId
     * @param user user
     */
    @PostMapping("/admin/village/{villageId}/change-day")
    fun changeDay(
        @PathVariable("villageId") villageId: Int,
        @AuthenticationPrincipal user: LastwolfUser
    ) {
        if ("local" != env) throw LastwolfBusinessException("この環境では使用できません")

        val latestDay = villageDayBhv.selectEntityWithDeletedCheck {
            it.query().setVillageId_Equal(villageId)
            it.query().addOrderBy_Day_Desc()
            it.query().queryNoonnight().addOrderBy_DispOrder_Desc()
            it.fetchFirst(1)
        }
        latestDay.endDatetime = LastwolfDateUtil.currentLocalDateTime().plusSeconds(10L)
        villageDayBhv.update(latestDay)
    }

    /**
     * 村情報取得（役職やプレーヤーが全て見える状態）
     * @param villageId villageId
     */
    @GetMapping("/admin/village/{villageId}")
    fun village(@PathVariable("villageId") villageId: Int): DebugVillageView {
        if ("local" != env) throw LastwolfBusinessException("この環境では使用できません")

        val village: Village = villageService.findVillage(villageId)
        val createPlayer: com.ort.lastwolf.domain.model.player.Player = playerService.findPlayer(village.creatorPlayer.id)
        return DebugVillageView(
            village = village,
            createPlayer = createPlayer
        )
    }

    /**
     * 突然死なしにする
     * @param villageId villageId
     * @param user user
     */
    @PostMapping("/admin/village/{villageId}/no-suddenly-death")
    fun setNoSuddenlyDeath(
        @PathVariable("villageId") villageId: Int,
        @AuthenticationPrincipal user: LastwolfUser
    ) {
        if ("local" != env) throw LastwolfBusinessException("この環境では使用できません")

        val setting = VillageSetting()
        setting.villageId = villageId
        setting.setVillageSettingItemCode_突然死ありか()
        setting.villageSettingText = "0"
        villageSettingBhv.update(setting)
    }

    @PostMapping("/admin/village/{villageId}/multiple-say")
    fun multiSay(
        @PathVariable("villageId") villageId: Int,
        @AuthenticationPrincipal user: LastwolfUser
    ) {
        if ("local" != env) throw LastwolfBusinessException("この環境では使用できません")

        repeat(100) {
            villageCoordinator.say(villageId, user, "${it}回目の発言", "NORMAL_SAY", false)
        }
    }

    @PostMapping("/admin/village/{villageId}/all-rollcall")
    fun allRollcall(
        @PathVariable("villageId") villageId: Int,
        @AuthenticationPrincipal user: LastwolfUser
    ) {
        if ("local" != env) throw LastwolfBusinessException("この環境では使用できません")
        val village = villageService.findVillage(villageId)
        val changedVillage = village.copy(
            participants = village.participants.copy(
                list = village.participants.list.map { it.rollCall(true) }
            )
        )
        villageService.updateVillageDifference(village, changedVillage)
    }

    /**
     * 全員1票投票
     *
     * @param villageId villageId
     * @param user user
     */
    @PostMapping("/admin/village/{villageId}/all-draw-vote")
    fun allDrawVote(
        @PathVariable("villageId") villageId: Int,
        @AuthenticationPrincipal user: LastwolfUser
    ) {
        if ("local" != env) throw LastwolfBusinessException("この環境では使用できません")
        val village = villageService.findVillage(villageId)
        val participants = village.participants.filterAlive()
        participants.list.forEachIndexed { index, participant ->
            if (participant.id == participants.list.last().id) {
                val villageVote = VillageVote(
                    village.days.latestDay(),
                    participant.id,
                    participants.list.first().id
                )
                voteService.updateVote(village, villageVote)
            } else {
                val villageVote = VillageVote(
                    village.days.latestDay(),
                    participant.id,
                    participants.list[index + 1].id
                )
                voteService.updateVote(village, villageVote)
            }
        }
    }

    /**
     * 全員ランダム投票
     *
     * @param villageId villageId
     * @param user user
     */
    @PostMapping("/admin/village/{villageId}/all-random-vote")
    fun allRandomVote(
        @PathVariable("villageId") villageId: Int,
        @AuthenticationPrincipal user: LastwolfUser
    ) {
        if ("local" != env) throw LastwolfBusinessException("この環境では使用できません")
        val village = villageService.findVillage(villageId)
        val participants = village.participants.filterAlive()
        participants.list.forEach { participant ->
            val villageVote = VillageVote(
                village.days.latestDay(),
                participant.id,
                participants.list.filterNot { p -> p.id == participant.id }.random().id
            )
            voteService.updateVote(village, villageVote)
        }
    }

    /**
     * 全員投票
     *
     * @param villageId villageId
     * @param user user
     */
    @PostMapping("/admin/village/{villageId}/all-vote/{participantId}")
    fun allRandomVote(
        @PathVariable("villageId") villageId: Int,
        @PathVariable("participantId") participantId: Int,
        @AuthenticationPrincipal user: LastwolfUser
    ) {
        if ("local" != env) throw LastwolfBusinessException("この環境では使用できません")
        val village = villageService.findVillage(villageId)
        val participants = village.participants.filterAlive()
        participants.list.forEach { participant ->
            val target: VillageParticipant = if (participant.id == participantId) {
                participants.list.first { it.id != participantId }
            } else participants.list.first { it.id == participantId }
            val villageVote = VillageVote(
                village.days.latestDay(),
                participant.id,
                target.id
            )
            voteService.updateVote(village, villageVote)
        }
    }

    // ===================================================================================
    //                                                                        Assist Logic
    //                                                                        ============
    private fun updatePlayerUid(playerId: Int?, uid: String?) {
        val p = Player()
        p.playerId = playerId
        p.uid = uid
        playerBhv.update(p)
    }
}