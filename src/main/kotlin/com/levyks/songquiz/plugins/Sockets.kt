package com.levyks.songquiz.plugins

import com.levyks.songquiz.routes.webSocketRoutes
import io.ktor.server.application.*
import io.ktor.server.websocket.*
import java.time.Duration
import io.ktor.serialization.jackson.*

fun Application.configureSockets() {
    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
        contentConverter = JacksonWebsocketContentConverter()
    }
    webSocketRoutes();
}
