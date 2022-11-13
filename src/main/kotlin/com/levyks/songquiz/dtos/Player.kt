package com.levyks.songquiz.dtos

import com.levyks.songquiz.models.Player

data class PlayerDTO(
    val nickname: String,
    val score: Int,
    var isOnline: Boolean
) {
    companion object {
        fun fromPlayer(player: Player): PlayerDTO {
            return PlayerDTO(
                player.nickname,
                player.score,
                player.isOnline
            )
        }
    }
}

data class PlayerWithTokenDTO(
    val nickname: String,
    val score: Int,
    val token: String,
    var isOnline: Boolean
) {
    companion object {
        fun fromPlayer(player: Player): PlayerWithTokenDTO {
            return PlayerWithTokenDTO(
                player.nickname,
                player.score,
                player.token,
                player.isOnline
            )
        }
    }
}