package com.levyks.songquiz.models

import java.security.SecureRandom
import java.util.*

class Player(val nickname: String, val room: Room) {

    val token: String = generateToken()
    var connection: Connection? = null

    var score: Int = 0

    val isOnline: Boolean
        get() = connection != null
    val isLeader: Boolean
        get() = room.leader == this

    companion object {
        fun generateToken(): String {
            val random = SecureRandom()
            val bytes = ByteArray(20)
            random.nextBytes(bytes)
            val encoder = Base64.getUrlEncoder().withoutPadding()
            return encoder.encodeToString(bytes)
        }
    }

    fun leave() {
        room.players.remove(nickname)
    }

}