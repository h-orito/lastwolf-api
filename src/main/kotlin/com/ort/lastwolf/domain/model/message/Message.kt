package com.ort.lastwolf.domain.model.message

import com.ort.dbflute.allcommon.CDef
import com.ort.lastwolf.domain.model.village.participant.VillageParticipant
import com.ort.lastwolf.fw.LastwolfDateUtil

data class Message(
    val fromParticipantId: Int?,
    val time: MessageTime,
    val content: MessageContent
) {

    companion object {

        fun createSayMessage(
            from: VillageParticipant,
            villageDayId: Int,
            messageContent: MessageContent
        ): Message = Message(
            fromParticipantId = from.id,
            time = MessageTime(
                villageDayId = villageDayId,
                datetime = LastwolfDateUtil.currentLocalDateTime(), // dummy
                unixTimeMilli = 0L // dummy
            ),
            content = messageContent
        )

        fun createPublicSystemMessage(text: String, villageDayId: Int, isStrong: Boolean = false): Message =
            createSystemMessage(
                messageType = MessageType(CDef.MessageType.公開システムメッセージ),
                text = text,
                villageDayId = villageDayId,
                isStrong = isStrong
            )

        fun createPrivateSystemMessage(text: String, villageDayId: Int, isStrong: Boolean = false): Message =
            createSystemMessage(
                messageType = MessageType(CDef.MessageType.非公開システムメッセージ),
                text = text,
                villageDayId = villageDayId,
                isStrong = isStrong
            )

        fun createPrivateAbilityMessage(
            text: String,
            villageDayId: Int,
            participant: VillageParticipant,
            isStrong: Boolean = false
        ): Message =
            createSystemMessage(
                messageType = MessageType(CDef.MessageType.個別能力行使結果),
                text = text,
                villageDayId = villageDayId,
                from = participant,
                isStrong = isStrong
            )

        fun createPsychicPrivateMessage(text: String, villageDayId: Int, isStrong: Boolean = false): Message =
            createSystemMessage(
                messageType = MessageType(CDef.MessageType.白黒霊視結果),
                text = text,
                villageDayId = villageDayId,
                isStrong = isStrong
            )

        fun createAttackPrivateMessage(text: String, villageDayId: Int, isStrong: Boolean = false): Message =
            createSystemMessage(
                messageType = MessageType(CDef.MessageType.襲撃結果),
                text = text,
                villageDayId = villageDayId,
                isStrong = isStrong
            )

        fun createMasonPrivateMessage(text: String, villageDayId: Int, isStrong: Boolean = false): Message =
            createSystemMessage(
                messageType = MessageType(CDef.MessageType.共有相互確認メッセージ),
                text = text,
                villageDayId = villageDayId,
                isStrong = isStrong
            )

        fun createCreatorSayMessage(text: String, villageDayId: Int, isStrong: Boolean = false): Message =
            createSystemMessage(
                messageType = MessageType(CDef.MessageType.村建て発言),
                text = text,
                villageDayId = villageDayId,
                isStrong = isStrong
            )

        // ===================================================================================
        //                                                                        Assist Logic
        //                                                                        ============
        private fun createSystemMessage(
            messageType: MessageType,
            text: String,
            villageDayId: Int,
            from: VillageParticipant? = null,
            isStrong: Boolean
        ): Message = Message(
            fromParticipantId = from?.id,
            time = MessageTime(
                villageDayId = villageDayId,
                datetime = LastwolfDateUtil.currentLocalDateTime(), // dummy
                unixTimeMilli = 0L // dummy
            ),
            content = MessageContent(
                type = messageType,
                text = text,
                isStrong = isStrong
            )
        )
    }
}