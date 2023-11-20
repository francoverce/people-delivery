package com.example.userint.domain.requests

import java.time.LocalDateTime

data class AcceptedTrip(
    val idViaje: String,
    val idChofer: String,
    val nombreChofer: String,
    val apellidoChofer: String,
    val vehiculo: String,
    val patente: String,
    val date: String
)


data class OnGoingTrip(
    val idViaje: String,
    val idChofer: String,
    val isStarted: Boolean,
    val date: String
)

data class ClosedTrip(
    val idViaje: String,
    val status: String,
    val finishTimestamp: String
)
