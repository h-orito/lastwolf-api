package com.ort.lastwolf.application.service

import com.ort.lastwolf.domain.model.commit.Commit
import com.ort.lastwolf.domain.model.commit.Commits
import com.ort.lastwolf.domain.model.village.Village
import com.ort.lastwolf.domain.model.village.participant.VillageParticipant
import com.ort.lastwolf.infrastructure.datasource.commit.CommitDataSource
import org.springframework.stereotype.Service

@Service
class CommitService(
    val commitDataSource: CommitDataSource
) {
    fun findCommits(villageId: Int): Commits = commitDataSource.findCommits(villageId)

    fun findCommit(village: Village, participant: VillageParticipant?): Commit? {
        participant ?: return null
        return commitDataSource.findCommit(village, participant)
    }

    /**
     * コミット/取り消し
     * @param village village
     * @param commit commit
     */
    fun updateCommit(village: Village, commit: Commit) = commitDataSource.updateCommit(village, commit)
}