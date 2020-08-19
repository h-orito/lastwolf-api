package com.ort.lastwolf.dummy

import com.ort.dbflute.exbhv.PlayerBhv
import com.ort.lastwolf.application.coordinator.VillageCoordinator
import com.ort.lastwolf.application.service.VillageService
import com.ort.lastwolf.domain.model.village.Village
import com.ort.lastwolf.fw.security.LastwolfUser
import org.springframework.stereotype.Service

@Service
class DummyVillageService(
    private val playerBhv: PlayerBhv,
    private val villageCoordinator: VillageCoordinator,
    private val villageService: VillageService
) {

    // G16編成、沈黙時間なし
    fun createG16PrologueVillage(): Village {
        val player = playerBhv.selectByPK(2).get()
        val user = LastwolfUser(player.uid, player.authorityCodeAsAuthority)
        val villageId = villageCoordinator.registerVillage(
            DummyDomainModelCreator.createDummyVillageRegisterParam(),
            user
        )
        return villageService.findVillage(villageId)
    }
}
