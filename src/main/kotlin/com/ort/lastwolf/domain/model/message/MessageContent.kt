package com.ort.lastwolf.domain.model.message

import com.google.firebase.database.PropertyName
import com.ort.dbflute.allcommon.CDef
import com.ort.lastwolf.fw.exception.LastwolfBadRequestException

data class MessageContent(
    val type: MessageType,
    val text: String,
    @get:PropertyName("strong") // firebase用
    val isStrong: Boolean
) {
    companion object {

        const val defaultLengthMax = 200

        operator fun invoke(
            messageType: String,
            text: String,
            isStrong: Boolean
        ): MessageContent {
            val cdefMessageType = checkNotNull(CDef.MessageType.codeOf(messageType))
            return MessageContent(
                type = MessageType(cdefMessageType),
                text = removeSurrogate(text.trim()),
                isStrong = isStrong
            )
        }

        /**
         * 絵文字を除く文字列を返す
         * @param text
         * @return 4byte文字を除いた文字列
         */
        private fun removeSurrogate(text: String): String {
            return text.chunked(1).filter { c ->
                !c.matches("[\\uD800-\\uDFFF]".toRegex())
            }.joinToString(separator = "")
        }
    }

    fun assertMessageLength() {
        // 文字数
        if (text.isEmpty()) throw LastwolfBadRequestException("発言内容がありません")
        if (type.toCdef() == CDef.MessageType.村建て発言) {
            if (400 < text.replace("\r\n", "").replace("\n", "").length)
                throw LastwolfBadRequestException("文字数オーバーです")
        } else {
            if (defaultLengthMax < text.length) throw LastwolfBadRequestException("文字数オーバーです")
        }
    }
}