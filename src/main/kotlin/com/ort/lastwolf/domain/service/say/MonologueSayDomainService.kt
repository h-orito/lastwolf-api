package com.ort.lastwolf.domain.service.say

import com.ort.lastwolf.domain.model.village.Village
import com.ort.lastwolf.domain.model.village.participant.VillageParticipant
import com.ort.lastwolf.fw.exception.LastwolfBusinessException
import org.springframework.stereotype.Service

@Service
class MonologueSayDomainService {

    fun isViewable(village: Village, participant: VillageParticipant?): Boolean {
        // 村として可能か
        return village.isViewableMonologueSay()
    }

    fun isSayable(village: Village, participant: VillageParticipant): Boolean {
        // 参加者として可能か
        if (!participant.isSayableMonologueSay()) return false
        // 村として可能か
        return village.isSayableMonologueSay()
    }

    fun assertSay(village: Village, participant: VillageParticipant) {
        if (!isSayable(village, participant)) throw LastwolfBusinessException("発言できません")
    }
}
