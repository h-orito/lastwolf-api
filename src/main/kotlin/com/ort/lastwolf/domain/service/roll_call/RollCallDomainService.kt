package com.ort.lastwolf.domain.service.roll_call

import com.ort.lastwolf.domain.model.myself.participant.VillageRollCallSituation
import com.ort.lastwolf.domain.model.village.Village
import com.ort.lastwolf.domain.model.village.participant.VillageParticipant
import com.ort.lastwolf.fw.exception.LastwolfBusinessException
import org.springframework.stereotype.Service

@Service
class RollCallDomainService {

    fun convertToSituation(village: Village, participant: VillageParticipant?): VillageRollCallSituation {
        return VillageRollCallSituation(
            isAvailableRollCall = isAvailableRollCall(village, participant),
            doneRollCall = participant?.doneRollCall ?: false
        )
    }

    fun assertRollCall(village: Village, participant: VillageParticipant?) {
        if (!isAvailableRollCall(village, participant)) throw LastwolfBusinessException("点呼できません")
    }

    // ===================================================================================
    //                                                                        Assist Logic
    //                                                                        ============
    private fun isAvailableRollCall(village: Village, participant: VillageParticipant?): Boolean {
        if (!village.isAvailableRollCall()) return false
        participant ?: return false
        return participant.isAvailableRollCall()
    }
}