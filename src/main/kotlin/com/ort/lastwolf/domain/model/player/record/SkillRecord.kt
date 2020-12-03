package com.ort.lastwolf.domain.model.player.record

import com.ort.dbflute.allcommon.CDef
import com.ort.lastwolf.domain.model.player.Player
import com.ort.lastwolf.domain.model.skill.Skill
import com.ort.lastwolf.domain.model.village.Villages

data class SkillRecord(
    val skill: Skill,
    val participateCount: Int,
    val winCount: Int,
    val winRate: Float,
    val loseCount: Int,
    val loseRate: Float,
    val drawCount: Int,
    val drawRate: Float
) {
    constructor(
        skill: Skill,
        player: Player,
        villages: Villages
    ) : this(
        skill = skill,
        participateCount = participantCount(villages),
        winCount = sumWinCount(villages, player),
        winRate = if (participantCount(villages) == 0) 0F
        else sumWinCount(villages, player).toFloat() / participantCount(villages).toFloat(),
        loseCount = sumLoseCount(villages, player),
        loseRate = if (participantCount(villages) == 0) 0F
        else sumLoseCount(villages, player).toFloat() / participantCount(villages).toFloat(),
        drawCount = sumDrawCount(villages, player),
        drawRate = if (participantCount(villages) == 0) 0F
        else sumDrawCount(villages, player).toFloat() / participantCount(villages).toFloat()
    )

    companion object {
        private fun participantCount(villages: Villages): Int = villages.list.size

        private fun sumWinCount(villages: Villages, player: Player): Int {
            return villages.list.count { village ->
                village.participants.findByPlayerId(player.id)?.winlose?.toCdef() == CDef.WinLose.勝利
            }
        }

        private fun sumLoseCount(villages: Villages, player: Player): Int {
            return villages.list.count { village ->
                village.participants.findByPlayerId(player.id)?.winlose?.toCdef() == CDef.WinLose.敗北
            }
        }

        private fun sumDrawCount(villages: Villages, player: Player): Int {
            return villages.list.count { village ->
                village.participants.findByPlayerId(player.id)?.winlose?.toCdef() == CDef.WinLose.引分
            }
        }
    }
}
