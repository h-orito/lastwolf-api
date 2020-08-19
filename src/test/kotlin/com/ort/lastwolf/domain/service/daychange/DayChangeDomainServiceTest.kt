package com.ort.lastwolf.domain.service.daychange

import com.ort.dbflute.allcommon.CDef
import com.ort.lastwolf.LastwolfTest
import com.ort.lastwolf.domain.model.daychange.DayChange
import com.ort.lastwolf.domain.model.message.Messages
import com.ort.lastwolf.domain.model.village.VillageStatus
import com.ort.lastwolf.dummy.DummyDomainModelCreator
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest
class DayChangeDomainServiceTest : LastwolfTest() {

    @Autowired
    lateinit var dayChangeDomainService: DayChangeDomainService

    @Test
    fun test_process_終了後() {
        // ## Arrange ##
        val village = DummyDomainModelCreator.createDummyVillage().copy(
            status = VillageStatus(CDef.VillageStatus.終了)
        )
        val dayChange = DayChange(
            village = village,
            votes = DummyDomainModelCreator.createDummyVillageVotes(),
            abilities = DummyDomainModelCreator.createDummyVillageAbilities(),
            players = DummyDomainModelCreator.createDummyPlayers()
        )
        val todayMessages = Messages(listOf())
        val charas = DummyDomainModelCreator.createDummyCharas()

        // ## Act ##
        val afterDayChange = dayChangeDomainService.process(dayChange, todayMessages, charas)

        // ## Assert ##
        assertThat(afterDayChange.isChange).`as`("終了後は何もしない").isFalse()
    }
}