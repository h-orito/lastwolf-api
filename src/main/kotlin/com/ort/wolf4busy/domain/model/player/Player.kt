package com.ort.wolf4busy.domain.model.player

import com.ort.dbflute.allcommon.CDef
import com.ort.wolf4busy.fw.exception.Wolf4busyBusinessException
import com.ort.wolf4busy.fw.security.Wolf4busyUser

data class Player(
    val id: Int,
    val nickname: String,
    val twitterUserName: String,
    val isRestrictedParticipation: Boolean,
    val participateProgressVillageIdList: List<Int> = listOf(),
    val participateFinishedVillageIdList: List<Int> = listOf(),
    val createProgressVillageIdList: List<Int> = listOf(),
    val createFinishedVillageIdList: List<Int> = listOf()
) {
    fun restrictParticipation(): Player {
        return this.copy(isRestrictedParticipation = true)
    }

    /**
     * 村作成チェック
     * @param user user
     */
    fun assertCreateVillage(user: Wolf4busyUser?) {
        if (!isAvailableCreateVillage(user)) throw Wolf4busyBusinessException("村を作成できません")
    }

    /**
     * @return 村に参戦可能か
     */
    fun isAvailableParticipate(): Boolean {
        if (isParticipatingProgressVillage()) return false
        if (isRestrictedParticipation) return false
        return true
    }

    /**
     * @param user user
     * @return 村を作成可能か
     */
    fun isAvailableCreateVillage(user: Wolf4busyUser?): Boolean {
        user ?: return false
        if (user.authority == CDef.Authority.管理者) return true
        if (isParticipatingProgressVillage()) return false
        if (isRestrictedParticipation) return false
        if (isProgressCreateVillage()) return false
        return true
    }

    // ===================================================================================
    //                                                                        Assist Logic
    //                                                                        ============
    // 進行中の村に参加しているか
    private fun isParticipatingProgressVillage(): Boolean = participateProgressVillageIdList.isNotEmpty()

    // 作成した村の勝敗が決していないか
    private fun isProgressCreateVillage(): Boolean = createProgressVillageIdList.isNotEmpty()
}