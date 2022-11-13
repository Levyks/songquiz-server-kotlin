package com.levyks.routes

import com.levyks.models.Connection
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import java.util.*


fun Application.webSocketRoutes() {

    routing {
        val connections = Collections.synchronizedSet<Connection?>(LinkedHashSet())
        webSocket("/ws") {
            val connection = Connection(this)
            connections += connection
            try {
                for (frame in incoming) {
                    connection.handleFrame(frame)
                }
            } catch (e: Exception) {
                println("Error: $e")
            } finally {
                connection.handleDisconnect()
                connections -= connection
            }
        }
    }
}