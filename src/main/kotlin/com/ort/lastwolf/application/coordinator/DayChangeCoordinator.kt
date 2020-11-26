package com.ort.lastwolf.application.coordinator

import com.ort.lastwolf.application.service.AbilityService
import com.ort.lastwolf.application.service.CommitService
import com.ort.lastwolf.application.service.MessageService
import com.ort.lastwolf.application.service.PlayerService
import com.ort.lastwolf.application.service.VillageService
import com.ort.lastwolf.application.service.VoteService
import com.ort.lastwolf.domain.model.commit.Commits
import com.ort.lastwolf.domain.model.daychange.DayChange
import com.ort.lastwolf.domain.model.player.Players
import com.ort.lastwolf.domain.model.village.ability.VillageAbilities
import com.ort.lastwolf.domain.model.village.vote.VillageVotes
import com.ort.lastwolf.domain.service.daychange.DayChangeDomainService
import com.ort.lastwolf.fw.exception.LastwolfBusinessException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DayChangeCoordinator(
    val villageService: VillageService,
    val voteService: VoteService,
    val abilityService: AbilityService,
    val commitService: CommitService,
    val messageService: MessageService,
    val playerService: PlayerService,
    // domain service
    val dayChangeDomainService: DayChangeDomainService
) {

    // 手動開始
    @Transactional(rollbackFor = [Exception::class, LastwolfBusinessException::class])
    fun startVillage(villageId: Int) {
        val village = villageService.findVillage(villageId)
        val votes: VillageVotes = voteService.findVillageVotes(village.id)
        val abilities: VillageAbilities = abilityService.findVillageAbilities(village.id)
        val players: Players = playerService.findPlayers(village.id)

        val beforeDayChange = DayChange(
            village = village,
            votes = votes,
            abilities = abilities,
            players = players
        )

        var dayChange = beforeDayChange.copy(
            village = village.addNewDay()
        ).setIsChange(beforeDayChange)

        update(beforeDayChange, dayChange)

        // 登録後の村日付idが必要になるので取得し直す
        dayChange = dayChange.copy(village = villageService.findVillage(village.id))

        // 日付更新
        dayChangeDomainService.process(dayChange).also {
            updateIfNeeded(dayChange, it)
        }
    }

    /**
     * 必要あれば日付更新
     *
     * @param village village
     */
    @Transactional(rollbackFor = [Exception::class, LastwolfBusinessException::class])
    fun dayChangeIfNeeded(villageId: Int) {
        val village = villageService.findVillage(villageId)
        val votes: VillageVotes = voteService.findVillageVotes(village.id)
        val abilities: VillageAbilities = abilityService.findVillageAbilities(village.id)
        val commits: Commits = commitService.findCommits(village.id)
        val players: Players = playerService.findPlayers(village.id)

        val beforeDayChange = DayChange(
            village = village,
            votes = votes,
            abilities = abilities,
            players = players
        )

        // プロローグ/点呼延長処理
        var dayChange = updateIfNeeded(
            beforeDayChange,
            dayChangeDomainService.extendVillageIfNeeded(beforeDayChange)
        )

        // 必要あれば次の日へ
        dayChange = dayChangeDomainService.addDayIfNeeded(dayChange, commits).let {
            if (!it.isChange) return
            updateIfNeeded(dayChange, it)
        }

        // 登録後の村日付idが必要になるので取得し直す
        dayChange = dayChange.copy(village = villageService.findVillage(village.id))

        // 日付更新
        dayChangeDomainService.process(dayChange).also {
            updateIfNeeded(dayChange, it)
        }
    }

    // ===================================================================================
    //                                                                        Assist Logic
    //                                                                        ============
    private fun updateIfNeeded(before: DayChange, after: DayChange): DayChange {
        if (!after.isChange) return after
        update(before, after)
        return after.copy(isChange = false)
    }

    private fun update(before: DayChange, after: DayChange) {
        // player
        if (before.players.existsDifference(after.players)) {
            playerService.updateDifference(before.players, after.players)
        }
        // village
        if (before.village.existsDifference(after.village)) {
            villageService.updateVillageDifference(before.village, after.village)
        }
        // message
        if (before.messages.existsDifference(after.messages)) {
            messageService.updateDifference(before, after)
        }
        // votes
        if (before.votes.existsDifference(after.votes)) {
            voteService.updateDifference(after.village, before.votes, after.votes)
        }
        // abilities
        if (before.abilities.existsDifference(after.abilities)) {
            abilityService.updateDifference(after.village, before.abilities, after.abilities)
        }
    }
}