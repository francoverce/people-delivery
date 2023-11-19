package com.example.userint.v1

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin

@Controller
@CrossOrigin(origins = ["*"])
class WebSocketController{

    @Autowired
    private lateinit var messagingTemplate: SimpMessageSendingOperations

    @MessageMapping("/accepted_trips")
    @SendTo("/topic/update")
    fun sendUpdate(message: String): String {

        val notification = "Nueva notificación: $message"
        
        messagingTemplate.convertAndSend("/topic/update", notification)
        logger.info(notification)
        return notification
    }
}
