package com.ort.lastwolf.domain.model.village.setting

import com.ort.dbflute.allcommon.CDef
import com.ort.lastwolf.domain.model.message.MessageType

data class VillageMessageRestricts(
    val existRestricts: Boolean = true,
    val restrictList: List<VillageMessageRestrict> = listOf(
        VillageMessageRestrict(
            type = MessageType(CDef.MessageType.通常発言),
            count = 20,
            length = 200
        ),
        VillageMessageRestrict(
            type = MessageType(CDef.MessageType.人狼の囁き),
            count = 40,
            length = 200
        ),
        VillageMessageRestrict(
            type = MessageType(CDef.MessageType.独り言),
            count = 100,
            length = 200
        ),
        VillageMessageRestrict(
            type = MessageType(CDef.MessageType.死者の呻き),
            count = 40,
            length = 200
        )
    )
) {

    fun restrict(cdefMessageType: CDef.MessageType): VillageMessageRestrict? {
        return if (!existRestricts) null
        else restrictList.find { it.type.toCdef() == cdefMessageType }
    }

    fun existsDifference(messageRestrict: VillageMessageRestricts): Boolean {
        if (existRestricts != messageRestrict.existRestricts) return true
        if (restrictList.size != messageRestrict.restrictList.size) return true
        return restrictList.any { restrict1 ->
            messageRestrict.restrictList.none { restrict2 ->
                restrict1.type.code == restrict2.type.code
                    && restrict1.count == restrict2.count
                    && restrict1.length == restrict2.length
            }
        }
    }
}