package com.ort.lastwolf.domain.model.message

import com.ort.dbflute.allcommon.CDef
import com.ort.lastwolf.domain.model.village.participant.VillageParticipant

data class MessageQuery(
    val from: Long?,
    val pageSize: Int?,
    val pageNum: Int?,
    val keyword: String?,
    val participant: VillageParticipant?,
    val messageTypeList: List<CDef.MessageType>,
    val participantIdList: List<Int>?,
    val includeMonologue: Boolean,
    val includePrivateAbility: Boolean
) {
    constructor(
        messageTypeList: List<CDef.MessageType>
    ) : this(
        from = null,
        pageSize = null,
        pageNum = null,
        keyword = null,
        participant = null,
        messageTypeList = messageTypeList,
        participantIdList = null,
        includeMonologue = false,
        includePrivateAbility = false
    )

    companion object {
        val personalPrivateAbilityList = listOf(CDef.MessageType.個別能力行使結果)
    }

    fun isPaging(): Boolean = pageSize != null && pageNum != null
}