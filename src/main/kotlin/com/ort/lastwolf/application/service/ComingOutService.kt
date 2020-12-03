package com.ort.lastwolf.application.service

import com.ort.lastwolf.domain.model.skill.Skill
import com.ort.lastwolf.domain.model.village.participant.coming_out.ComingOut
import com.ort.lastwolf.infrastructure.datasource.coming_out.ComingOutDataSource
import org.springframework.stereotype.Service

@Service
class ComingOutService(
    private val comingOutDataSource: ComingOutDataSource
) {

    fun registerComingOut(
        participantId: Int,
        skill: Skill
    ) = comingOutDataSource.register(
        participantId,
        ComingOut(skill)
    )

    fun deleteComingOut(participantId: Int) = comingOutDataSource.delete(participantId)
}