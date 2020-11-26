package com.ort.lastwolf.domain.service.say

import com.ort.dbflute.allcommon.CDef
import com.ort.lastwolf.domain.model.charachip.Chara
import com.ort.lastwolf.domain.model.message.Message
import com.ort.lastwolf.domain.model.message.MessageContent
import com.ort.lastwolf.domain.model.message.MessageType
import com.ort.lastwolf.domain.model.myself.participant.VillageSaySituation
import com.ort.lastwolf.domain.model.village.Village
import com.ort.lastwolf.domain.model.village.participant.VillageParticipant
import com.ort.lastwolf.fw.exception.LastwolfBadRequestException
import com.ort.lastwolf.fw.exception.LastwolfBusinessException
import org.springframework.stereotype.Service

@Service
class SayDomainService(
    private val normalSayDomainService: NormalSayDomainService,
    private val graveSayDomainService: GraveSayDomainService,
    private val monologueSayDomainService: MonologueSayDomainService,
    private val werewolfSayDomainService: WerewolfSayDomainService
) {

    private val defaultMessageTypeOrder = listOf(
        CDef.MessageType.人狼の囁き,
        CDef.MessageType.通常発言,
        CDef.MessageType.死者の呻き,
        CDef.MessageType.独り言
    )

    fun convertToSituation(
        village: Village,
        participant: VillageParticipant?
    ): VillageSaySituation {
        return VillageSaySituation(
            isAvailableSay = isAvailableSay(village, participant),
            selectableMessageTypeList = getSelectableMessageTypeList(village, participant),
            defaultMessageType = detectDefaultMessageType(
                isAvailableSay(village, participant),
                getSelectableMessageTypeList(village, participant)
            )
        )
    }

    fun assertSay(
        village: Village,
        participant: VillageParticipant?,
        chara: Chara?,
        latestDayMessageList: List<Message>,
        messageContent: MessageContent
    ) {
        // 事前チェック
        if (!isAvailableSay(village, participant)) throw LastwolfBusinessException("発言できません")
        // 発言種別ごとのチェック
        when (messageContent.type.toCdef()) {
            CDef.MessageType.通常発言 -> normalSayDomainService.assertSay(village, participant!!)
            CDef.MessageType.人狼の囁き -> werewolfSayDomainService.assertSay(village, participant!!)
            CDef.MessageType.死者の呻き -> graveSayDomainService.assertSay(village, participant!!)
            CDef.MessageType.独り言 -> monologueSayDomainService.assertSay(village, participant!!)
            else -> throw LastwolfBadRequestException("不正な発言種別です")
        }
        // 発言回数、長さ、行数チェック
        messageContent.assertMessageLength()
    }

    fun assertCreatorSay(
        village: Village,
        messageContent: MessageContent
    ) {
        // 事前チェック
        if (!village.isAvailableSay()) throw LastwolfBusinessException("発言できません")
        // 長さ、行数チェック
        messageContent.assertMessageLength()
    }

    fun assertParticipateSay(
        village: Village,
        chara: Chara?,
        messageContent: MessageContent
    ) {
        // 事前チェック
        if (!village.isAvailableSay()) throw LastwolfBusinessException("入村発言できません")
        // 発言長さ、行数チェック
        messageContent.assertMessageLength()
    }

    // ===================================================================================
    //                                                                        Assist Logic
    //                                                                        ============
    private fun isAvailableSay(village: Village, participant: VillageParticipant?): Boolean {
        // 参加者として可能か
        participant ?: return false
        if (!participant.isAvailableSay(village.status.isSettled())) return false
        // 村として可能か
        if (!village.isAvailableSay()) return false
        return true
    }

    private fun getSelectableMessageTypeList(
        village: Village,
        participant: VillageParticipant?
    ): List<MessageType> {
        if (!isAvailableSay(village, participant)) return listOf()

        val list: MutableList<MessageType> = mutableListOf()

        if (normalSayDomainService.isSayable(village, participant!!)) list.add(
            MessageType(CDef.MessageType.通常発言)
        )
        if (werewolfSayDomainService.isSayable(village, participant)) list.add(
            MessageType(CDef.MessageType.人狼の囁き)
        )
        if (graveSayDomainService.isSayable(village, participant)) list.add(
            MessageType(CDef.MessageType.死者の呻き)
        )
        if (monologueSayDomainService.isSayable(village, participant)) list.add(
            MessageType(CDef.MessageType.独り言)
        )

        return list
    }

    private fun detectDefaultMessageType(
        availableSay: Boolean,
        selectableMessageTypeList: List<MessageType>
    ): MessageType? {
        if (!availableSay || selectableMessageTypeList.isEmpty()) return null
        val selectableMessageTypeCdefList = selectableMessageTypeList.map { it.toCdef() }

        defaultMessageTypeOrder.forEach { cdefMessageType ->
            if (selectableMessageTypeCdefList.contains(cdefMessageType)) return MessageType(cdefMessageType)
        }
        return null
    }
}