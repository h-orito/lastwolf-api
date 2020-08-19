package com.ort.lastwolf.domain.service.say

import com.ort.lastwolf.domain.model.village.Village
import com.ort.lastwolf.domain.model.village.participant.VillageParticipant
import com.ort.lastwolf.fw.exception.LastwolfBusinessException
import org.springframework.stereotype.Service

@Service
class SpectateSayDomainService {

    fun isViewable(village: Village, participant: VillageParticipant?, day: Int): Boolean {
        // いずれかを満たせば開放
        // 村として可能か
        if (village.isViewableSpectateSay()) return true
        // 0日目の発言か
        if (day == 0) return true
        // 参加者として可能か
        participant ?: return false
        return participant.isViewableSpectateSay()
    }

    fun isSayable(village: Village, participant: VillageParticipant): Boolean {
        // 参加者として可能か
        if (!participant.isSayableSpectateSay()) return false
        // 村として可能か
        return village.isSayableSpectateSay()
    }

    fun assertSay(village: Village, participant: VillageParticipant) {
        if (!isSayable(village, participant)) throw LastwolfBusinessException("発言できません")
    }
}
