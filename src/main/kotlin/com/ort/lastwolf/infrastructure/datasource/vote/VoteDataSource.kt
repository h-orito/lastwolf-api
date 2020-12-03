package com.ort.lastwolf.infrastructure.datasource.vote

import com.ort.dbflute.exbhv.VoteBhv
import com.ort.dbflute.exentity.Vote
import com.ort.lastwolf.domain.model.village.Village
import com.ort.lastwolf.domain.model.village.vote.VillageVote
import com.ort.lastwolf.domain.model.village.vote.VillageVotes
import com.ort.lastwolf.infrastructure.datasource.firebase.FirebaseDataSource
import org.springframework.stereotype.Repository

@Repository
class VoteDataSource(
    val voteBhv: VoteBhv,
    val firebaseDataSource: FirebaseDataSource
) {

    // ===================================================================================
    //                                                                              Select
    //                                                                              ======
    fun findVotes(villageId: Int): VillageVotes {
        val voteList = voteBhv.selectList {
            it.query().queryVillageDay().setVillageId_Equal(villageId)
        }
        return VillageVotes(voteList.map { convertToVoteToVillageVote(it) })
    }

    // ===================================================================================
    //                                                                              Update
    //                                                                              ======
    fun updateVote(village: Village, villageVote: VillageVote) {
        deleteVote(villageVote)
        insertVote(villageVote)
        firebaseDataSource.registerSituationLatest(village, villageVote)
    }

    private fun deleteVote(villageVote: VillageVote) {
        voteBhv.queryDelete {
            it.query().setVillageDayId_Equal(villageVote.villageDayId)
            it.query().setVillagePlayerId_Equal(villageVote.myselfId)
        }
    }

    private fun insertVote(villageVote: VillageVote) {
        val vote = Vote()
        vote.villageDayId = villageVote.villageDayId
        vote.villagePlayerId = villageVote.myselfId
        vote.targetVillagePlayerId = villageVote.targetId
        voteBhv.insert(vote)
    }

    fun updateDifference(village: Village, before: VillageVotes, after: VillageVotes) {
        // 削除
        before.list.filterNot { beforeVote ->
            after.list.any { afterVote ->
                beforeVote.villageDayId == afterVote.villageDayId
                    && beforeVote.myselfId == afterVote.myselfId
            }
        }.forEach { deleteVote(it) }
        // 更新
        after.list.filter { afterVote ->
            before.list.any { beforeVote ->
                beforeVote.villageDayId == afterVote.villageDayId
                    && beforeVote.myselfId == afterVote.myselfId
            }
        }.forEach { updateVote(village, it) }
        // 追加
        after.list.filterNot { afterVote ->
            before.list.any { beforeVote ->
                beforeVote.villageDayId == afterVote.villageDayId
                    && beforeVote.myselfId == afterVote.myselfId
            }
        }.forEach { insertVote(it) }
    }

    // ===================================================================================
    //                                                                        Assist Logic
    //                                                                        ============
    private fun convertToVoteToVillageVote(vote: Vote): VillageVote {
        return VillageVote(
            villageDayId = vote.villageDayId,
            myselfId = vote.villagePlayerId,
            targetId = vote.targetVillagePlayerId
        )
    }
}