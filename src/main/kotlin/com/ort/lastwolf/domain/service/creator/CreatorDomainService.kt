package com.ort.lastwolf.domain.service.creator

import com.ort.lastwolf.domain.model.myself.participant.VillageCreatorSituation
import com.ort.lastwolf.domain.model.player.Player
import com.ort.lastwolf.domain.model.village.Village
import com.ort.lastwolf.domain.service.daychange.RollCallingDomainService
import com.ort.lastwolf.fw.exception.LastwolfBusinessException
import org.springframework.stereotype.Service

@Service
class CreatorDomainService(
    private val rollCallingDomainService: RollCallingDomainService
) {

    fun convertToSituation(
        village: Village,
        player: Player?
    ): VillageCreatorSituation {
        return VillageCreatorSituation(
            isAvailableCreatorSetting = isAvailableCreatorSetting(village, player),
            isAvailableCreatorSay = isAvailableCreatorSay(village, player),
            isAvailableStartVillage = isAvailableStartVillage(village, player),
            isAvailableCancelVillage = isAvailableCancelVillage(village, player),
            isAvailableKick = isAvailableKick(village, player),
            isAvailableModifySetting = isAvailableModifySetting(village, player),
            isAvailableStartRollCall = rollCallingDomainService.canStartRollCall(village, player),
            isAvailableCancelRollCall = rollCallingDomainService.canCancelRollCall(village, player),
            isViewableSpoiler = isViewableSpoiler(village, player)
        )
    }

    fun assertStartVillage(village: Village, player: Player) {
        if (!isAvailableStartVillage(village, player)) throw LastwolfBusinessException("村を開始できません")
    }


    // ===================================================================================
    //                                                                        Assist Logic
    //                                                                        ============
    private fun isAvailableCreatorSetting(
        village: Village,
        player: Player?
    ): Boolean {
        player ?: return false
        if (village.status.isFinished()) return false
        // 管理者か村建てならok
        if (village.creatorPlayer.id == player.id) return true
        if (village.dummyParticipant()!!.player.id == player.id) return true
        return false
    }

    private fun isViewableSpoiler(village: Village, player: Player?): Boolean {
        player ?: return false
        // GMか村建てならok
        if (village.isGameMaster(player)) return true
        if (village.dummyParticipant()!!.player.id == player.id) return true
        return false
    }

    private fun isAvailableCreatorSay(
        village: Village,
        player: Player?
    ): Boolean {
        if (!this.isAvailableCreatorSetting(village, player)) return false
        return true
    }

    private fun isAvailableStartVillage(
        village: Village,
        player: Player?
    ): Boolean {
        if (!this.isAvailableCreatorSetting(village, player)) return false
        return village.isAvailableStart()
    }

    private fun isAvailableCancelVillage(
        village: Village,
        player: Player?
    ): Boolean {
        if (!this.isAvailableCreatorSetting(village, player)) return false
        return village.status.isRecruiting() // プロローグ中のみ可能
    }

    private fun isAvailableKick(
        village: Village,
        player: Player?
    ): Boolean {
        if (!this.isAvailableCreatorSetting(village, player)) return false
        return village.status.isRecruiting() // プロローグ中のみ可能
    }

    private fun isAvailableModifySetting(
        village: Village,
        player: Player?
    ): Boolean {
        if (!this.isAvailableCreatorSetting(village, player)) return false
        return village.status.isRecruiting() // プロローグ中のみ可能
    }
}