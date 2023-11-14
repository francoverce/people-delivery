package com.example.userint.domain.model

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


data class ReviewNewEvent (
    val exchange:String,
    val message:MessageReviewEvent
)

data class MessageReviewEvent(
    val idUsuario: String,
    val nombreUsuario: String,
    val idChofer: String,
    val cantidadEstrellas: Int,
    val idViaje: String,
    val fechaCalificacion: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
)



