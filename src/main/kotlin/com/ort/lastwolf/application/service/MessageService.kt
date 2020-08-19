package com.ort.lastwolf.application.service

import com.ort.dbflute.allcommon.CDef
import com.ort.lastwolf.domain.model.ability.AbilityType
import com.ort.lastwolf.domain.model.charachip.Chara
import com.ort.lastwolf.domain.model.charachip.Charas
import com.ort.lastwolf.domain.model.daychange.DayChange
import com.ort.lastwolf.domain.model.message.Message
import com.ort.lastwolf.domain.model.message.MessageContent
import com.ort.lastwolf.domain.model.message.MessageQuery
import com.ort.lastwolf.domain.model.message.Messages
import com.ort.lastwolf.domain.model.player.Players
import com.ort.lastwolf.domain.model.skill.Skills
import com.ort.lastwolf.domain.model.village.Village
import com.ort.lastwolf.domain.model.village.participant.VillageParticipant
import com.ort.lastwolf.domain.service.ability.AbilityDomainService
import com.ort.lastwolf.domain.service.coming_out.ComingOutDomainService
import com.ort.lastwolf.domain.service.commit.CommitDomainService
import com.ort.lastwolf.domain.service.message.MessageDomainService
import com.ort.lastwolf.domain.service.participate.ParticipateDomainService
import com.ort.lastwolf.infrastructure.datasource.firebase.MessageLatestDatetimeDataSource
import com.ort.lastwolf.infrastructure.datasource.message.MessageDataSource
import org.springframework.stereotype.Service

@Service
class MessageService(
    private val messageDataSource: MessageDataSource,
    private val messageLatestDatetimeDataSource: MessageLatestDatetimeDataSource,
    private val abilityDomainService: AbilityDomainService,
    private val participateDomainService: ParticipateDomainService,
    private val commitDomainService: CommitDomainService,
    private val comingOutDomainService: ComingOutDomainService,
    private val messageDomainService: MessageDomainService
) {
    /**
     * 発言取得
     *
     * @param villageId villageId
     * @param villageDayId 村日付ID
     * @param query query
     * @return 発言
     */
    fun findMessages(
        villageId: Int,
        villageDayId: Int,
        query: MessageQuery
    ): Messages {
        return messageDataSource.findMessages(villageId, villageDayId, query)
    }

    /**
     * 最新発言日時取得
     *
     * @param villageId villageId
     * @param messageTypeList 発言種別
     * @param participant 参加情報
     * @return 発言
     */
    fun findLatestMessagesUnixTimeMilli(
        villageId: Int,
        messageTypeList: List<CDef.MessageType>,
        participant: VillageParticipant? = null
    ): Long {
        return messageDataSource.findLatestMessagesUnixTimeMilli(villageId, messageTypeList, participant)
    }

    /**
     * アンカー発言取得
     *
     * @param villageId villageId
     * @param messageType 発言種別
     * @param messageNumber 発言番号
     * @return 発言
     */
    fun findMessage(villageId: Int, messageType: CDef.MessageType, messageNumber: Int): Message? {
        return messageDataSource.findMessage(villageId, messageType, messageNumber)
    }

    /**
     * 参加者のその日の発言を取得
     *
     * @param villageId villageId
     * @param villageDay 村日付
     * @param participant 参加情報
     * @return 発言
     */
    fun findParticipateDayMessageList(
        villageId: Int,
        villageDay: com.ort.lastwolf.domain.model.village.VillageDay,
        participant: VillageParticipant?
    ): List<Message> {
        participant ?: return listOf()
        return messageDataSource.selectParticipateDayMessageList(villageId, villageDay.id, participant)
    }

    /**
     * 発言登録
     *
     * @param village village
     * @param players players
     * @param message 発言内容
     */
    fun registerMessage(
        village: Village,
        players: Players,
        message: Message
    ) {
        val registeredMessage = messageDataSource.registerMessage(village.id, message)
        messageDomainService.getViewableUserAndMessageLatestTime(village, players, registeredMessage).forEach {
            messageLatestDatetimeDataSource.register(village.id, it.uid, it.time)
        }
    }

    /**
     * 村作成時のシステムメッセージ登録
     */
    fun registerInitialMessage(village: Village, players: Players) {
        registerMessage(village, players, village.createVillagePrologueMessage())
    }

    /**
     * 村に参加する際の発言を登録
     */
    fun registerParticipateMessage(
        village: Village,
        players: Players,
        participant: VillageParticipant,
        chara: Chara,
        message: String,
        isSpectate: Boolean
    ) {
        // {N}人目、{キャラ名}。
        messageDataSource.registerMessage(
            village.id,
            participateDomainService.createParticipateMessage(village, chara, isSpectate)
        )
        // 参加発言
        val messageContent = MessageContent.invoke(
            if (isSpectate) CDef.MessageType.見学発言.code() else CDef.MessageType.通常発言.code(),
            message,
            CDef.FaceType.通常.code()
        )
        registerMessage(
            village,
            players,
            Message.createSayMessage(participant, village.day.prologueDay().id, messageContent)
        )
    }

    /**
     * 退村する際のシステムメッセージを登録
     */
    fun registerLeaveMessage(village: Village, players: Players, chara: Chara) =
        registerMessage(
            village,
            players,
            participateDomainService.createLeaveMessage(village, chara)
        )

    /**
     * 能力セットする際のシステムメッセージを登録
     * @param village village
     * @param participant 村参加者
     * @param targetId 対象の村参加者ID
     * @param abilityType abilityType
     * @param charas キャラ
     */
    fun registerAbilitySetMessage(
        village: Village,
        participant: VillageParticipant,
        targetId: Int?,
        abilityType: AbilityType,
        charas: Charas,
        players: Players
    ) {
        val myChara: Chara = charas.chara(participant.charaId)
        val targetChara: Chara? = if (targetId == null) null else charas.chara(village.participant, targetId)
        val message: Message = abilityDomainService.createAbilitySetMessage(village, myChara, targetChara, abilityType)
        registerMessage(village, players, message)
    }

    /**
     * コミットする際のシステムメッセージを登録
     *
     * @param village village
     * @param chara キャラ
     * @param doCommit コミット/取り消し
     */
    fun registerCommitMessage(village: Village, players: Players, chara: Chara, doCommit: Boolean) =
        registerMessage(
            village,
            players,
            commitDomainService.createCommitMessage(chara, doCommit, village.day.latestDay().id)
        )

    fun registerComingOutMessage(village: Village, players: Players, chara: Chara, skills: Skills) =
        registerMessage(
            village,
            players,
            comingOutDomainService.createComingOutMessage(chara, skills, village.day.latestDay().id)
        )

    /**
     * 差分更新
     */
    fun updateDifference(before: DayChange, after: DayChange) {
        val villageId = after.village.id
        val messages = messageDataSource.updateDifference(villageId, before.messages, after.messages)
        messageDomainService.getViewableUserAndMessageLatestTime(
            after.village,
            after.players,
            messages
        ).forEach {
            messageLatestDatetimeDataSource.register(villageId, it.uid, it.time)
        }
    }
}