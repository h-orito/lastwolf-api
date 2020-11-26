package com.ort.lastwolf.application.coordinator

import com.ort.dbflute.allcommon.CDef
import com.ort.lastwolf.application.service.MessageService
import com.ort.lastwolf.domain.model.message.Messages
import com.ort.lastwolf.domain.model.village.Village
import com.ort.lastwolf.domain.model.village.participant.VillageParticipant
import com.ort.lastwolf.domain.service.message.MessageDomainService
import com.ort.lastwolf.fw.security.LastwolfUser
import org.springframework.stereotype.Service

@Service
class MessageCoordinator(
    val dayChangeCoordinator: DayChangeCoordinator,
    val villageCoordinator: VillageCoordinator,
    val messageService: MessageService,
    val messageDomainService: MessageDomainService
) {

    // ===================================================================================
    //                                                                             Execute
    //                                                                           =========
    fun findMessageList(
        village: Village,
        user: LastwolfUser?,
        from: Long?,
        pageSize: Int?,
        pageNum: Int?,
        keyword: String?,
        messageTypeList: List<CDef.MessageType>?,
        participantIdList: List<Int>?
    ): Messages {
        val participant: VillageParticipant? = villageCoordinator.findParticipant(village, user)
        val query = messageDomainService.createQuery(
            village = village,
            participant = participant,
            authority = user?.authority,
            messageTypeList = messageTypeList,
            from = from,
            pageSize = pageSize,
            pageNum = pageNum,
            keyword = keyword,
            participantIdList = participantIdList
        )
        val messages: Messages = messageService.findMessages(
            villageId = village.id,
            query = query
        )
        dayChangeCoordinator.dayChangeIfNeeded(village.id)
        return messages
    }

    fun findLatestMessagesUnixTimeMilli(
        village: Village,
        user: LastwolfUser?
    ): Long {
        val participant: VillageParticipant? = villageCoordinator.findParticipant(village, user)
        val messageTypeList: List<CDef.MessageType> =
            messageDomainService.viewableMessageTypeList(village, participant, user?.authority)
        return messageService.findLatestMessagesUnixTimeMilli(village.id, messageTypeList, participant)
    }
}
