package com.ort.wolf4busy.domain.model.message

import com.ort.wolf4busy.domain.model.village.Village
import com.ort.wolf4busy.domain.model.village.participant.VillageParticipant

object MonologueSay {

    fun isViewable(village: Village): Boolean {
        // 終了していたら全て見られる
        return village.status.isCompleted()
    }

    fun isSayable(village: Village, participant: VillageParticipant): Boolean {
        return true
    }
}
