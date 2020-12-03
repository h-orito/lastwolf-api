package com.ort.lastwolf.application.service

import com.ort.lastwolf.domain.model.player.Player
import com.ort.lastwolf.domain.model.player.Players
import com.ort.lastwolf.fw.security.LastwolfUser
import com.ort.lastwolf.infrastructure.datasource.player.PlayerDataSource
import org.springframework.stereotype.Service

@Service
class PlayerService(
    private val playerDataSource: PlayerDataSource
) {

    fun findPlayer(id: Int): Player = playerDataSource.findPlayer(id)

    fun findPlayer(user: LastwolfUser): Player = playerDataSource.findPlayer(user.uid)

    fun findPlayers(villageId: Int): Players = playerDataSource.findPlayers(villageId)

    fun findPlayers(playerIdList: List<Int>): Players = playerDataSource.findPlayers(playerIdList)

    fun updateNickname(user: LastwolfUser, nickname: String, twitterUserName: String) {
        playerDataSource.update(user.uid, nickname, twitterUserName)
    }

    fun updateDifference(before: Players, after: Players) = playerDataSource.updateDifference(before, after)
}