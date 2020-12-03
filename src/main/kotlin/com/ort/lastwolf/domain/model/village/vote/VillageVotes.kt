package com.ort.lastwolf.domain.model.village.vote

import com.ort.lastwolf.domain.model.village.Village
import com.ort.lastwolf.domain.model.village.VillageDay

data class VillageVotes(
    val list: List<VillageVote>
) {

    fun filterLatestday(village: Village): VillageVotes = filterByDay(village.days.latestDay())

    fun filterYesterday(village: Village): VillageVotes = filterByDay(village.days.yesterday())

    fun addAll(voteList: List<VillageVote>): VillageVotes {
        if (voteList.isEmpty()) return this
        return this.copy(
            list = list + voteList
        )
    }

    fun existsDifference(votes: VillageVotes): Boolean {
        return list.size != votes.list.size
    }

    private fun filterByDay(villageDay: VillageDay): VillageVotes {
        return this.copy(
            list = list.filter { it.villageDayId == villageDay.id }
        )
    }

    // 得票 key: target participant id, value: vote list
    fun toVotedMap(village: Village): Map<Int, List<VillageVote>> {
        return list
            .filter {
                it.villageDayId == village.days.yesterday().id &&
                    village.participants.first(it.myselfId).isAlive()
            }
            .groupBy { it.targetId }
    }
}