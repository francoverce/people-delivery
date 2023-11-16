package com.example.userint.domain.requests

data class TripsDTO(
    val from: String,
    val since: String,
    val distance: Float,
    val paymentMethod: String?,
)

data class NewTripDTO(
    val paymentMethod: String,
    val numberTarjeta: String?,
    val isMobilityReduce: Boolean,

)
