package com.levyks.songquiz.enums

enum class ClientEventType() {
    CREATE_ROOM,
    JOIN_ROOM,
    START_GAME,
    ANSWER_QUESTION,
    LEAVE_ROOM,
}


enum class ServerEventType() {
    CLIENT_MESSAGE_RESOLVED,
    CLIENT_MESSAGE_REJECTED,
    ROOM_CREATED,
    ROOM_JOINED,

    PLAYER_JOINED,
    PLAYER_LEFT,

    PLAYER_RECONNECTED,
    PLAYER_DISCONNECTED,

    GAME_STARTED,
    QUESTION_ANSWERED,
    ROOM_LEFT,
}

enum class ExceptionCode {
    INVALID_EVENT_TYPE,
    MISSING_PAYLOAD,
    MALFORMED_PAYLOAD,
    INVALID_PAYLOAD,

    ALREADY_IN_ROOM,
    NICKNAME_ALREADY_USED,
    ROOM_NOT_FOUND,
}

enum class MessageCode {
    SUCCESS,
}