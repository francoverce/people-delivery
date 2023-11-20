package com.example.userint.v1

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.stereotype.Controller

@Controller
class WebSocketController{

    @Autowired
    private lateinit var messagingTemplate: SimpMessageSendingOperations

    private val logger: Logger = LoggerFactory.getLogger(WebSocketController::class.java)

    @MessageMapping("/ws")
    @SendTo("/topic/update")
    fun sendUpdate(message: String): String {
        val notification = "Nueva notificación: $message"
        logger.info("Mensaje recibido: {}", message)
        logger.info("Enviando notificación: {}", notification)
        messagingTemplate.convertAndSend("/topic/update", notification)
        return notification
    }
}
