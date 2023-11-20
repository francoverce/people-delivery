package com.example.userint.domain.requests

data class AcceptedTrip(
    val idViaje: Long,
    val idChofer: Long,
    val nombreChofer: String,
    val apellidoChofer: String,
    val vehiculo: String,
    val patente: String,
    val date: String
)


data class OnGoingTrip(
    val idViaje: Long,
    val idChofer: Long,
    val isStarted: Boolean,
    val date: String
)

data class ClosedTrip(
    val idViaje: Long,
    val status: String,
    val finishTimestamp: String
)
