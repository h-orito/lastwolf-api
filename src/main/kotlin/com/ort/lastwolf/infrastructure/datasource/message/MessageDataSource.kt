package com.ort.lastwolf.infrastructure.datasource.message

import com.ort.dbflute.allcommon.CDef
import com.ort.dbflute.cbean.MessageCB
import com.ort.dbflute.exbhv.MessageBhv
import com.ort.dbflute.exentity.Message
import com.ort.lastwolf.api.controller.VillageController
import com.ort.lastwolf.domain.model.message.MessageContent
import com.ort.lastwolf.domain.model.message.MessageQuery
import com.ort.lastwolf.domain.model.message.MessageTime
import com.ort.lastwolf.domain.model.message.MessageType
import com.ort.lastwolf.domain.model.message.Messages
import com.ort.lastwolf.domain.model.village.Village
import com.ort.lastwolf.domain.model.village.participant.VillageParticipant
import com.ort.lastwolf.fw.LastwolfDateUtil
import com.ort.lastwolf.infrastructure.datasource.firebase.FirebaseDataSource
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import java.time.ZoneOffset

@Repository
class MessageDataSource(
    val messageBhv: MessageBhv,
    val firebaseDataSource: FirebaseDataSource
) {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    private val logger = LoggerFactory.getLogger(VillageController::class.java)

    // ===================================================================================
    //                                                                             Execute
    //                                                                           =========
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
        if (query.messageTypeList.isEmpty() && !query.includeMonologue) {
            return Messages(listOf())
        }

        return if (query.isPaging()) {
            val messageList = messageBhv.selectPage {
                queryMessage(
                    cb = it,
                    villageId = villageId,
                    query = query
                )
                it.query().addOrderBy_MessageUnixtimestampMilli_Asc()
                it.paging(query.pageSize!!, query.pageNum!!)
            }
            Messages(
                list = messageList.map { convertMessageToMessage(it) },
                allRecordCount = messageList.allRecordCount,
                allPageCount = messageList.allPageCount,
                isExistPrePage = messageList.existsPreviousPage(),
                isExistNextPage = messageList.existsNextPage(),
                currentPageNum = messageList.currentPageNumber
            )

        } else {
            val messageList = messageBhv.selectList {
                queryMessage(
                    cb = it,
                    villageId = villageId,
                    query = query
                )
                it.query().addOrderBy_MessageUnixtimestampMilli_Asc()
            }
            Messages(
                list = messageList.map { convertMessageToMessage(it) }
            )
        }
    }

    /**
     * 最新発言日時取得
     *
     * @param villageId villageId
     * @param messageTypeList 発言種別
     * @param participant 参加情報
     * @return 最新発言日時(unix_datetime_milli)
     */
    fun findLatestMessagesUnixTimeMilli(
        villageId: Int,
        messageTypeList: List<CDef.MessageType>,
        participant: VillageParticipant?
    ): Long {
        val query = MessageQuery(
            from = null,
            pageSize = null,
            pageNum = null,
            keyword = null,
            participant = participant,
            messageTypeList = messageTypeList,
            participantIdList = null,
            includeMonologue = false,
            includePrivateAbility = false
        )
        return messageBhv.selectEntityWithDeletedCheck() {
            queryMessage(it, villageId, query)
            it.query().addOrderBy_MessageUnixtimestampMilli_Desc()
            it.fetchFirst(1)
        }.messageUnixtimestampMilli
    }

    /**
     * 参加者のその日の発言を取得
     *
     * @param villageId villageId
     * @param villageDayId 村日付ID
     * @param participant 参加情報
     * @return 発言
     */
    fun selectParticipateDayMessageList(
        villageId: Int,
        villageDayId: Int,
        participant: VillageParticipant
    ): List<com.ort.lastwolf.domain.model.message.Message> {
        val messageList = messageBhv.selectList {
            it.query().setVillageId_Equal(villageId)
            it.query().setVillagePlayerId_Equal(participant.id)
            it.query().setVillageDayId_Equal(villageDayId)
        }
        return messageList.map { convertMessageToMessage(it) }
    }

    fun registerMessage(
        village: Village,
        message: com.ort.lastwolf.domain.model.message.Message
    ): com.ort.lastwolf.domain.model.message.Message {
        if (!village.days.first(message.time.villageDayId).isNightTime()) {
            firebaseDataSource.registerMessage(village, message)
            return message
        }
        val mes = Message()
        val messageTypeCode = message.content.type.code
        mes.villageId = village.id
        mes.villageDayId = message.time.villageDayId
        mes.messageTypeCode = messageTypeCode
        mes.messageContent = message.content.text
        mes.villagePlayerId = message.fromParticipantId
        mes.isStrong = message.content.isStrong
        val now = LastwolfDateUtil.currentLocalDateTime()
        mes.messageDatetime = now
        val epocTimeMilli = now.toInstant(ZoneOffset.ofHours(+9)).toEpochMilli()
        mes.messageUnixtimestampMilli = epocTimeMilli

        messageBhv.insert(mes)
        return convertMessageToMessage(mes)
    }


    /**
     * 差分更新
     * @param village village
     * @param before messages
     * @param after messages
     */
    fun updateDifference(village: Village, before: Messages, after: Messages): Messages {
        // 追加しかないのでbeforeにないindexから追加していく
        val messageList = after.list.drop(before.list.size).map {
            registerMessage(village, it)
        }
        return Messages(list = messageList)
    }

    // ===================================================================================
    //                                                                        Assist Logic
    //                                                                        ============
    private fun convertMessageToMessage(message: Message): com.ort.lastwolf.domain.model.message.Message {
        return com.ort.lastwolf.domain.model.message.Message(
            fromParticipantId = message.villagePlayerId,
            time = MessageTime(
                villageDayId = message.villageDayId,
                datetime = message.messageDatetime,
                unixTimeMilli = message.messageUnixtimestampMilli
            ),
            content = MessageContent(
                type = MessageType(
                    code = message.messageTypeCode,
                    name = CDef.MessageType.codeOf(message.messageTypeCode).alias()
                ),
                text = message.messageContent,
                isStrong = message.isStrong
            )
        )
    }

    private fun queryMessage(
        cb: MessageCB,
        villageId: Int,
        query: MessageQuery
    ) {
        cb.query().setVillageId_Equal(villageId)
        // 参加していない場合は特に考慮不要
        if (query.participant == null) {
            cb.query().setMessageTypeCode_InScope(query.messageTypeList.map { type -> type.code() })
        } else {
            val participantId = query.participant.id
            // 進行中で独り言や秘話だけを見たい場合
            if (query.messageTypeList.isEmpty()) {
                if (!query.includeMonologue && !query.includePrivateAbility) {
                    // 何もしない
                } else {
                    cb.orScopeQuery { orCB ->
                        if (query.includeMonologue) orCB.orScopeQueryAndPart { andCB -> queryMyMonologue(andCB, participantId) }
                        if (query.includePrivateAbility) orCB.orScopeQueryAndPart { andCB -> queryMyPrivateAbility(andCB, participantId) }
                    }
                }
            }
            // エピローグなど、全部見える状況の場合はorでなくて良い
            else if (!query.includeMonologue && !query.includePrivateAbility) {
                cb.query().setMessageTypeCode_InScope(query.messageTypeList.map { type -> type.code() })
            }
            // その他
            else {
                cb.orScopeQuery { orCB ->
                    orCB.query().setMessageTypeCode_InScope(query.messageTypeList.map { type -> type.code() })
                    if (query.includeMonologue) orCB.orScopeQueryAndPart { andCB -> queryMyMonologue(andCB, participantId) }
                    if (query.includePrivateAbility) orCB.orScopeQueryAndPart { andCB -> queryMyPrivateAbility(andCB, participantId) }
                }
            }
        }
        query.from?.let { cb.query().setMessageUnixtimestampMilli_GreaterThan(it) }
        query.keyword?.let { cb.query().setMessageContent_LikeSearch(it) { op -> op.splitByBlank().likeContain().asOrSplit() } }
        if (!query.participantIdList.isNullOrEmpty()) cb.query().setVillagePlayerId_InScope(query.participantIdList)
    }

    private fun queryMyMonologue(cb: MessageCB, id: Int) {
        cb.query().setVillagePlayerId_Equal(id)
        cb.query().setMessageTypeCode_Equal(CDef.MessageType.独り言.code())
    }

    private fun queryMyPrivateAbility(cb: MessageCB, id: Int) {
        cb.query().setVillagePlayerId_Equal(id)
        cb.query().setMessageTypeCode_InScope(MessageQuery.personalPrivateAbilityList.map { it.code() })
    }
}
