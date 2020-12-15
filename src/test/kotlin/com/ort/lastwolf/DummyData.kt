package com.ort.lastwolf

import com.ort.dbflute.allcommon.CDef
import com.ort.lastwolf.domain.model.charachip.Chara
import com.ort.lastwolf.domain.model.charachip.CharaImage
import com.ort.lastwolf.domain.model.charachip.CharaName
import com.ort.lastwolf.domain.model.noonnight.NoonNight
import com.ort.lastwolf.domain.model.player.Player
import com.ort.lastwolf.domain.model.skill.SkillRequest
import com.ort.lastwolf.domain.model.village.Village
import com.ort.lastwolf.domain.model.village.VillageDay
import com.ort.lastwolf.domain.model.village.VillageDays
import com.ort.lastwolf.domain.model.village.VillageStatus
import com.ort.lastwolf.domain.model.village.participant.VillageParticipant
import com.ort.lastwolf.domain.model.village.participant.VillageParticipants
import com.ort.lastwolf.domain.model.village.setting.PersonCapacity
import com.ort.lastwolf.domain.model.village.setting.VillageCharachip
import com.ort.lastwolf.domain.model.village.setting.VillageOrganizations
import com.ort.lastwolf.domain.model.village.setting.VillagePassword
import com.ort.lastwolf.domain.model.village.setting.VillageRules
import com.ort.lastwolf.domain.model.village.setting.VillageSettings
import com.ort.lastwolf.domain.model.village.setting.VillageTime
import java.time.LocalDateTime
import java.util.*

object DummyData {

    fun prologueVillage(): Village {
        val dummyPlayer = villageParticipant()
        return Village(
            id = randomInt(),
            name = "dummy",
            creatorPlayer = Player(
                id = 1,
                uid = "dummy",
                twitterUserName = "dummy",
                nickname = "dummy",
                isRestrictedParticipation = false
            ),
            status = VillageStatus(CDef.VillageStatus.進行中),
            winCamp = null,
            setting = villageSetting(dummyPlayer.chara.id),
            participants = VillageParticipants(
                count = 1,
                list = listOf(
                    dummyPlayer
                )
            ),
            days = VillageDays(listOf(prologueVillageDay()))
        )
    }

    private fun villageSetting(dummyCharaId: Int): VillageSettings = VillageSettings(
        capacity = PersonCapacity(8, 20),
        time = villageTime(),
        charachip = VillageCharachip(
            dummyCharaId = dummyCharaId,
            charachipId = 1
        ),
        organizations = villageOrganizations(),
        rules = villageRules(),
        password = VillagePassword.invoke(null)
    )

    private fun villageTime(): VillageTime = VillageTime(
        createDatetime = LocalDateTime.now(),
        startDatetime = LocalDateTime.now().plusHours(1L),
        noonSeconds = 240,
        voteSeconds = 120,
        nightSeconds = 240
    )

    private fun villageOrganizations(): VillageOrganizations = VillageOrganizations()

    private fun villageRules(): VillageRules = VillageRules()

    private fun prologueVillageDay(): VillageDay = VillageDay(
        id = randomInt(),
        day = 1,
        noonNight = NoonNight(CDef.Noonnight.昼),
        isEpilogue = false,
        startDatetime = LocalDateTime.now(),
        endDatetime = LocalDateTime.now().plusHours(2L)
    )

    fun villageParticipant(): VillageParticipant = VillageParticipant(
        id = randomInt(),
        chara = chara(),
        player = player(),
        dead = null,
        isGone = false,
        skill = null,
        skillRequest = SkillRequest(CDef.Skill.おまかせ, CDef.Skill.おまかせ),
        winlose = null,
        comingOut = null,
        doneRollCall = false
    )

    fun chara(): Chara = Chara(
        id = randomInt(),
        name = charaName(),
        charachipId = 1,
        image = charaImage()
    )

    fun charaName(): CharaName = CharaName(
        name = "dummy",
        shortName = "d"
    )

    fun charaImage(): CharaImage = CharaImage(
        width = 50,
        height = 77,
        imageUrl = "dummy"
    )

    fun player(): Player = Player(
        id = randomInt(),
        uid = randomString(),
        nickname = "dummy",
        twitterUserName = "dummy",
        isRestrictedParticipation = false,
        participateProgressVillageIdList = listOf(),
        participateFinishedVillageIdList = listOf(),
        createProgressVillageIdList = listOf(),
        createFinishedVillageIdList = listOf()
    )

    private fun randomInt(): Int = Random().nextInt(10000)
    private fun randomString(): String = randomInt().toString()

}