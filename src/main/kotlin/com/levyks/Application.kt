package com.levyks

import com.levyks.plugins.configureRouting
import com.levyks.plugins.configureSerialization
import com.levyks.plugins.configureSockets
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureSerialization()
    configureSockets()
    configureRouting()
}
