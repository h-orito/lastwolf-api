package com.ort.lastwolf.dummy

import com.ort.dbflute.allcommon.CDef
import com.ort.lastwolf.domain.model.charachip.Chara
import com.ort.lastwolf.domain.model.charachip.CharaDefaultMessage
import com.ort.lastwolf.domain.model.charachip.CharaName
import com.ort.lastwolf.domain.model.charachip.CharaSize
import com.ort.lastwolf.domain.model.charachip.Charas
import com.ort.lastwolf.domain.model.commit.Commits
import com.ort.lastwolf.domain.model.daychange.DayChange
import com.ort.lastwolf.domain.model.dead.Dead
import com.ort.lastwolf.domain.model.message.Message
import com.ort.lastwolf.domain.model.message.MessageContent
import com.ort.lastwolf.domain.model.message.MessageTime
import com.ort.lastwolf.domain.model.message.MessageType
import com.ort.lastwolf.domain.model.player.Player
import com.ort.lastwolf.domain.model.player.Players
import com.ort.lastwolf.domain.model.skill.Skill
import com.ort.lastwolf.domain.model.skill.SkillRequest
import com.ort.lastwolf.domain.model.village.Village
import com.ort.lastwolf.domain.model.village.VillageCharachipCreateResource
import com.ort.lastwolf.domain.model.village.VillageCreateResource
import com.ort.lastwolf.domain.model.village.VillageDay
import com.ort.lastwolf.domain.model.village.VillageDays
import com.ort.lastwolf.domain.model.village.VillageOrganizationCreateResource
import com.ort.lastwolf.domain.model.village.VillageRuleCreateResource
import com.ort.lastwolf.domain.model.village.VillageSettingCreateResource
import com.ort.lastwolf.domain.model.village.VillageStatus
import com.ort.lastwolf.domain.model.village.VillageTimeCreateResource
import com.ort.lastwolf.domain.model.village.ability.VillageAbilities
import com.ort.lastwolf.domain.model.village.participant.VillageParticipant
import com.ort.lastwolf.domain.model.village.participant.VillageParticipants
import com.ort.lastwolf.domain.model.village.participant.coming_out.ComingOuts
import com.ort.lastwolf.domain.model.village.setting.PersonCapacity
import com.ort.lastwolf.domain.model.village.setting.VillageCharachip
import com.ort.lastwolf.domain.model.village.setting.VillageOrganizations
import com.ort.lastwolf.domain.model.village.setting.VillagePassword
import com.ort.lastwolf.domain.model.village.setting.VillageRules
import com.ort.lastwolf.domain.model.village.setting.VillageSettings
import com.ort.lastwolf.domain.model.village.setting.VillageTime
import com.ort.lastwolf.domain.model.village.vote.VillageVotes
import java.time.LocalDateTime

object DummyDomainModelCreator {

    fun createDummyVillage(): Village {
        return Village(
            id = randomNumber(),
            name = "dummy",
            creatorPlayerId = randomNumber(),
            status = createDummyVillageStatus(),
            winCamp = null,
            setting = createDummyVillageSettings(),
            participant = createDummyVillageParticipants(),
            spectator = createDummyVillageParticipants(),
            day = createDummyVillageDays()
        )
    }

    fun createDummyVillageRegisterParam(): Village = Village.createForRegister(
        resource = VillageCreateResource(
            villageName = "aaa",
            createPlayerId = 1,
            setting = VillageSettingCreateResource(
                time = VillageTimeCreateResource(
                    startDatetime = LocalDateTime.now().plusDays(1L),
                    silentHours = null
                ),
                organization = VillageOrganizationCreateResource(
                    organization = "狼狼狼狂占霊狩村村村村村村村村村"
                ),
                charachip = VillageCharachipCreateResource(
                    dummyCharaId = 1,
                    charachipId = 1
                ),
                rule = VillageRuleCreateResource(
                    isOpenVote = false,
                    isAvailableSkillRequest = true,
                    isAvailableSpectate = false,
                    isOpenSkillInGrave = false,
                    isVisibleGraveMessage = false,
                    isAvailableSuddenlyDeath = true,
                    isAvailableCommit = false,
                    isAvailableDummySkill = false,
                    restrictList = listOf(),
                    joinPassword = null
                )
            )
        )
    )

    fun createDummyVillageDays(): VillageDays = VillageDays(listOf())

    fun createDummyVillageDay(): VillageDay = VillageDay(
        id = randomNumber(),
        day = randomNumber(),
        noonnight = CDef.Noonnight.昼.code(),
        dayChangeDatetime = LocalDateTime.now()
    )

    fun createDummyVillageParticipants(): VillageParticipants = VillageParticipants(0, listOf())

    fun createDummyVillageSettings(): VillageSettings {
        return VillageSettings(
            capacity = createDummyPersonCapacity(),
            time = createDummyVillageTime(),
            charachip = createDummyVillageCharachip(),
            organizations = createDummyVillageOrganizations(),
            rules = createDummyVillageRules(),
            password = createDummyVillagePassword()
        )
    }

    fun createDummyVillagePassword(): VillagePassword = VillagePassword(false, null)

    fun createDummyVillageRules(): VillageRules = VillageRules()

    fun createDummyVillageOrganizations(): VillageOrganizations = VillageOrganizations()

    fun createDummyVillageCharachip(): VillageCharachip = VillageCharachip(1, 1)

    fun createDummyVillageTime(): VillageTime = VillageTime("dummy", LocalDateTime.now(), null, null, LocalDateTime.now(), 0, null)

    fun createDummyPersonCapacity(): PersonCapacity = PersonCapacity(1, 1)

    fun createDummyVillageStatus(): VillageStatus = VillageStatus(CDef.VillageStatus.廃村)

    fun createDummyVillageParticipant(): VillageParticipant = VillageParticipant(
        id = randomNumber(),
        charaId = randomNumber(),
        playerId = randomNumber(),
        dead = null,
        isSpectator = false,
        isGone = false,
        skill = null,
        skillRequest = createDummySkillRequest(),
        isWin = null,
        commigOuts = ComingOuts()
    )

    fun createDummySkillRequest(): SkillRequest = SkillRequest(
        first = Skill(CDef.Skill.人狼),
        second = Skill(CDef.Skill.人狼)
    )

    fun createDummyVillageAbilities(): VillageAbilities = VillageAbilities(listOf())

    fun createDummyChara(): Chara = Chara(
        id = randomNumber(),
        charaName = createDummyCharaName(),
        charachipId = randomNumber(),
        defaultMessage = createDummyCharaDefaultMessage(),
        display = createDummyCharaSize(),
        faceList = listOf()
    )

    fun createDummyCharaSize(): CharaSize = CharaSize(50, 60)

    fun createDummyCharaDefaultMessage(): CharaDefaultMessage = CharaDefaultMessage("dummy", "dummy")

    fun createDummyCharaName(): CharaName = CharaName("dummy", "dummy")

    fun createDummyDead(): Dead = Dead(CDef.DeadReason.処刑, createDummyVillageDay())

    fun createDummyVillageVotes(): VillageVotes = VillageVotes(listOf())

    fun createDummyPlayers(): Players = Players(listOf())

    fun createDummyPlayer(): Player = Player(
        id = randomNumber(),
        uid = randomNumber().toString(),
        nickname = "dummy",
        twitterUserName = "dummy",
        otherSiteName = null,
        introduction = null,
        isRestrictedParticipation = false
    )

    fun createDummyCharas(): Charas = Charas(listOf())

    fun createDummyDayChange(): DayChange = DayChange(
        village = createDummyVillage(),
        votes = createDummyVillageVotes(),
        abilities = createDummyVillageAbilities(),
        players = createDummyPlayers()
    )

    fun createDummyCommits(): Commits = Commits(listOf())

    fun createDummyMessage(): Message = Message(
        fromVillageParticipantId = createDummyVillageParticipant().id,
        toVillageParticipantId = null,
        time = createDummyMessageTime(),
        content = createDummyMessageContent()
    )

    fun createDummyMessageTime(): MessageTime = MessageTime(
        villageDayId = randomNumber(),
        datetime = LocalDateTime.now(),
        unixTimeMilli = 1L
    )

    fun createDummyMessageContent(): MessageContent = MessageContent(
        type = MessageType(CDef.MessageType.公開システムメッセージ),
        num = null,
        count = null,
        text = "dummy message",
        faceCode = null
    )

    // ===================================================================================
    //                                                                                頻出
    //                                                                        ============
    fun createDummyFirstVillageDay(): VillageDay = createDummyVillageDay().copy(day = 1)

    fun createDummyAliveWolf(): VillageParticipant = createDummyVillageParticipant().copy(skill = Skill(CDef.Skill.人狼))

    fun createDummyAliveVillager(): VillageParticipant = createDummyVillageParticipant().copy(skill = Skill(CDef.Skill.村人))

    fun createDummyAliveSeer(): VillageParticipant = createDummyVillageParticipant().copy(skill = Skill(CDef.Skill.占い師))

    fun createDummyAlivePsychic(): VillageParticipant = createDummyVillageParticipant().copy(skill = Skill(CDef.Skill.霊能者))

    fun createDummyAliveHunter(): VillageParticipant = createDummyVillageParticipant().copy(skill = Skill(CDef.Skill.狩人))

    fun createDummyAliveMadman(): VillageParticipant = createDummyVillageParticipant().copy(skill = Skill(CDef.Skill.狂人))

    fun createDummyDeadWolf(): VillageParticipant = createDummyAliveWolf().copy(dead = createDummyDead())

    fun createDummyDeadVillager(): VillageParticipant = createDummyAliveVillager().copy(dead = createDummyDead())

    fun createDummyDeadSeer(): VillageParticipant = createDummyAliveSeer().copy(dead = createDummyDead())

    fun createDummyDeadPsychic(): VillageParticipant = createDummyAlivePsychic().copy(dead = createDummyDead())

    fun createDummyDeadHunter(): VillageParticipant = createDummyAliveHunter().copy(dead = createDummyDead())

    // ===================================================================================
    //                                                                        Assist Logic
    //                                                                        ============
    private fun randomNumber(): Int = (Math.random() * 10000).toInt()
}