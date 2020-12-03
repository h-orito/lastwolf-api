package com.ort.lastwolf.infrastructure.datasource.village

import com.ort.dbflute.allcommon.CDef
import com.ort.dbflute.exbhv.VillageBhv
import com.ort.dbflute.exbhv.VillageDayBhv
import com.ort.dbflute.exbhv.VillagePlayerBhv
import com.ort.dbflute.exbhv.VillageSettingBhv
import com.ort.dbflute.exentity.Village
import com.ort.dbflute.exentity.VillageDay
import com.ort.dbflute.exentity.VillagePlayer
import com.ort.dbflute.exentity.VillageSetting
import com.ort.lastwolf.domain.model.noonnight.NoonNight
import com.ort.lastwolf.domain.model.village.Villages
import com.ort.lastwolf.domain.model.village.participant.VillageParticipant
import com.ort.lastwolf.domain.model.village.setting.VillageSettings
import com.ort.lastwolf.fw.LastwolfDateUtil
import com.ort.lastwolf.fw.security.LastwolfUser
import com.ort.lastwolf.infrastructure.datasource.village.converter.VillageDataConverter
import org.springframework.stereotype.Repository

@Repository
class VillageDataSource(
    val villageBhv: VillageBhv,
    val villageSettingBhv: VillageSettingBhv,
    val villageDayBhv: VillageDayBhv,
    val villagePlayerBhv: VillagePlayerBhv
) {

    /**
     * 村登録
     * @param paramVillage village
     * @return 村ID
     */
    fun registerVillage(
        paramVillage: com.ort.lastwolf.domain.model.village.Village
    ): com.ort.lastwolf.domain.model.village.Village {
        // 村
        val villageId = insertVillage(paramVillage)
        // 村設定
        insertVillageSettings(villageId, paramVillage.setting)
        // 村日付
        insertVillageDay(
            villageId,
            com.ort.lastwolf.domain.model.village.VillageDay(
                id = 1, // dummy
                day = 1,
                noonNight = NoonNight(CDef.Noonnight.昼),
                isEpilogue = false,
                startDatetime = LastwolfDateUtil.currentLocalDateTime(),
                endDatetime = paramVillage.setting.time.startDatetime
            )
        )
        return findVillage(villageId)
    }

    /**
     * 村一覧取得
     * @param user 指定した場合は自分が参加した村一覧
     * @param villageStatusList 指定した場合はそのステータスで絞り込む
     * @return 村一覧
     */
    fun findVillages(
        user: LastwolfUser? = null,
        villageStatusList: List<com.ort.lastwolf.domain.model.village.VillageStatus>? = listOf()
    ): Villages {
        val villageList = villageBhv.selectList {
            it.setupSelect_Player()

            it.specify().derivedVillagePlayer().count({ vpCB ->
                vpCB.specify().columnVillagePlayerId()
                vpCB.query().setIsGone_Equal(false)
            }, Village.ALIAS_participantCount)

            if (user != null) {
                it.query().existsVillagePlayer { vpCB ->
                    vpCB.query().setIsGone_Equal(false)
                    vpCB.query().queryPlayer().setUid_Equal(user.uid)
                }
            }
            if (villageStatusList != null && villageStatusList.isNotEmpty()) {
                it.query().setVillageStatusCode_InScope_AsVillageStatus(
                    villageStatusList.map { status -> status.toCdef() }
                )
            }

            it.query().addOrderBy_VillageId_Desc()
        }
        villageBhv.load(villageList) { loader ->
            loader.loadVillageSetting { }
            loader.loadVillageDay {
                it.query().addOrderBy_Day_Desc()
                it.query().queryNoonnight().addOrderBy_DispOrder_Desc()
            }
        }
        return VillageDataConverter.convertVillageListToVillages(villageList)
    }

    /**
     * 村一覧取得
     * @param villageIdList 村IDリスト
     * @return 村一覧
     */
    fun findVillages(villageIdList: List<Int>): Villages {
        if (villageIdList.isEmpty()) return Villages(listOf())
        val villageList = villageBhv.selectList {
            it.setupSelect_Player()

            it.specify().derivedVillagePlayer().count({ vpCB ->
                vpCB.specify().columnVillagePlayerId()
                vpCB.query().setIsGone_Equal(false)
            }, Village.ALIAS_participantCount)

            it.query().setVillageId_InScope(villageIdList)
            it.query().addOrderBy_VillageId_Desc()
        }
        villageBhv.load(villageList) { loader ->
            loader.loadVillageSetting { }
            loader.loadVillageDay {
                it.query().addOrderBy_Day_Desc()
                it.query().queryNoonnight().addOrderBy_DispOrder_Desc()
            }
        }

        return VillageDataConverter.convertVillageListToVillages(villageList)
    }

    /**
     * 村一覧取得（詳細）
     * @param villageIdList 村IDリスト
     * @return 村一覧（詳細）
     */
    fun findVillagesAsDetail(villageIdList: List<Int>): Villages {
        if (villageIdList.isEmpty()) return Villages(listOf())
        val villageList = villageBhv.selectList {
            it.setupSelect_Player()

            it.specify().derivedVillagePlayer().count({ vpCB ->
                vpCB.specify().columnVillagePlayerId()
                vpCB.query().setIsGone_Equal(false)
            }, Village.ALIAS_participantCount)

            it.query().setVillageId_InScope(villageIdList)
            it.query().addOrderBy_VillageId_Desc()
        }
        villageBhv.load(villageList) { loader ->
            loader.loadVillagePlayer { vpCB ->
                vpCB.setupSelect_Player()
                vpCB.setupSelect_Chara()
                vpCB.setupSelect_VillageDay()
                vpCB.query().setIsGone_Equal(false)
            }
            loader.loadVillageSetting { }
            loader.loadVillageDay {
                it.query().addOrderBy_Day_Asc()
                it.query().queryNoonnight().addOrderBy_DispOrder_Asc()
            }
        }

        return Villages(villageList.map { VillageDataConverter.convertVillageToVillage(it) })
    }

    /**
     * 村情報取得
     * @param villageId villageId
     * @return 村情報
     */
    fun findVillage(villageId: Int, excludeGonePlayer: Boolean = true): com.ort.lastwolf.domain.model.village.Village {
        val village = villageBhv.selectEntityWithDeletedCheck {
            it.setupSelect_Player()
            it.query().setVillageId_Equal(villageId)
        }
        villageBhv.load(village) { loader ->
            loader.loadVillagePlayer { vpCB ->
                vpCB.setupSelect_Player()
                vpCB.setupSelect_Chara()
                vpCB.setupSelect_VillageDay()
                if (excludeGonePlayer) {
                    vpCB.query().setIsGone_Equal(false)
                }
                vpCB.query().addOrderBy_VillagePlayerId_Asc()
            }.withNestedReferrer {
                it.loadComingOut { }
            }
            loader.loadVillageSetting { }
            loader.loadVillageDay {
                it.query().addOrderBy_Day_Asc()
                it.query().queryNoonnight().addOrderBy_DispOrder_Asc()
            }
        }

        return VillageDataConverter.convertVillageToVillage(village)
    }


    /**
     * 差分更新
     * @param before village
     * @param after village
     */
    fun updateDifference(
        before: com.ort.lastwolf.domain.model.village.Village,
        after: com.ort.lastwolf.domain.model.village.Village
    ): com.ort.lastwolf.domain.model.village.Village {
        // village
        updateVillageDifference(before, after)
        // village_day
        updateVillageDayDifference(before, after)
        // village_player
        updateVillagePlayerDifference(before, after)
        // village_setting
        updateVillageSettingDifference(before, after)

        return findVillage(before.id)
    }

    // ===================================================================================
    //                                                                              Update
    //                                                                              ======
    private fun updateVillageDifference(
        before: com.ort.lastwolf.domain.model.village.Village,
        after: com.ort.lastwolf.domain.model.village.Village
    ) {
        if (before.status.code != after.status.code
            || before.winCamp?.code != after.winCamp?.code
            || before.name != after.name
        ) {
            updateVillage(after)
        }
    }

    private fun updateVillageDayDifference(
        before: com.ort.lastwolf.domain.model.village.Village,
        after: com.ort.lastwolf.domain.model.village.Village
    ) {
        if (!before.days.existsDifference(after.days)) return
        after.days.list
            .filterNot { afterDay ->
                before.days.list.any {
                    it.day == afterDay.day && it.noonNight.code == afterDay.noonNight.code
                }
            }
            .forEach {
                insertVillageDay(after.id, it)
            }
        after.days.list
            .filter { afterDay ->
                before.days.list.any { it.id == afterDay.id }
            }.forEach { afterDay ->
                val beforeDay = before.days.list.first { it.id == afterDay.id }
                if (afterDay.existsDifference(beforeDay)) updateVillageDay(afterDay)
            }
    }

    private fun updateVillagePlayerDifference(
        before: com.ort.lastwolf.domain.model.village.Village,
        after: com.ort.lastwolf.domain.model.village.Village
    ) {
        val villageId = after.id
        if (!before.participants.existsDifference(after.participants)) return
        // 新規
        after.participants.list.filterNot { member ->
            before.participants.list.any { it.id == member.id }
        }.forEach {
            insertVillagePlayer(villageId, it)
        }

        // 更新
        after.participants.list.filter { member ->
            before.participants.list.any { it.id == member.id }
        }.forEach {
            val beforeMember = before.participants.first(it.id)
            if (it.existsDifference(beforeMember)) updateVillagePlayer(villageId, it)
        }
    }

    private fun updateVillageSettingDifference(
        before: com.ort.lastwolf.domain.model.village.Village,
        after: com.ort.lastwolf.domain.model.village.Village
    ) {
        val villageId = after.id
        if (!before.setting.existsDifference(after.setting)) return

        after.setting.capacity.let(fun(afterCapacity) {
            if (!before.setting.capacity.existsDifference(afterCapacity)) return
            updateVillageSetting(villageId, CDef.VillageSettingItem.最低人数, afterCapacity.min.toString())
            updateVillageSetting(villageId, CDef.VillageSettingItem.最大人数, afterCapacity.max.toString())
        })
        after.setting.time.let(fun(afterTime) {
            if (!before.setting.time.existsDifference(afterTime)) return
            updateVillageSetting(
                villageId,
                CDef.VillageSettingItem.開始予定日時,
                afterTime.startDatetime.format(VillageDataConverter.DATETIME_FORMATTER)
            )
            updateVillageSetting(villageId, CDef.VillageSettingItem.昼時間秒, afterTime.noonSeconds.toString())
            updateVillageSetting(villageId, CDef.VillageSettingItem.投票時間秒, afterTime.voteSeconds.toString())
            updateVillageSetting(villageId, CDef.VillageSettingItem.夜時間秒, afterTime.nightSeconds.toString())
        })
        after.setting.organizations.let(fun(afterOrg) {
            if (!before.setting.organizations.existsDifference(afterOrg)) return
            updateVillageSetting(villageId, CDef.VillageSettingItem.構成, afterOrg.toString())
        })
        after.setting.rules.let(fun(afterRules) {
            if (!before.setting.rules.existsDifference(afterRules)) return
            updateVillageSetting(villageId, CDef.VillageSettingItem.役職希望可能か, toFlg(afterRules.availableSkillRequest))
            updateVillageSetting(villageId, CDef.VillageSettingItem.墓下役職公開ありか, toFlg(afterRules.openSkillInGrave))
            updateVillageSetting(villageId, CDef.VillageSettingItem.突然死ありか, toFlg(afterRules.availableSuddenlyDeath))
            updateVillageSetting(villageId, CDef.VillageSettingItem.コミット可能か, toFlg(afterRules.availableCommit))
            updateVillageSetting(villageId, CDef.VillageSettingItem.役欠けありか, toFlg(afterRules.availableDummySkill))
        })
        after.setting.password.let(fun(afterPassword) {
            if (!before.setting.password.existsDifference(afterPassword)) return
            updateVillageSetting(villageId, CDef.VillageSettingItem.入村パスワード, afterPassword.joinPassword ?: "")
        })
    }

    // ===================================================================================
    //                                                                             village
    //                                                                        ============
    /**
     * 村登録
     * @param villageModel 村
     * @return villageId
     */
    private fun insertVillage(villageModel: com.ort.lastwolf.domain.model.village.Village): Int {
        val village = Village()
        village.villageDisplayName = villageModel.name
        village.villageStatusCodeAsVillageStatus = CDef.VillageStatus.codeOf(villageModel.status.code)
        village.createPlayerId = villageModel.creatorPlayer.id
        villageBhv.insert(village)
        return village.villageId
    }

    /**
     * 村更新
     * @param villageModel 村
     */
    private fun updateVillage(villageModel: com.ort.lastwolf.domain.model.village.Village) {
        val village = Village()
        village.villageId = villageModel.id
        village.villageDisplayName = villageModel.name
        village.villageStatusCodeAsVillageStatus = villageModel.status.toCdef()
        village.winCampCodeAsCamp = villageModel.winCamp?.toCdef()
        villageBhv.update(village)
    }

    // ===================================================================================
    //                                                                      village_player
    //                                                                        ============
    /**
     * 村参加者登録
     * @param villageId villageId
     * @param participant participant
     * @return 村参加ID
     */
    private fun insertVillagePlayer(
        villageId: Int,
        participant: VillageParticipant
    ): Int {
        val vp = VillagePlayer()
        vp.villageId = villageId
        vp.playerId = participant.player.id
        vp.charaId = participant.chara.id
        vp.isDead = false
        vp.isGone = false
        vp.requestSkillCodeAsSkill = participant.skillRequest.first.toCdef()
        vp.secondRequestSkillCodeAsSkill = participant.skillRequest.second.toCdef()
        vp.doneRollcall = participant.doneRollCall
        villagePlayerBhv.insert(vp)
        return vp.villagePlayerId
    }

    private fun updateVillagePlayer(
        villageId: Int,
        villagePlayerModel: VillageParticipant
    ) {
        val villagePlayer = VillagePlayer()
        villagePlayer.villageId = villageId
        villagePlayer.villagePlayerId = villagePlayerModel.id
        villagePlayer.isDead = villagePlayerModel.dead != null
        villagePlayer.deadReasonCodeAsDeadReason = villagePlayerModel.dead?.toCdef()
        villagePlayer.deadVillageDayId = villagePlayerModel.dead?.villageDay?.id
        villagePlayer.isGone = villagePlayerModel.isGone
        villagePlayer.skillCodeAsSkill = villagePlayerModel.skill?.toCdef()
        villagePlayer.requestSkillCodeAsSkill = villagePlayerModel.skillRequest.first.toCdef()
        villagePlayer.secondRequestSkillCodeAsSkill = villagePlayerModel.skillRequest.second.toCdef()
        villagePlayer.doneRollcall = villagePlayerModel.doneRollCall
        villagePlayer.winloseCodeAsWinLose = villagePlayerModel.winlose?.toCdef()
        villagePlayerBhv.update(villagePlayer)
    }

    // ===================================================================================
    //                                                                         village_day
    //                                                                        ============
    /**
     * 村日付登録
     * @param villageId villageId
     * @param day 村日付
     * @return 村日付id
     */
    private fun insertVillageDay(
        villageId: Int,
        day: com.ort.lastwolf.domain.model.village.VillageDay
    ): com.ort.lastwolf.domain.model.village.VillageDay {
        val villageDay = VillageDay()
        villageDay.villageId = villageId
        villageDay.day = day.day
        villageDay.noonnightCodeAsNoonnight = day.noonNight.toCdef()
        villageDay.isEpilogue = day.isEpilogue
        villageDay.startDatetime = day.startDatetime
        villageDay.endDatetime = day.endDatetime
        villageDayBhv.insert(villageDay)
        return VillageDataConverter.convertVillageDayToVillageDay(villageDay)
    }

    private fun updateVillageDay(
        day: com.ort.lastwolf.domain.model.village.VillageDay
    ) {
        val villageDay = VillageDay()
        villageDay.villageDayId = day.id
        villageDay.isEpilogue = day.isEpilogue
        villageDay.startDatetime = day.startDatetime
        villageDay.endDatetime = day.endDatetime
        villageDayBhv.update(villageDay)
    }

    // ===================================================================================
    //                                                                     village_setting
    //                                                                        ============
    /**
     * 村設定登録
     * @param villageId villageId
     * @param settings model settings
     */
    private fun insertVillageSettings(villageId: Int, settings: VillageSettings) {
        insertVillageSetting(villageId, CDef.VillageSettingItem.最低人数, settings.capacity.min.toString())
        insertVillageSetting(villageId, CDef.VillageSettingItem.最大人数, settings.capacity.max.toString())
        insertVillageSetting(
            villageId,
            CDef.VillageSettingItem.開始予定日時,
            settings.time.startDatetime.format(VillageDataConverter.DATETIME_FORMATTER)
        )
        insertVillageSetting(villageId, CDef.VillageSettingItem.昼時間秒, settings.time.noonSeconds.toString())
        insertVillageSetting(villageId, CDef.VillageSettingItem.投票時間秒, settings.time.voteSeconds.toString())
        insertVillageSetting(villageId, CDef.VillageSettingItem.夜時間秒, settings.time.nightSeconds.toString())
        insertVillageSetting(villageId, CDef.VillageSettingItem.ダミーキャラid, settings.charachip.dummyCharaId.toString())
        insertVillageSetting(villageId, CDef.VillageSettingItem.キャラクターグループid, settings.charachip.charachipId.toString())
        insertVillageSetting(villageId, CDef.VillageSettingItem.構成, settings.organizations.toString())
        insertVillageSetting(villageId, CDef.VillageSettingItem.役職希望可能か, toFlg(settings.rules.availableSkillRequest))
        insertVillageSetting(villageId, CDef.VillageSettingItem.墓下役職公開ありか, toFlg(settings.rules.openSkillInGrave))
        insertVillageSetting(villageId, CDef.VillageSettingItem.突然死ありか, toFlg(settings.rules.availableSuddenlyDeath))
        insertVillageSetting(villageId, CDef.VillageSettingItem.コミット可能か, toFlg(settings.rules.availableCommit))
        insertVillageSetting(villageId, CDef.VillageSettingItem.入村パスワード, settings.password.joinPassword ?: "")
        insertVillageSetting(villageId, CDef.VillageSettingItem.役欠けありか, toFlg(settings.rules.availableDummySkill))
    }

    private fun insertVillageSetting(villageId: Int, item: CDef.VillageSettingItem, value: String) {
        val setting = VillageSetting()
        setting.villageId = villageId
        setting.villageSettingItemCodeAsVillageSettingItem = item
        setting.villageSettingText = value
        villageSettingBhv.insert(setting)
    }

    private fun updateVillageSetting(villageId: Int, item: CDef.VillageSettingItem, value: String) {
        val setting = VillageSetting()
        setting.villageSettingText = value
        villageSettingBhv.queryUpdate(setting) {
            it.query().setVillageId_Equal(villageId)
            it.query().setVillageSettingItemCode_Equal_AsVillageSettingItem(item)
        }
    }

    // ===================================================================================
    //                                                                        Assist Logic
    //                                                                        ============
    private fun toFlg(bool: Boolean): String {
        return if (bool) VillageDataConverter.FLG_TRUE else VillageDataConverter.FLG_FALSE
    }
}