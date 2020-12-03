package com.ort.lastwolf.infrastructure.datasource.ability

import com.ort.dbflute.allcommon.CDef
import com.ort.dbflute.exbhv.AbilityBhv
import com.ort.dbflute.exentity.Ability
import com.ort.lastwolf.domain.model.village.Village
import com.ort.lastwolf.domain.model.village.ability.VillageAbilities
import com.ort.lastwolf.domain.model.village.ability.VillageAbility
import com.ort.lastwolf.infrastructure.datasource.firebase.FirebaseDataSource
import org.springframework.stereotype.Repository

@Repository
class AbilityDataSource(
    val abilityBhv: AbilityBhv,
    val firebaseDataSource: FirebaseDataSource
) {

    // ===================================================================================
    //                                                                              Select
    //                                                                              ======
    fun findAbilities(villageId: Int): VillageAbilities {
        val abilityList = abilityBhv.selectList {
            it.query().queryVillageDay().setVillageId_Equal(villageId)
        }
        return VillageAbilities(abilityList.map { convertToAbilityToVillageAbility(it) })
    }

    // ===================================================================================
    //                                                                              Update
    //                                                                              ======
    fun updateAbility(village: Village, villageAbility: VillageAbility) {
        deleteAbility(villageAbility)
        insertAbility(villageAbility)
        firebaseDataSource.registerSituationLatest(village, villageAbility)
    }

    fun updateDifference(village: Village, before: VillageAbilities, after: VillageAbilities) {
        after.list.drop(before.list.size).forEach {
            insertAbility(it)
            firebaseDataSource.registerSituationLatest(village, it)
        }
    }

    // ===================================================================================
    //                                                                        Assist Logic
    //                                                                        ============
    private fun deleteAbility(villageAbility: VillageAbility) {
        abilityBhv.queryDelete {
            it.query().setVillageDayId_Equal(villageAbility.villageDayId)
            if (villageAbility.abilityType.toCdef() != CDef.AbilityType.襲撃) {
                it.query().setVillagePlayerId_Equal(villageAbility.myselfId)
            }
            it.query().setAbilityTypeCode_Equal_AsAbilityType(villageAbility.abilityType.toCdef())
        }
    }

    private fun insertAbility(villageAbility: VillageAbility) {
        val ability = Ability()
        ability.villageDayId = villageAbility.villageDayId
        ability.villagePlayerId = villageAbility.myselfId
        ability.targetVillagePlayerId = villageAbility.targetId
        ability.abilityTypeCodeAsAbilityType = villageAbility.abilityType.toCdef()
        abilityBhv.insert(ability)
    }

    // ===================================================================================
    //                                                                             Mapping
    //                                                                             =======
    private fun convertToAbilityToVillageAbility(ability: Ability): VillageAbility {
        return VillageAbility(
            villageDayId = ability.villageDayId,
            myselfId = ability.villagePlayerId,
            targetId = ability.targetVillagePlayerId,
            abilityType = com.ort.lastwolf.domain.model.ability.AbilityType(ability.abilityTypeCodeAsAbilityType)
        )
    }
}