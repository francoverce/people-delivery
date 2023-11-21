package com.example.userint.domain.model

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

enum class Status {
    NEW, INPROGRESS, CLOSED
}

enum class Prioridad {
    ALTA, MEDIA, BAJA
}

enum class TipoUsuario {
    CHOFER, USUARIO, ADMIN
}

data class EventNewTicket (
    val exchange:String,
    val message:NewTicket
)

data class NewTicket(
    val idTicket: Long,
    val idSolicitante: Long,
    val idReclamado: Long,
    val idViaje: Long,
    val asunto: String,
    val detalle: String,
    val status: Status,
    val prioridad: Prioridad,
    val timestampCreacion: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
    val tipoUsuario: TipoUsuario
)



data class UpdateTicket(
    val idTicket: Long,
    val newStatus: String,
    val timeStampActualizacion: String
)
