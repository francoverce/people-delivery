package com.example.userint.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service

@Service
class NovedadService @Autowired constructor(
    private val messagingTemplate: SimpMessagingTemplate
) {

    fun enviarNovedadDesdeServicio(novedad: String) {
        // Enviar novedad a través de WebSocket
        messagingTemplate.convertAndSend("/topic/novedades", novedad)
    }
}