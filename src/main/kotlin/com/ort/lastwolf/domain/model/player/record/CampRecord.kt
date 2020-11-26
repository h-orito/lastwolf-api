package com.ort.lastwolf.domain.model.player.record

import com.ort.dbflute.allcommon.CDef
import com.ort.lastwolf.domain.model.camp.Camp
import com.ort.lastwolf.domain.model.player.Player
import com.ort.lastwolf.domain.model.village.Villages

data class CampRecord(
    val camp: Camp,
    val participateCount: Int,
    val winCount: Int,
    val winRate: Float,
    val loseRate: Float,
    val drawRate: Float
) {
    constructor(
        camp: Camp,
        player: Player,
        villages: Villages
    ) : this(
        camp = camp,
        participateCount = villages.list.size,
        winCount = sumWinCount(villages, player),
        winRate = if (villages.list.isEmpty()) 0F
        else sumWinCount(villages, player).toFloat() / villages.list.size.toFloat(),
        loseRate = if (villages.list.isEmpty()) 0F
        else sumLoseCount(villages, player).toFloat() / villages.list.size.toFloat(),
        drawRate = if (villages.list.isEmpty()) 0F
        else sumDrawCount(villages, player).toFloat() / villages.list.size.toFloat()
    )

    companion object {
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
