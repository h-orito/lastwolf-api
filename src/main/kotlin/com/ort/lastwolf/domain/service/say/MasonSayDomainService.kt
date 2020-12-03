package com.ort.lastwolf.domain.service.say

import com.ort.lastwolf.domain.model.village.Village
import com.ort.lastwolf.domain.model.village.participant.VillageParticipant
import com.ort.lastwolf.fw.exception.LastwolfBusinessException
import org.springframework.stereotype.Service

@Service
class MasonSayDomainService {

    fun isViewable(village: Village, participant: VillageParticipant?): Boolean {
        // いずれかを満たせばok
        // 村として可能か
        if (village.isViewableMasonSay()) return true
        // 参加者として可能か
        participant ?: return false
        return participant.isViewableMasonSay()
    }

    fun isSayable(village: Village, participant: VillageParticipant): Boolean {
        // 参加者として可能か
        if (!participant.isSayableMasonSay()) return false
        // 村として可能か
        return village.isSayableMasonSay()
    }

    fun assertSay(village: Village, participant: VillageParticipant) {
        if (!isSayable(village, participant)) throw LastwolfBusinessException("発言できません")
    }
}
