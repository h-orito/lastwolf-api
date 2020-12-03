package com.ort.lastwolf.api.view.player

import com.ort.lastwolf.domain.model.player.PlayerRecords
import com.ort.lastwolf.domain.model.player.record.CampRecord
import com.ort.lastwolf.domain.model.player.record.Record
import com.ort.lastwolf.domain.model.player.record.SkillRecord

data class PlayerRecordsView(
    val player: PlayerView,
    val wholeRecord: Record,
    val campRecordList: List<CampRecord>,
    val skillRecordList: List<SkillRecord>,
    val participateVillageList: List<ParticipateVillageView>
) {
    constructor(
        playerRecords: PlayerRecords
    ) : this(
        player = PlayerView(playerRecords.player),
        wholeRecord = playerRecords.wholeRecord,
        campRecordList = playerRecords.campRecordList,
        skillRecordList = playerRecords.skillRecordList,
        participateVillageList = playerRecords.participateVillageList.map { participateVillage ->
            ParticipateVillageView(
                participateVillage.village,
                participateVillage.participant
            )
        }
    )
}
