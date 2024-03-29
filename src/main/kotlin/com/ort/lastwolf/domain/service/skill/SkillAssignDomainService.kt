package com.ort.lastwolf.domain.service.skill

import com.ort.dbflute.allcommon.CDef
import com.ort.lastwolf.domain.model.daychange.DayChange
import com.ort.lastwolf.domain.model.skill.Skill
import com.ort.lastwolf.domain.model.village.participant.VillageParticipant
import com.ort.lastwolf.domain.model.village.participant.VillageParticipants
import org.springframework.stereotype.Service

@Service
class SkillAssignDomainService {

    fun assign(
        dayChange: DayChange
    ): DayChange {
        val village = dayChange.village
        val skillPersonCountMap = village.setting.organizations.mapToSkillCount(village.participants.count)

        // ダミー配役
        var participants = assignDummy(
            village.participants,
            skillPersonCountMap,
            village.dummyParticipant()!!,
            village.setting.rules.availableDummySkill
        )

        // 第1希望で役職希望した人を割り当て
        participants = assignFirstSpecifyRequest(participants, skillPersonCountMap)

        // 第1希望で範囲指定希望した人を割り当て
        participants = assignFirstRangeRequest(participants, skillPersonCountMap)

        // 第2希望で役職希望した人を割り当て
        participants = assignSecondSpecifyRequest(participants, skillPersonCountMap)

        // 第2希望で範囲指定希望した人を割り当て
        participants = assignSecondRangeRequest(participants, skillPersonCountMap)

        // ここまでで割当たらなかった人に割り当て
        participants = assignOther(participants, skillPersonCountMap)

        return dayChange.copy(
            village = village.assignSkill(participants)
        )
    }

    // ===================================================================================
    //                                                                        Assist Logic
    //                                                                        ============
    private fun assignDummy(
        participants: VillageParticipants,
        skillPersonCountMap: Map<CDef.Skill, Int>,
        dummyChara: VillageParticipant,
        availableDummySkill: Boolean
    ): VillageParticipants {
        // 役欠け無しの場合は村人固定
        if (!availableDummySkill) return participants.assignSkill(dummyChara.id, Skill(CDef.Skill.村人))
        // ありの場合は2日目に襲撃死できる役職を割り当て
        val availableSkillList = mutableListOf<CDef.Skill>()
        for ((cdefSkill, capacity) in skillPersonCountMap.entries) {
            if (cdefSkill.isNoDeadByAttack ||
                cdefSkill.isNotSelectableAttack ||
                cdefSkill.isForceDoubleSuicide
            ) continue
            repeat(capacity) {
                availableSkillList.add(cdefSkill)
            }
        }
        val assignedSkill = availableSkillList.shuffled().first()
        return participants.assignSkill(dummyChara.id, Skill(assignedSkill))
    }

    private fun assignFirstSpecifyRequest(
        participants: VillageParticipants,
        skillPersonCountMap: Map<CDef.Skill, Int>
    ): VillageParticipants {
        var changedParticipants = participants.copy()
        for ((cdefSkill, capacity) in skillPersonCountMap.entries) {
            // この役職を希望していてまだ割り当たってない人
            val requestPlayerList = changedParticipants.list.filter {
                it.skillRequest.first.code == cdefSkill.code() && it.skill == null
            }
            // 希望している人がいない
            if (requestPlayerList.isEmpty()) continue

            // 空いている枠数
            val left = capacity - changedParticipants.list.count { it.skill?.code == cdefSkill.code() }
            // もう空きがない
            if (left <= 0) continue

            // 空いている人数まで割り当てる
            var count = 0
            for (requestPlayer in requestPlayerList.shuffled()) {
                if (count >= left) break
                changedParticipants = changedParticipants.assignSkill(requestPlayer.id, Skill(cdefSkill))
                count++
            }
        }
        return changedParticipants
    }

    private fun assignSecondSpecifyRequest(
        participants: VillageParticipants,
        skillPersonCountMap: Map<CDef.Skill, Int>
    ): VillageParticipants {
        var changedParticipants = participants.copy()
        for ((cdefSkill, capacity) in skillPersonCountMap.entries) {
            // この役職を希望していてまだ割り当たってない人
            val requestPlayerList = changedParticipants.list.filter {
                it.skillRequest.second.code == cdefSkill.code() && it.skill == null
            }
            // 希望している人がいない
            if (requestPlayerList.isEmpty()) continue

            // 空いている枠数
            val left = capacity - changedParticipants.list.count { it.skill?.code == cdefSkill.code() }
            // もう空きがない
            if (left <= 0) continue

            // 空いている人数まで割り当てる
            var count = 0
            for (requestPlayer in requestPlayerList.shuffled()) {
                if (count >= left) break
                changedParticipants = changedParticipants.assignSkill(requestPlayer.id, Skill(cdefSkill))
                count++
            }
        }
        return changedParticipants
    }

    private fun assignFirstRangeRequest(
        participants: VillageParticipants,
        skillPersonCountMap: Map<CDef.Skill, Int>
    ): VillageParticipants {
        var changedParticipants = participants.copy()
        // 範囲指定している人
        changedParticipants.list
            .filter {
                it.skill == null && CDef.Skill.listOfSomeoneSkill()
                    .contains(it.skillRequest.first.toCdef()) && it.skillRequest.first.toCdef() != CDef.Skill.おまかせ
            }
            .shuffled()
            .forEach {
                // 役職候補
                val candidateSkillList = getCandidateSkillList(it.skillRequest.first.toCdef()).sorted()
                for (skill in candidateSkillList) {
                    val capacity = skillPersonCountMap[skill] ?: 0
                    val left = capacity - changedParticipants.list.count { member -> member.skill?.toCdef() == skill }
                    if (left <= 0) continue
                    changedParticipants = changedParticipants.assignSkill(it.id, Skill(skill))
                    break
                }
            }
        return changedParticipants
    }

    private fun getCandidateSkillList(cdefSomeoneSkill: CDef.Skill): List<CDef.Skill> {
        return when (cdefSomeoneSkill) {
            CDef.Skill.おまかせ村人陣営 -> CDef.Skill.listAll().filter {
                !CDef.Skill.listOfSomeoneSkill().contains(it) && it.campCode() == CDef.Camp.村人陣営.code()
            }
            CDef.Skill.おまかせ人狼陣営 -> CDef.Skill.listAll().filter {
                !CDef.Skill.listOfSomeoneSkill().contains(it) && it.campCode() == CDef.Camp.人狼陣営.code()
            }
            else -> throw IllegalStateException("謎のおまかせ希望")
        }
    }

    private fun assignSecondRangeRequest(
        participants: VillageParticipants,
        skillPersonCountMap: Map<CDef.Skill, Int>
    ): VillageParticipants {
        var changedParticipants = participants.copy()
        // 範囲指定している人
        changedParticipants.list
            .filter {
                it.skill == null && CDef.Skill.listOfSomeoneSkill()
                    .contains(it.skillRequest.second.toCdef()) && it.skillRequest.second.toCdef() != CDef.Skill.おまかせ
            }
            .shuffled()
            .forEach {
                // 役職候補
                val candidateSkillList = getCandidateSkillList(it.skillRequest.second.toCdef()).sorted()
                for (skill in candidateSkillList) {
                    val capacity = skillPersonCountMap[skill] ?: 0
                    val left = capacity - changedParticipants.list.count { member -> member.skill?.toCdef() == skill }
                    if (left <= 0) continue
                    changedParticipants = changedParticipants.assignSkill(it.id, Skill(skill))
                    break
                }
            }
        return changedParticipants
    }

    private fun assignOther(
        participants: VillageParticipants,
        skillPersonCountMap: Map<CDef.Skill, Int>
    ): VillageParticipants {
        var changedParticipants = participants.copy()

        // 役職が決まっていない参加者に
        participants.list.filter { it.skill == null }.shuffled().forEach { member ->
            // 枠が余っている役職を割り当てる
            for ((cdefSkill, capacity) in skillPersonCountMap.entries) {
                // 空いている枠数
                val left = capacity - changedParticipants.list.count { it.skill?.code == cdefSkill.code() }
                if (left <= 0) continue
                changedParticipants = changedParticipants.assignSkill(member.id, Skill(cdefSkill))
                break
            }
        }
        return changedParticipants
    }
}