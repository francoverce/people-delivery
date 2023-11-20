package com.example.userint.v1

import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller

@Controller
class WebSocketController {

    @MessageMapping("/ws")
    @SendTo("/topic/novedades")
    fun enviarNovedad(novedad: String): String {
        return novedad
    }
}