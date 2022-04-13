package com.ort.lastwolf.application.service

import com.ort.dbflute.allcommon.CDef
import com.ort.lastwolf.domain.model.charachip.Chara
import com.ort.lastwolf.domain.model.daychange.DayChange
import com.ort.lastwolf.domain.model.message.Message
import com.ort.lastwolf.domain.model.message.MessageQuery
import com.ort.lastwolf.domain.model.message.Messages
import com.ort.lastwolf.domain.model.skill.Skill
import com.ort.lastwolf.domain.model.village.Village
import com.ort.lastwolf.domain.model.village.ability.VillageAbility
import com.ort.lastwolf.domain.model.village.participant.VillageParticipant
import com.ort.lastwolf.domain.service.ability.AbilityDomainService
import com.ort.lastwolf.domain.service.coming_out.ComingOutDomainService
import com.ort.lastwolf.domain.service.commit.CommitDomainService
import com.ort.lastwolf.domain.service.message.MessageDomainService
import com.ort.lastwolf.domain.service.participate.ParticipateDomainService
import com.ort.lastwolf.infrastructure.datasource.firebase.FirebaseDataSource
import com.ort.lastwolf.infrastructure.datasource.message.MessageDataSource
import org.springframework.stereotype.Service

@Service
class MessageService(
    private val messageDataSource: MessageDataSource,
    private val firebaseDataSource: FirebaseDataSource,
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
     * @param query query
     * @return 発言
     */
    fun findMessages(
        villageId: Int,
        query: MessageQuery
    ): Messages {
        return messageDataSource.findMessages(villageId, query)
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
     * 村作成時のシステムメッセージ登録
     */
    fun registerInitialMessage(village: Village) {
        registerMessage(village, village.createVillagePrologueMessage())
    }

    /**
     * 発言登録
     *
     * @param village village
     * @param message 発言内容
     */
    fun registerMessage(
        village: Village,
        message: Message
    ) {
        val registeredMessage = messageDataSource.registerMessage(village, message)
        messageDomainService.getViewableUserAndMessageLatestTime(village, registeredMessage).forEach {
            firebaseDataSource.registerMessageLatest(village.id, it.uid, it.time)
        }
    }

    /**
     * 村に参加する際の発言を登録
     */
    fun registerParticipateMessage(
        village: Village,
        participant: VillageParticipant,
        chara: Chara
    ) {
        // {N}人目、{キャラ名}。
        messageDataSource.registerMessage(
            village,
            participateDomainService.createParticipateMessage(village, chara)
        )
    }

    /**
     * 退村する際のシステムメッセージを登録
     */
    fun registerLeaveMessage(village: Village, chara: Chara) =
        registerMessage(
            village,
            participateDomainService.createLeaveMessage(village, chara)
        )

    /**
     * 能力セットする際のシステムメッセージを登録
     */
    fun registerAbilitySetMessage(
        village: Village,
        participant: VillageParticipant,
        ability: VillageAbility
    ) {
        val message: Message = abilityDomainService.createAbilityMessage(village, participant, ability)
        registerMessage(village, message)
    }

    /**
     * コミットする際のシステムメッセージを登録
     *
     * @param village village
     * @param chara キャラ
     * @param doCommit コミット/取り消し
     */
    fun registerCommitMessage(village: Village, chara: Chara, doCommit: Boolean) =
        registerMessage(
            village,
            commitDomainService.createCommitMessage(chara, doCommit, village.days.latestDay().id)
        )

    fun registerComingOutMessage(village: Village, chara: Chara, skill: Skill?) =
        registerMessage(
            village,
            comingOutDomainService.createComingOutMessage(chara, skill, village.days.latestDay().id)
        )

    /**
     * 差分更新
     */
    fun updateDifference(before: DayChange, after: DayChange) {
        val villageId = after.village.id
        val messages = messageDataSource.updateDifference(after.village, before.messages, after.messages)
        messageDomainService.getViewableUserAndMessageLatestTime(
            after.village,
            after.players,
            messages
        ).forEach {
            firebaseDataSource.registerMessageLatest(villageId, it.uid, it.time)
        }
    }
}