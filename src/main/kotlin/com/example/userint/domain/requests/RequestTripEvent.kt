package com.example.userint.domain.requests

import java.time.LocalDateTime

data class AcceptedTrip(
    val idViaje: Long,
    val idChofer: String,
    val nombreChofer: String,
    val apellidoChofer: String,
    val vehiculo: String,
    val patente: String,
    val date: LocalDateTime
)


data class OnGoingTrip(
    val idViaje: Long,
    val idChofer: String,
    val isStarted: Boolean,
    val date: LocalDateTime
)

data class ClosedTrip(
    val idViaje: Long,
    val status: String,
    val finishTimestmap: LocalDateTime
)