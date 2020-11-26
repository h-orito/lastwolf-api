package com.ort.lastwolf.application.service

import com.ort.lastwolf.domain.model.village.Village
import com.ort.lastwolf.domain.model.village.ability.VillageAbilities
import com.ort.lastwolf.domain.model.village.ability.VillageAbility
import com.ort.lastwolf.infrastructure.datasource.ability.AbilityDataSource
import org.springframework.stereotype.Service

@Service
class AbilityService(
    private val abilityDataSource: AbilityDataSource
) {

    fun findVillageAbilities(villageId: Int): VillageAbilities = abilityDataSource.findAbilities(villageId)

    fun updateAbility(
        village: Village,
        villageAbility: VillageAbility
    ) {
        abilityDataSource.updateAbility(village, villageAbility)
    }

    fun updateDifference(village: Village, before: VillageAbilities, after: VillageAbilities) {
        abilityDataSource.updateDifference(village, before, after)
    }
}
