package com.levyks.songquiz.dtos

import com.levyks.songquiz.enums.RoomStatus
import com.levyks.songquiz.models.Room
import io.konform.validation.Validation
import io.konform.validation.ValidationResult
import io.konform.validation.jsonschema.maxLength
import io.konform.validation.jsonschema.minLength

data class CreateRoomDTO(
    val nickname: String,
): Validatable {
     override fun validate(): ValidationResult<CreateRoomDTO> {
        return Validation {
            CreateRoomDTO::nickname {
                minLength(3)
                maxLength(20)
            }
        }(this)
    }
}

data class JoinRoomDTO(
    val nickname: String,
    val roomCode: String,
    val token: String?,
): Validatable {
    override fun validate(): ValidationResult<JoinRoomDTO> {
        return Validation {
            JoinRoomDTO::nickname {
                minLength(3)
                maxLength(20)
            }
            JoinRoomDTO::roomCode {
                minLength(4)
                maxLength(4)
            }
        }(this)
    }
}

data class RoomDTO(
    val code: String,
    val players: List<PlayerDTO>,
    val leader: String,
    val status: RoomStatus,
    val numberOfRounds: Int,
    val secondsPerRound: Int,
    val playlist: PlaylistDTO?
) {
    companion object {
        fun fromRoom(room: Room): RoomDTO {
            return RoomDTO(
                room.code,
                room.players.values.map { PlayerDTO.fromPlayer(it) },
                room.leader.nickname,
                room.status,
                room.numberOfRounds,
                room.secondsPerRound,
                null
            )
        }
    }
}

data class RoomCreatedDTO(
    val room: RoomDTO,
    val player: PlayerWithTokenDTO
) {
    companion object {
        fun fromRoom(room: Room): RoomCreatedDTO {
            return RoomCreatedDTO(
                RoomDTO.fromRoom(room),
                PlayerWithTokenDTO.fromPlayer(room.leader)
            )
        }
    }
}