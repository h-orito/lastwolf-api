package com.ort.lastwolf.infrastructure.datasource.village.converter

import com.ort.dbflute.allcommon.CDef
import com.ort.dbflute.exentity.Chara
import com.ort.dbflute.exentity.Player
import com.ort.dbflute.exentity.Village
import com.ort.dbflute.exentity.VillageDay
import com.ort.dbflute.exentity.VillagePlayer
import com.ort.dbflute.exentity.VillageSetting
import com.ort.lastwolf.domain.model.charachip.CharaImage
import com.ort.lastwolf.domain.model.charachip.CharaName
import com.ort.lastwolf.domain.model.dead.Dead
import com.ort.lastwolf.domain.model.noonnight.NoonNight
import com.ort.lastwolf.domain.model.skill.Skill
import com.ort.lastwolf.domain.model.skill.SkillRequest
import com.ort.lastwolf.domain.model.village.VillageDays
import com.ort.lastwolf.domain.model.village.VillageStatus
import com.ort.lastwolf.domain.model.village.Villages
import com.ort.lastwolf.domain.model.village.participant.VillageParticipant
import com.ort.lastwolf.domain.model.village.participant.VillageParticipants
import com.ort.lastwolf.domain.model.village.participant.coming_out.ComingOut
import com.ort.lastwolf.domain.model.village.setting.PersonCapacity
import com.ort.lastwolf.domain.model.village.setting.VillageCharachip
import com.ort.lastwolf.domain.model.village.setting.VillageOrganizations
import com.ort.lastwolf.domain.model.village.setting.VillagePassword
import com.ort.lastwolf.domain.model.village.setting.VillageRules
import com.ort.lastwolf.domain.model.village.setting.VillageSettings
import com.ort.lastwolf.domain.model.village.setting.VillageTime
import com.ort.lastwolf.domain.model.winlose.WinLose
import org.dbflute.cbean.result.ListResultBean
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object VillageDataConverter {

    const val FLG_TRUE = "1"
    const val FLG_FALSE = "0"
    val DATETIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss")

    fun convertVillageListToVillages(villageList: ListResultBean<Village>): Villages {
        return Villages(
            list = villageList.map { convertVillageToSimpleVillage(it) }
        )
    }

    fun convertVillageToVillage(village: Village): com.ort.lastwolf.domain.model.village.Village {
        val participantList = village.villagePlayerList
        return com.ort.lastwolf.domain.model.village.Village(
            id = village.villageId,
            name = village.villageDisplayName,
            creatorPlayer = convertPlayerToSimplePlayer(village.player.get()),
            status = VillageStatus(village.villageStatusCodeAsVillageStatus),
            setting = convertVillageSettingListToVillageSetting(village),
            participants = VillageParticipants(
                count = participantList.size,
                list = participantList.map { convertVillagePlayerToParticipant(it) }
            ),
            days = VillageDays(
                list = village.villageDayList.map { convertVillageDayToVillageDay(it) }
            ),
            winCamp = village.winCampCodeAsCamp?.let { com.ort.lastwolf.domain.model.camp.Camp(it) }
        )
    }

    fun convertVillageDayToVillageDay(villageDay: VillageDay): com.ort.lastwolf.domain.model.village.VillageDay {
        val day: Int = villageDay.day
        return com.ort.lastwolf.domain.model.village.VillageDay(
            id = villageDay.villageDayId,
            day = day,
            noonNight = NoonNight(villageDay.noonnightCodeAsNoonnight),
            isEpilogue = villageDay.isEpilogue,
            startDatetime = villageDay.startDatetime,
            endDatetime = villageDay.endDatetime
        )
    }

    // ===================================================================================
    //                                                                        Assist Logic
    //                                                                        ============
    private fun convertVillageToSimpleVillage(village: Village): com.ort.lastwolf.domain.model.village.Village {
        return com.ort.lastwolf.domain.model.village.Village(
            id = village.villageId,
            name = village.villageDisplayName,
            creatorPlayer = convertPlayerToSimplePlayer(village.player.get()),
            status = VillageStatus(village.villageStatusCodeAsVillageStatus),
            setting = convertVillageSettingListToVillageSetting(village),
            participants = VillageParticipants(count = village.participantCount),
            days = VillageDays( // 最新の1日だけ
                list = village.villageDayList.firstOrNull()?.let {
                    listOf(convertVillageDayToVillageDay(it))
                }.orEmpty()
            ),
            winCamp = village.winCampCodeAsCamp?.let { com.ort.lastwolf.domain.model.camp.Camp(it) }
        )
    }

    private fun convertVillageSettingListToVillageSetting(
        village: Village
    ): VillageSettings {
        val settingList = village.villageSettingList
        return VillageSettings(
            capacity = PersonCapacity.invoke(
                min = detectItemText(settingList, CDef.VillageSettingItem.最低人数)?.toInt(),
                max = detectItemText(settingList, CDef.VillageSettingItem.最大人数)?.toInt()
            ),
            time = VillageTime.invoke(
                createDatetime = village.registerDatetime,
                startDatetime = detectItemText(settingList, CDef.VillageSettingItem.開始予定日時)?.let {
                    LocalDateTime.parse(
                        it,
                        DATETIME_FORMATTER
                    )
                },
                noonSeconds = detectItemText(settingList, CDef.VillageSettingItem.昼時間秒)?.toInt(),
                voteSeconds = detectItemText(settingList, CDef.VillageSettingItem.投票時間秒)?.toInt(),
                nightSeconds = detectItemText(settingList, CDef.VillageSettingItem.夜時間秒)?.toInt()
            ),
            charachip = VillageCharachip.invoke(
                dummyCharaId = detectItemText(settingList, CDef.VillageSettingItem.ダミーキャラid)?.toInt(),
                charachipId = detectItemText(settingList, CDef.VillageSettingItem.キャラクターグループid)?.toInt()
            ),
            organizations = VillageOrganizations.invoke(detectItemText(settingList, CDef.VillageSettingItem.構成)),
            rules = VillageRules.invoke(
                availableSkillRequest = detectItemText(settingList, CDef.VillageSettingItem.役職希望可能か)?.let { it == FLG_TRUE },
                openSkillInGrave = detectItemText(settingList, CDef.VillageSettingItem.墓下役職公開ありか)?.let { it == FLG_TRUE },
                availableSuddenlyDeath = detectItemText(settingList, CDef.VillageSettingItem.突然死ありか)?.let { it == FLG_TRUE },
                availableCommit = detectItemText(settingList, CDef.VillageSettingItem.コミット可能か)?.let { it == FLG_TRUE },
                availableDummySkill = detectItemText(settingList, CDef.VillageSettingItem.役欠けありか)?.let { it == FLG_TRUE }
            ),
            password = VillagePassword(
                joinPassword = detectItemText(settingList, CDef.VillageSettingItem.入村パスワード)
            )
        )
    }

    private fun convertVillagePlayerToParticipant(vp: VillagePlayer): VillageParticipant {
        return VillageParticipant(
            id = vp.villagePlayerId,
            chara = vp.chara.map { convertToChara(it) }.orElse(null),
            player = vp.player.map { convertPlayerToSimplePlayer(it) }.orElse(null),
            dead = if (vp.isDead) convertToDeadReasonToDead(vp) else null,
            isGone = vp.isGone,
            skill = if (vp.skillCodeAsSkill == null) null else Skill(vp.skillCodeAsSkill),
            skillRequest = SkillRequest(
                first = Skill(vp.requestSkillCodeAsSkill),
                second = Skill(vp.secondRequestSkillCodeAsSkill)
            ),
            winlose = vp.winloseCodeAsWinLose?.let { WinLose(it) },
            comingOut = vp.comingOutList.firstOrNull()?.let { ComingOut(Skill(it.skillCodeAsSkill)) },
            doneRollCall = vp.doneRollcall
        )
    }

    private fun convertPlayerToSimplePlayer(player: Player): com.ort.lastwolf.domain.model.player.Player {
        return com.ort.lastwolf.domain.model.player.Player(
            id = player.playerId,
            uid = player.uid,
            nickname = player.nickname,
            twitterUserName = player.twitterUserName,
            isRestrictedParticipation = player.isRestrictedParticipation
        )
    }

    private fun convertToChara(chara: Chara): com.ort.lastwolf.domain.model.charachip.Chara {
        return com.ort.lastwolf.domain.model.charachip.Chara(
            id = chara.charaId,
            name = CharaName(
                name = chara.charaName,
                shortName = chara.charaShortName
            ),
            charachipId = chara.charaGroupId,
            image = CharaImage(
                width = chara.displayWidth,
                height = chara.displayHeight,
                imageUrl = chara.charaImgUrl
            )
        )
    }

    private fun convertToDeadReasonToDead(vp: VillagePlayer): Dead {
        return Dead(vp.deadReasonCodeAsDeadReason, convertVillageDayToVillageDay(vp.villageDay.get()))
    }

    private fun detectItemText(settingList: List<VillageSetting>, item: CDef.VillageSettingItem): String? {
        return settingList.find { setting -> setting.villageSettingItemCodeAsVillageSettingItem == item }?.villageSettingText
    }
}