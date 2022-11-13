package com.levyks.songquiz.dtos

import com.levyks.songquiz.enums.ExceptionCode
import com.levyks.songquiz.enums.MessageCode
import com.levyks.songquiz.enums.ServerEventType
import io.konform.validation.ValidationResult

open class ServerMessagePayload

data class ServerMessage<T : ServerMessagePayload>(
    val type: ServerEventType,
    val payload: T,
)

class ClientMessageResolvedResponsePayload<T>(
    val uuid: String,
    val messageCode: MessageCode,
    val data: T,
): ServerMessagePayload()

class ClientMessageRejectedResponsePayload<T>(
    val uuid: String,
    val exceptionCode: ExceptionCode,
    val data: T,
): ServerMessagePayload()

data class InvalidPayloadExceptionData(
    val fields: Map<String, String>,
)

interface Validatable {
    fun validate(): ValidationResult<*>
}