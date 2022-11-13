package com.levyks.songquiz.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.jackson.*
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

val objectMapper = jacksonObjectMapper()

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        jackson()
    }
}
