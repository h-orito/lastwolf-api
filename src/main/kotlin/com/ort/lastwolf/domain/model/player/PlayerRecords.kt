package com.ort.lastwolf.domain.model.player

import com.ort.dbflute.allcommon.CDef
import com.ort.lastwolf.domain.model.camp.Camp
import com.ort.lastwolf.domain.model.player.record.CampRecord
import com.ort.lastwolf.domain.model.player.record.ParticipateVillage
import com.ort.lastwolf.domain.model.player.record.Record
import com.ort.lastwolf.domain.model.player.record.SkillRecord
import com.ort.lastwolf.domain.model.skill.Skill
import com.ort.lastwolf.domain.model.village.Villages

data class PlayerRecords(
    val player: Player,
    val wholeRecord: Record,
    val campRecordList: List<CampRecord>,
    val skillRecordList: List<SkillRecord>,
    val participateVillageList: List<ParticipateVillage>
) {
    constructor(
        player: Player,
        villages: Villages
    ) : this(
        player = player,
        wholeRecord = Record(player, villages),
        campRecordList = createCampRecordList(player, villages),
        skillRecordList = createSkillRecordList(player, villages),
        participateVillageList = villages.list.sortedByDescending { it.id }.map { ParticipateVillage(player, it) }
    )

    companion object {
        private fun createCampRecordList(player: Player, villages: Villages): List<CampRecord> {
            return CDef.Camp.listAll().sortedBy { Integer.parseInt(it.order()) }.map { camp ->
                val campVillageList = villages.list.filter { village ->
                    val myCampCode = village.participants.list.firstOrNull {
                        !it.isGone && it.player.id == player.id
                    }?.skill?.winJudgeCamp?.code
                    myCampCode == camp.code()
                }
                CampRecord(Camp(camp), player, Villages(campVillageList))
            }
        }

        private fun createSkillRecordList(player: Player, villages: Villages): List<SkillRecord> {
            return CDef.Skill.listAll().sortedBy { Integer.parseInt(it.order()) }
                .filterNot { CDef.Skill.listOfSomeoneSkill().contains(it) }
                .map { skill ->
                    val skillVillageList = villages.list.filter { village ->
                        val mySkillCode = village.participants.list.firstOrNull {
                            !it.isGone && it.player.id == player.id
                        }?.skill?.code
                        mySkillCode == skill.code()
                    }
                    SkillRecord(Skill(skill), player, Villages(skillVillageList))
                }
        }
    }
}