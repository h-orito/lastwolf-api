package com.ort.lastwolf.infrastructure.datasource.coming_out

import com.ort.dbflute.exbhv.ComingOutBhv
import com.ort.dbflute.exentity.ComingOut
import org.springframework.stereotype.Repository

@Repository
class ComingOutDataSource(
    private val comingOutBhv: ComingOutBhv
) {

    fun register(
        villageParticipantId: Int,
        comingOut: com.ort.lastwolf.domain.model.village.participant.coming_out.ComingOut
    ) {
        delete(villageParticipantId)
        val entity = ComingOut()
        entity.villagePlayerId = villageParticipantId
        entity.skillCodeAsSkill = comingOut.skill.toCdef()
        comingOutBhv.insert(entity)
    }

    fun delete(villageParticipantId: Int) {
        comingOutBhv.queryDelete {
            it.query().setVillagePlayerId_Equal(villageParticipantId)
        }
    }
}