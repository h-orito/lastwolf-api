package com.ort.lastwolf.infrastructure.datasource.commit

import com.ort.dbflute.exbhv.CommitBhv
import com.ort.dbflute.exentity.Commit
import com.ort.lastwolf.domain.model.commit.Commits
import com.ort.lastwolf.domain.model.village.participant.VillageParticipant
import org.springframework.stereotype.Repository

@Repository
class CommitDataSource(
    val commitBhv: CommitBhv
) {

    // ===================================================================================
    //                                                                              Select
    //                                                                              ======
    /**
     * コミットを取得
     * @param village village
     * @param participant 村参加情報
     * @return コミット
     */
    fun findCommit(
        village: com.ort.lastwolf.domain.model.village.Village,
        participant: VillageParticipant
    ): com.ort.lastwolf.domain.model.commit.Commit? {
        val latestDay = village.days.latestDay()

        val optCommit = commitBhv.selectEntity {
            it.query().setVillageDayId_Equal(latestDay.id)
            it.query().setVillagePlayerId_Equal(participant.id)
        }
        return optCommit.map { c ->
            com.ort.lastwolf.domain.model.commit.Commit(
                villageDayId = c.villageDayId,
                myselfId = c.villagePlayerId,
                isCommitting = true
            )
        }.orElse(null)
    }

    fun findCommits(villageId: Int): Commits {
        val commitList = commitBhv.selectList {
            it.query().queryVillageDay().setVillageId_Equal(villageId)
        }
        return Commits(
            list = commitList.map { c ->
                com.ort.lastwolf.domain.model.commit.Commit(
                    villageDayId = c.villageDayId,
                    myselfId = c.villagePlayerId,
                    isCommitting = true
                )
            }
        )
    }

    // ===================================================================================
    //                                                                              Update
    //                                                                              ======
    /**
     * コミット/取り消し
     *
     * @param commit commit
     */
    fun updateCommit(commit: com.ort.lastwolf.domain.model.commit.Commit) {
        deleteCommit(commit)
        if (commit.isCommitting) insertCommit(commit)
    }

    private fun deleteCommit(commit: com.ort.lastwolf.domain.model.commit.Commit) {
        commitBhv.queryDelete {
            it.query().setVillageDayId_Equal(commit.villageDayId)
            it.query().setVillagePlayerId_Equal(commit.myselfId)
        }
    }

    private fun insertCommit(c: com.ort.lastwolf.domain.model.commit.Commit) {
        val commit = Commit()
        commit.villageDayId = c.villageDayId
        commit.villagePlayerId = c.myselfId
        commitBhv.insert(commit)
    }
}
