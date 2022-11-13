package com.levyks.songquiz.models

import com.levyks.songquiz.enums.ExceptionCode
import com.levyks.songquiz.enums.RoomStatus
import com.levyks.songquiz.exceptions.SongQuizException
import java.util.*
import kotlin.collections.LinkedHashMap

class Room(val code: String, leaderNickname: String) {

    var leader: Player
    val players: MutableMap<String, Player> = Collections.synchronizedMap(LinkedHashMap());

    var status: RoomStatus = RoomStatus.IN_LOBBY

    var numberOfRounds: Int = 10
    var secondsPerRound: Int = 15

    var playlist: Playlist? = null

    init {
        val leader = Player(leaderNickname, this)
        players[leaderNickname] = leader
        this.leader = leader
    }

    fun reconnectPlayer(player: Player, token: String?): Player {
        if (token != player.token)
            throw SongQuizException(ExceptionCode.NICKNAME_ALREADY_USED)
        return player
    }

    fun joinPlayer(nickname: String, token: String?): Player {
        val existentPlayer = players[nickname]

        if (existentPlayer != null)
            return reconnectPlayer(existentPlayer, token)

        val player = Player(nickname, this)
        players[nickname] = player
        return player
    }

}