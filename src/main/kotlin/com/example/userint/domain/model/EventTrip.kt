package com.example.userint.domain.model

import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class EventTrip (
    val exchange:String,
    val message: MessageNewTrip
)

data class MessageNewTrip(
    val idViaje: String,
    val idUsuario: String,
    val nombre: String,
    val apellido: String,
    val date: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
    val estadoViaje: String,
    val puntoPartida: String,
    val puntoLlegada: String,
    val isMovilidadReducida: Boolean,
    val metodoPago: String,
    val precio: Float,
    val distancia: Float
)


data class EventTripCancelledTripsBeforeRequestingDriver (
    val exchange:String,
    val message: MessageTripCancelledTripsBeforeRequestingDriver
)

data class MessageTripCancelledTripsBeforeRequestingDriver(
    val idViaje: Long,
    val nombre: String,
    val apellido: String,
    val date: Instant,
    val estadoViaje: String,
    val puntoPartida: String,
    val puntoLlegada: String,
    val isMovilidadReducida: Boolean,
    val metodoPago: String,
    val precio: Float,
    val distancia: Float
)


data class EventTripCancelledTripsAfterRequestingDriver (
    val exchange:String,
    val message: MessageTripCancelledTripsAfterRequestingDriver
)

data class MessageTripCancelledTripsAfterRequestingDriver(
    val idViaje: Long,
    val date: Instant,
    val estadoViaje: String,
)