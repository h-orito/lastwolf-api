package com.ort.lastwolf.application.service

import com.ort.lastwolf.domain.model.village.Village
import com.ort.lastwolf.domain.model.village.vote.VillageVote
import com.ort.lastwolf.domain.model.village.vote.VillageVotes
import com.ort.lastwolf.infrastructure.datasource.vote.VoteDataSource
import org.springframework.stereotype.Service

@Service
class VoteService(
    val voteDataSource: VoteDataSource
) {

    fun findVillageVotes(villageId: Int): VillageVotes = voteDataSource.findVotes(villageId)

    fun updateVote(village: Village, villageVote: VillageVote) {
        voteDataSource.updateVote(village, villageVote)
    }

    fun updateDifference(village: Village, before: VillageVotes, after: VillageVotes) {
        voteDataSource.updateDifference(village, before, after)
    }
}
