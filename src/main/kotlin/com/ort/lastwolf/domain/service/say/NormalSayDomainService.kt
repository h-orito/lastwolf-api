package com.ort.lastwolf.domain.service.say

import com.ort.dbflute.allcommon.CDef
import com.ort.lastwolf.domain.model.village.Village
import com.ort.lastwolf.domain.model.village.participant.VillageParticipant
import com.ort.lastwolf.fw.exception.LastwolfBusinessException
import org.springframework.stereotype.Service

@Service
class NormalSayDomainService {

    fun isViewable(village: Village, participant: VillageParticipant?): Boolean {
        return true
    }

    fun isSayable(village: Village, participant: VillageParticipant): Boolean {
        // 参加者として可能か
        if (!participant.isSayableNormalSay(village.status.toCdef() == CDef.VillageStatus.エピローグ)) return false
        // 村として可能か
        return village.isSayableNormalSay()
    }

    fun assertSay(village: Village, participant: VillageParticipant) {
        if (!isSayable(village, participant)) throw LastwolfBusinessException("発言できません")
    }
}