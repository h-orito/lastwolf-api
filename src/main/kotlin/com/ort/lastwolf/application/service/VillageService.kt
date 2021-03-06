package com.ort.lastwolf.application.service

import com.ort.lastwolf.domain.model.village.Village
import com.ort.lastwolf.domain.model.village.VillageStatus
import com.ort.lastwolf.domain.model.village.Villages
import com.ort.lastwolf.fw.security.LastwolfUser
import com.ort.lastwolf.infrastructure.datasource.firebase.FirebaseDataSource
import com.ort.lastwolf.infrastructure.datasource.village.VillageDataSource
import org.springframework.stereotype.Service

@Service
class VillageService(
    private val villageDataSource: VillageDataSource,
    private val firebaseDataSource: FirebaseDataSource
) {

    /**
     * 村一覧取得
     * @param user 入力した場合、参加している村一覧
     * @param villageStatusList 入力した場合、そのステータスで絞り込む
     * @return Villages
     */
    fun findVillages(
        user: LastwolfUser? = null,
        villageStatusList: List<VillageStatus>? = listOf()
    ): Villages = villageDataSource.findVillages(user, villageStatusList)

    /**
     * 村一覧取得
     * @param villageIdList 村IDリスト
     * @return Villages
     */
    fun findVillages(villageIdList: List<Int>): Villages = villageDataSource.findVillages(villageIdList)

    /**
     * 村一覧（詳細）取得
     * @param villageIdList 村IDリスト
     * @return Villages
     */
    fun findVillagesAsDetail(villageIdList: List<Int>): Villages = villageDataSource.findVillagesAsDetail(villageIdList)

    /**
     * 村取得
     * @param villageId villageId
     * @return Village
     */
    fun findVillage(villageId: Int, excludeGonePlayer: Boolean = true): Village =
        villageDataSource.findVillage(villageId, excludeGonePlayer)

    /**
     * 村登録
     * @param village village
     * @return villageId
     */
    fun registerVillage(village: Village): Village = villageDataSource.registerVillage(village)

    /**
     * 差分更新
     *
     * @param before village
     * @param after village
     */
    fun updateVillageDifference(before: Village, after: Village): Village {
        val village = villageDataSource.updateDifference(before, after)
        firebaseDataSource.registerVillageLatest(before.id)
        return village
    }
}
