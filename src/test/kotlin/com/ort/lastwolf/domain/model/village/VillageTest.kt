package com.ort.lastwolf.domain.model.village

import com.ort.lastwolf.DummyData
import com.ort.lastwolf.LastwolfTest
import com.ort.lastwolf.domain.model.village.participant.VillageParticipants
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.transaction.annotation.Transactional

@RunWith(SpringRunner::class)
@SpringBootTest
@Transactional
class VillageTest : LastwolfTest() {


    @Test
    fun test_createOrganizationMessage() {
        // ## Arrange ##
        var village = DummyData.prologueVillage()
        village = village.copy(
            participants = VillageParticipants(
                count = 10,
                list = listOf(village.participants.list.first()) + (1..9).map { DummyData.villageParticipant() }
            )
        )

        // ## Act ##
        val message = village.createOrganizationMessage()

        // ## Assert ##
        assertThat(message.content.text).startsWith("この村には")
        assertThat(message.content.text).endsWith("いるようだ。")
    }
}