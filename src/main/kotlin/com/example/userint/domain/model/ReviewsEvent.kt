package com.example.userint.domain.model

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


data class ReviewNewEvent (
    val exchange:String,
    val message:MessageReviewEvent
)

data class MessageReviewEvent(
    val idUsuario: Long,
    val nombreUsuario: String,
    val idChofer: Long,
    val cantidadEstrellas: Int,
    val idViaje: Long,
    val fechaCalificacion: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
)



