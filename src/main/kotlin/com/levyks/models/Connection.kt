package com.levyks.models

import com.levyks.dtos.CreateRoomDTO
import com.levyks.dtos.JoinRoomDTO
import com.levyks.dtos.RoomCreatedDTO
import com.levyks.dtos.RoomDTO
import com.levyks.enums.ClientEventType
import com.levyks.enums.ExceptionCode
import com.levyks.enums.MessageCode
import com.levyks.exceptions.SongQuizException
import com.levyks.services.createRoom
import com.levyks.services.getRoom
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlin.reflect.KSuspendFunction1

class Connection(private val session: DefaultWebSocketServerSession) {

    private var player: Player? = null

    private fun setPlayer(player: Player) {
        this.player = player
        player.connection = this
    }

    private fun getHandler(type: ClientEventType): (suspend (Message<*>) -> Unit)? {
        return when (type) {
            ClientEventType.CREATE_ROOM -> handler(::onCreateRoom)
            ClientEventType.JOIN_ROOM -> handler(::onJoinRoom)
            else -> null
        }
    }

    private suspend fun onCreateRoom(message: Message<CreateRoomDTO>) {
        player?.let { throw SongQuizException(ExceptionCode.ALREADY_IN_ROOM, mapOf("roomCode" to it.room.code)) }
        val room = createRoom(message.payload.nickname)
        setPlayer(room.leader)
        message.resolve(MessageCode.SUCCESS, RoomCreatedDTO.fromRoom(room))
    }

    private suspend fun onJoinRoom(message: Message<JoinRoomDTO>) {
        player?.let { throw SongQuizException(ExceptionCode.ALREADY_IN_ROOM, mapOf("roomCode" to it.room.code)) }
        val room = getRoom(message.payload.roomCode) ?: throw SongQuizException(ExceptionCode.ROOM_NOT_FOUND)
        val player = room.joinPlayer(message.payload.nickname, message.payload.token)
        setPlayer(player)
        message.resolve(MessageCode.SUCCESS, RoomDTO.fromRoom(room))
    }

    private inline fun <reified T> handler(handler: KSuspendFunction1<Message<T>, Unit>): suspend (Message<*>) -> Unit {
        return { message ->
            if (message.payload is T) {
                @Suppress("UNCHECKED_CAST")
                handler(message as Message<T>)
            }
        }
    }

    suspend fun handleFrame(frame: Frame) {
        if (frame !is Frame.Text) return
        val message = Message.parse(frame, session) ?: return
        val function = getHandler(message.type) ?: return
        try {
            function(message)
        } catch (e: SongQuizException) {
            message.reject(e.code, e.data)
        }
    }

    fun handleDisconnect() {
        player?.connection = null
    }

}