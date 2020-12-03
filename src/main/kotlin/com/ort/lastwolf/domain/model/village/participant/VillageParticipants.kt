package com.ort.lastwolf.domain.model.village.participant

import com.ort.dbflute.allcommon.CDef
import com.ort.lastwolf.domain.model.camp.Camp
import com.ort.lastwolf.domain.model.skill.Skill
import com.ort.lastwolf.domain.model.skill.SkillRequest
import com.ort.lastwolf.domain.model.village.VillageDay

data class VillageParticipants(
    val count: Int, // 退村した人は含まない
    val list: List<VillageParticipant> = listOf()
) {
    fun assignSkill(villageParticipantId: Int, skill: Skill): VillageParticipants {
        return this.copy(
            list = this.list.map {
                if (it.id == villageParticipantId) it.assignSkill(skill)
                else it.copy()
            }
        )
    }

    fun addParticipant(charaId: Int, playerId: Int, skillRequest: SkillRequest): VillageParticipants {
        return this.copy(
            count = count + 1,
            list = list + VillageParticipant.createForRegister(
                charaId = charaId,
                playerId = playerId,
                skillRequest = skillRequest
            )
        )
    }

    fun changeSkillRequest(participantId: Int, first: CDef.Skill, second: CDef.Skill): VillageParticipants {
        return this.copy(
            list = this.list.map {
                if (it.id == participantId) it.changeSkillRequest(first, second)
                else it.copy()
            }
        )
    }

    // 退村
    fun leave(participantId: Int): VillageParticipants {
        return this.copy(
            count = this.count - 1,
            list = this.list.map {
                if (it.id == participantId) it.gone() else it.copy()
            }
        )
    }

    // 突然死
    fun suddenlyDeath(participantId: Int, villageDay: VillageDay): VillageParticipants {
        return this.copy(
            list = this.list.map {
                if (it.id == participantId) it.suddenlyDeath(villageDay) else it.copy()
            }
        )
    }

    // 処刑
    fun execute(participantId: Int, villageDay: VillageDay): VillageParticipants {
        return this.copy(
            list = this.list.map {
                if (it.id == participantId) it.execute(villageDay) else it.copy()
            }
        )
    }

    // 襲撃
    fun attack(participantId: Int, villageDay: VillageDay): VillageParticipants {
        return this.copy(
            list = this.list.map {
                if (it.id == participantId) it.attack(villageDay) else it.copy()
            }
        )
    }

    // 呪殺
    fun divineKill(participantId: Int, villageDay: VillageDay): VillageParticipants {
        return this.copy(
            list = this.list.map {
                if (it.id == participantId) it.divineKill(villageDay) else it.copy()
            }
        )
    }

    // 点呼
    fun rollCall(participantId: Int, done: Boolean): VillageParticipants {
        return this.copy(
            list = this.list.map {
                if (it.id == participantId) it.rollCall(done) else it.copy()
            }
        )
    }

    // 勝敗設定
    fun winLose(winCamp: Camp?): VillageParticipants = this.copy(list = this.list.map { it.winLose(winCamp) })

    fun find(id: Int): VillageParticipant? = list.firstOrNull { it.id == id }

    fun first(id: Int): VillageParticipant = list.firstOrNull { it.id == id } ?: throw IllegalStateException("not found member")

    fun findByPlayerId(playerId: Int): VillageParticipant? = list.firstOrNull { it.player.id == playerId && !it.isGone }

    fun firstByPlayerId(playerId: Int): VillageParticipant = findByPlayerId(playerId) ?: throw IllegalStateException("not found member")

    fun filterAlive(): VillageParticipants {
        val aliveMembers = list.filter { it.isAlive() }
        return VillageParticipants(
            count = aliveMembers.size,
            list = aliveMembers
        )
    }

    fun findRandom(predicate: (VillageParticipant) -> Boolean): VillageParticipant? {
        return list.filter { predicate(it) }.shuffled().firstOrNull()
    }

    fun existsDifference(participant: VillageParticipants): Boolean {
        if (count != participant.count) return true
        if (list.size != participant.list.size) return true
        return list.any { member1 ->
            participant.list.none { member2 -> !member1.existsDifference(member2) }
        }
    }
}