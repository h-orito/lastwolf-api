package com.ort.lastwolf.domain.service.message

import com.ort.dbflute.allcommon.CDef
import com.ort.lastwolf.domain.model.message.Message
import com.ort.lastwolf.domain.model.message.MessageQuery
import com.ort.lastwolf.domain.model.message.Messages
import com.ort.lastwolf.domain.model.player.Players
import com.ort.lastwolf.domain.model.village.Village
import com.ort.lastwolf.domain.model.village.participant.VillageParticipant
import com.ort.lastwolf.domain.service.say.GraveSayDomainService
import com.ort.lastwolf.domain.service.say.MonologueSayDomainService
import com.ort.lastwolf.domain.service.say.NormalSayDomainService
import com.ort.lastwolf.domain.service.say.WerewolfSayDomainService
import org.springframework.stereotype.Service

@Service
class MessageDomainService(
    private val normalSayDomainService: NormalSayDomainService,
    private val werewolfSayDomainService: WerewolfSayDomainService,
    private val graveSayDomainService: GraveSayDomainService,
    private val monologueSayDomainService: MonologueSayDomainService,
    private val psychicMessageDomainService: PsychicMessageDomainService,
    private val attackMessageDomainService: AttackMessageDomainService,
    private val masonMessageDomainService: MasonMessageDomainService
) {

    private val everyoneAllowedMessageTypeList = listOf(CDef.MessageType.公開システムメッセージ, CDef.MessageType.通常発言, CDef.MessageType.村建て発言)

    /**
     * 閲覧できる発言種別リスト
     *
     * @param participant 参加情報
     * @param day 何日目か
     * @param authority ユーザの権限
     * @return 閲覧できる発言種別
     */
    fun viewableMessageTypeList(
        village: Village,
        participant: VillageParticipant?,
        authority: CDef.Authority?
    ): List<CDef.MessageType> {
        // 管理者は全て見られる
        if (authority == CDef.Authority.管理者) return CDef.MessageType.listAll()
        // 村が終了していたら全て見られる
        if (village.status.isSolved()) return CDef.MessageType.listAll()

        val allowedTypeList: MutableList<CDef.MessageType> = mutableListOf()
        allowedTypeList.addAll(everyoneAllowedMessageTypeList)
        // 権限に応じて追加していく（独り言と秘話はここでは追加しない）
        listOf(
            CDef.MessageType.死者の呻き,
            CDef.MessageType.人狼の囁き,
            CDef.MessageType.白黒霊視結果,
            CDef.MessageType.襲撃結果,
            CDef.MessageType.共有相互確認メッセージ
        ).forEach {
            if (isViewableMessage(village, participant, it.code())) allowedTypeList.add(it)
        }
        return allowedTypeList
    }

    /**
     * 閲覧できるか
     *
     * @param participant 参加情報
     * @param messageType 発言種別
     * @return 閲覧できるか
     */
    fun isViewableMessage(
        village: Village,
        participant: VillageParticipant?,
        messageType: String
    ): Boolean {
        if (village.dummyParticipant()?.id == participant?.id) return true
        return when (CDef.MessageType.codeOf(messageType) ?: return false) {
            CDef.MessageType.通常発言 -> normalSayDomainService.isViewable(village, participant)
            CDef.MessageType.人狼の囁き -> werewolfSayDomainService.isViewable(village, participant)
            CDef.MessageType.死者の呻き -> graveSayDomainService.isViewable(village, participant)
            CDef.MessageType.独り言 -> monologueSayDomainService.isViewable(village, participant)
            CDef.MessageType.白黒霊視結果 -> psychicMessageDomainService.isViewable(village, participant)
            CDef.MessageType.襲撃結果 -> attackMessageDomainService.isViewable(village, participant)
            CDef.MessageType.共有相互確認メッセージ -> masonMessageDomainService.isViewable(village, participant)
            CDef.MessageType.村建て発言 -> true
            else -> return false
        }
    }

    fun createQuery(
        village: Village,
        participant: VillageParticipant?,
        authority: CDef.Authority?,
        messageTypeList: List<CDef.MessageType>?,
        from: Long?,
        pageSize: Int?,
        pageNum: Int?,
        keyword: String?,
        participantIdList: List<Int>?
    ): MessageQuery {
        val availableMessageTypeList = viewableMessageTypeList(village, participant, authority)
        val requestMessageTypeList = if (messageTypeList.isNullOrEmpty()) CDef.MessageType.listAll() else messageTypeList
        val queryMessageTypeList = requestMessageTypeList.filter { availableMessageTypeList.contains(it) }
        return MessageQuery(
            from = from,
            pageSize = pageSize,
            pageNum = pageNum,
            keyword = if (keyword.isNullOrBlank()) null else keyword,
            participant = participant,
            messageTypeList = queryMessageTypeList,
            participantIdList = participantIdList,
            includeMonologue = isIncludeMonologue(participant, participantIdList, requestMessageTypeList, queryMessageTypeList),
            includePrivateAbility = isIncludePrivateAbility(participant, requestMessageTypeList)
        )
    }

    fun getViewableUserAndMessageLatestTime(
        village: Village,
        message: Message
    ): List<UserViewableMessageLatestTime> {
        val updateParticipantAndTimeList =
            village.participants.list.mapNotNull { participant ->
                val isViewable = isViewableMessage(village, participant, message.content.type.code)
                val isMessageForMe = isMessageForMe(participant, message)
                val isEveryoneViewable = isEveryoneViewable(message)
                if (isViewable || isMessageForMe || isEveryoneViewable) {
                    UserViewableMessageLatestTime(
                        uid = participant.player.uid,
                        time = message.time.unixTimeMilli
                    )
                } else null
            }
        // 非ログインユーザ
        return if (isViewableMessage(village, null, message.content.type.code)) {
            updateParticipantAndTimeList + UserViewableMessageLatestTime(null, message.time.unixTimeMilli)
        } else {
            updateParticipantAndTimeList
        }
    }

    fun getViewableUserAndMessageLatestTime(
        village: Village,
        players: Players,
        messages: Messages
    ): List<UserViewableMessageLatestTime> {
        val updateParticipantAndTimeList =
            village.participants.list.mapNotNull { participant ->
                messages.list.filter { message ->
                    val isViewable = isViewableMessage(village, participant, message.content.type.code)
                    val isMessageForMe = isMessageForMe(participant, message)
                    val isEveryoneViewable = isEveryoneViewable(message)
                    isViewable || isMessageForMe || isEveryoneViewable
                }.maxBy { it.time.unixTimeMilli }?.let { message ->
                    UserViewableMessageLatestTime(
                        uid = participant.player.uid,
                        time = message.time.unixTimeMilli
                    )
                }
            }
        // 非ログインユーザ
        val notLoginUser = messages.list.filter { message ->
            val day = village.days.list.first { it.id == message.time.villageDayId }.day
            isViewableMessage(village, null, message.content.type.code)
        }.maxBy { it.time.unixTimeMilli }?.let { message ->
            UserViewableMessageLatestTime(
                uid = null,
                time = message.time.unixTimeMilli
            )
        }
        return if (notLoginUser == null) {
            updateParticipantAndTimeList
        } else {
            updateParticipantAndTimeList + notLoginUser
        }
    }

    data class UserViewableMessageLatestTime(val uid: String?, val time: Long)

    private fun isEveryoneViewable(message: Message): Boolean = everyoneAllowedMessageTypeList.any { it == message.content.type.toCdef() }

    private fun isMessageForMe(participant: VillageParticipant, message: Message): Boolean {
        return listOf(CDef.MessageType.個別能力行使結果, CDef.MessageType.独り言).any { it == message.content.type.toCdef() }
            && message.fromParticipantId == participant.id
    }

    private fun isIncludeMonologue(
        participant: VillageParticipant?,
        participantIdList: List<Int>?,
        requestMessageTypeList: List<CDef.MessageType>,
        queryMessageTypeList: List<CDef.MessageType>
    ): Boolean {
        // 既に取得対象になっていれば不要
        if (queryMessageTypeList.contains(CDef.MessageType.独り言)) return false
        // 自分が取得対象になっていなければ不要
        participant ?: return false
        if (participantIdList != null && !participantIdList.contains(participant.id)) return false
        // 求めていなければ不要
        if (!requestMessageTypeList.contains(CDef.MessageType.独り言)) return false

        return true
    }

    // 霊視や襲撃でなく、占いなどの個別のが対象
    private fun isIncludePrivateAbility(
        participant: VillageParticipant?,
        requestMessageTypeList: List<CDef.MessageType>
    ): Boolean {
        participant ?: return false
        return requestMessageTypeList.contains(CDef.MessageType.公開システムメッセージ)
    }
}