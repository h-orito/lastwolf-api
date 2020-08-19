package com.ort.lastwolf.domain.model.village.setting

import com.ort.dbflute.allcommon.CDef
import com.ort.lastwolf.domain.model.message.Message
import com.ort.lastwolf.domain.model.message.MessageContent
import com.ort.lastwolf.domain.model.message.MessageType
import com.ort.lastwolf.fw.exception.LastwolfBusinessException

data class VillageMessageRestrict(
    val type: MessageType,
    val count: Int,
    val length: Int,
    val line: Int = MessageContent.lineMax
) {
    fun assertSay(messageContent: MessageContent, cdefVillageStatus: CDef.VillageStatus, latestDayMessageList: List<Message>) {
        // 回数
        if (remainingCount(cdefVillageStatus, latestDayMessageList) <= 0) throw LastwolfBusinessException("発言残り回数が0回です")
        // 文字数
        messageContent.assertMessageLength(length)
    }

    fun remainingCount(cdefVillageStatus: CDef.VillageStatus, latestDayMessageList: List<Message>): Int {
        if (cdefVillageStatus == CDef.VillageStatus.プロローグ || cdefVillageStatus == CDef.VillageStatus.エピローグ) {
            return count // プロローグ、エピローグでは制限なし状態にする
        }
        val alreadySayCount = latestDayMessageList.count { it.content.type.toCdef() == type.toCdef() }
        return count - alreadySayCount
    }
}
