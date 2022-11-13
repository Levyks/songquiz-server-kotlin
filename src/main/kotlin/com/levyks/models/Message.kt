package com.levyks.models

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import com.levyks.dtos.*
import com.levyks.enums.ClientEventType
import com.levyks.enums.ExceptionCode
import com.levyks.enums.MessageCode
import com.levyks.enums.ServerEventType
import com.levyks.helpers.getMismatchedInputExceptionFilteredMessage
import com.levyks.helpers.tryParse
import com.levyks.plugins.objectMapper
import io.konform.validation.Invalid
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlin.reflect.KClass

class Message<T>(
    val session: DefaultWebSocketServerSession,
    val uuid: String,
    val type: ClientEventType,
    val payload: T
    ) {

    companion object {
        suspend fun <T> staticResolve(session: DefaultWebSocketServerSession, uuid: String, messageCode: MessageCode, data: T) {
            val payload = ClientMessageResolvedResponsePayload(uuid, messageCode, data)
            val message = ServerMessage(ServerEventType.CLIENT_MESSAGE_RESOLVED, payload)
            session.sendSerialized(message)
        }

        suspend fun <T> staticReject(session: DefaultWebSocketServerSession, uuid: String, exceptionCode: ExceptionCode, data: T) {
            val payload = ClientMessageRejectedResponsePayload(uuid, exceptionCode, data)
            val message = ServerMessage(ServerEventType.CLIENT_MESSAGE_REJECTED, payload)
            session.sendSerialized(message)
        }


        private fun getPayloadClass(type: ClientEventType): KClass<*>? {
            return when (type) {
                ClientEventType.CREATE_ROOM -> CreateRoomDTO::class
                ClientEventType.JOIN_ROOM -> JoinRoomDTO::class
                else -> null
            }
        }

        suspend fun parse(frame: Frame.Text, session: DefaultWebSocketServerSession): Message<*>? {
            val message = try {
                objectMapper.readValue(frame.readText(), JsonNode::class.java)
            } catch (e: Exception) {
                null
            } ?: return null

            val uuid = if (message["uuid"].isTextual) message["uuid"].asText() else return null
            val typeStr = if (message["type"].isTextual) message["type"].asText() else null

            val type = typeStr?.let { tryParse<ClientEventType>(it) };

            if (typeStr == null || type == null) {
                staticReject(session, uuid, ExceptionCode.INVALID_EVENT_TYPE, mapOf("type" to typeStr))
                return null
            }

            val payloadObject = message["payload"] ?: null;
            if (payloadObject == null) {
                staticReject(session, uuid, ExceptionCode.MISSING_PAYLOAD, null)
                return null
            }

            val payloadClass = getPayloadClass(type) ?: return null;

            val payload = try {
                objectMapper.treeToValue(payloadObject, payloadClass.java)
            } catch (e: Exception) {
                val errorMessage = if (e is MismatchedInputException) {
                    "payload.${e.path[0].fieldName} -> ${getMismatchedInputExceptionFilteredMessage(e)}"
                } else {
                    "Unknown error"
                }
                staticReject(session, uuid, ExceptionCode.MALFORMED_PAYLOAD , mapOf("message" to errorMessage))
                null
            } ?: return null

            if (payload is Validatable) {
                val validationResult = payload.validate();
                if (validationResult is Invalid) {
                    val fields = validationResult.errors.associate { it.dataPath.substring(1) to it.message }
                    staticReject(session, uuid, ExceptionCode.INVALID_PAYLOAD, InvalidPayloadExceptionData(fields))
                    return null
                }
            }

            return Message(session, uuid, type, payload);
        }
    }

    suspend fun <D> resolve(messageCode: MessageCode, data: D) {
        staticResolve(session, uuid, messageCode, data)
    }

    suspend fun <D> reject(exceptionCode: ExceptionCode, data: D) {
        staticReject(session, uuid, exceptionCode, data)
    }

}