package com.example.userint.services

import com.example.userint.client.CoreClient
import com.example.userint.domain.entities.Drivers
import com.example.userint.domain.entities.Trips
import com.example.userint.domain.model.*
import com.example.userint.domain.requests.*
import com.example.userint.repositories.sql.DriverRepository
import com.example.userint.repositories.sql.TripRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.*

@Service
class TripService {

    @Autowired
    private lateinit var tripRepository: TripRepository

    @Autowired
    private lateinit var driverRepository: DriverRepository

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    lateinit var coreClient: CoreClient

    @Autowired
    lateinit var novedadService: NovedadService

    enum class Estado {
        SIMULADO,
        CANCELLED_BEFORE_REQUESTING_DRIVER,
        CANCELLED_AFTER_REQUESTING_DRIVER,
        EN_CAMINO,
        INICIADO,
        FINALIZADO,
        BUSCANDO_CHOFER
    }

    @Transactional
    fun simulateTrip(trip: TripsDTO, userCode: UUID): Trips {
        userService.getUser(userCode).let {
            return tripRepository.save(
                Trips(
                    since = trip.since,
                    from = trip.from,
                    distancia = trip.distance,
                    userId = it,
                    created_at = Instant.now(),
                    is_finished = false,
                    status = Estado.SIMULADO.name,
                    driverId = null,
                    paymentType = trip.paymentMethod ?: "not-defined",
                    precio = trip.distance * 300F,
                )
            )
        }
    }
    @Transactional
    fun newTrip(userCode: UUID, tripCode: UUID, newTripDTO: NewTripDTO): Trips {
        userService.getUser(userCode).let {
           val trip = tripRepository.findByCode(tripCode).let { trip ->
               trip!!.status = Estado.BUSCANDO_CHOFER.name
               trip.paymentType = newTripDTO.paymentMethod
               trip.isMobilityReduce = newTripDTO.isMobilityReduce
               trip.updated_at = Instant.now()
               tripRepository.save(
                   trip
               )
           }
            coreClient.sendPostRequest(
                EventTrip(
                    exchange = "new_trips",
                    message = MessageNewTrip(
                        idViaje = trip.id,
                        idUsuario = trip.userId.id,
                        nombre = it.name ?: "",
                        apellido = it.lastName ?: "",
                        date = trip.created_at.toString(),
                        estadoViaje = Estado.BUSCANDO_CHOFER.name,
                        puntoPartida = trip.from!!,
                        puntoLlegada = trip.since,
                        isMovilidadReducida = trip.isMobilityReduce,
                        metodoPago = trip.paymentType!!,
                        precio = trip.distancia!! * 300F,
                        distancia = trip.distancia!!
                    )
                )
            )
            return trip
        }
    }


    @Transactional
    fun cancelTrip(code: UUID, status: String, userCode: UUID): Trips? {
        val trip = tripRepository.findByCodeAndUserId_Code(code, userCode)
        if (trip.isPresent) {
            val viaje = trip.get()
            viaje.status = status
            viaje.code = viaje.code
            viaje.id = viaje.id
            viaje.is_cancel = true
            viaje.updated_at = Instant.now()
            if (status == (Estado.CANCELLED_BEFORE_REQUESTING_DRIVER.name)) {
                coreClient.sendPostRequest(
                    EventTripCancelledTripsBeforeRequestingDriver(
                        exchange = "cancelled_trips_before_requesting_driver",
                        message = MessageTripCancelledTripsBeforeRequestingDriver(
                            idViaje = trip.get().id,
                            nombre = trip.get().userId.name!!,
                            apellido = trip.get().userId.lastName!!,
                            date = trip.get().created_at,
                            estadoViaje = Estado.CANCELLED_BEFORE_REQUESTING_DRIVER.name,
                            puntoPartida = trip.get().from!!,
                            puntoLlegada = trip.get().since,
                            isMovilidadReducida = trip.get().isMobilityReduce,
                            metodoPago = trip.get().paymentType!!,
                            precio = trip.get().precio!!,
                            distancia = trip.get().distancia!!
                        )
                    )
                )
            }
            if (status == (Estado.CANCELLED_AFTER_REQUESTING_DRIVER.name)) {
                coreClient.sendPostRequest(
                    EventTripCancelledTripsAfterRequestingDriver(
                        exchange = "cancelled_trips",
                        message = MessageTripCancelledTripsAfterRequestingDriver(
                            idViaje = trip.get().id,
                            date = trip.get().created_at,
                            estadoViaje = Estado.CANCELLED_AFTER_REQUESTING_DRIVER.name,
                        )
                    )
                )
            }
            return tripRepository.save(viaje)
        } else {
            throw IllegalArgumentException("El viaje con el code $code no existe.")
        }
    }

    @Transactional
    fun acceptedTrip(event: AcceptedTrip) {
        val epochWithDecimal: Double = event.date.toDouble()

        // Crear Instant con la parte entera (segundos)
        val instant: Instant = Instant.ofEpochSecond(epochWithDecimal.toLong())

        // Ajustar nanosegundos con la parte decimal
        val adjustedInstant: Instant = instant.plusNanos(((epochWithDecimal - instant.epochSecond.toDouble()) * 1_000_000_000).toLong())

        // Convertir a LocalDateTime
        val localDateTime: LocalDateTime = LocalDateTime.ofInstant(adjustedInstant, ZoneOffset.UTC)


        val driver =  driverRepository.save(Drivers(
            idChofer = event.idChofer,
            fullName = event.nombreChofer + " " + event.apellidoChofer,
            car = event.vehiculo,
            patent = event.patente,
            dateCome = localDateTime,
            icon = ""
        ))

        tripRepository.findById(event.idViaje).let {
            tripRepository.save(
                Trips(
                    id = it.get().id,
                    since = it.get().since,
                    from = it.get().from,
                    code = it.get().code,
                    distancia = it.get().distancia,
                    userId = it.get().userId,
                    created_at = it.get().created_at,
                    updated_at = Instant.now(),
                    is_finished = it.get().is_finished,
                    status = Estado.EN_CAMINO.name,
                    driverId = driver,
                    paymentType = it.get().paymentType,
                    precio = it.get().precio,
                )
            )
        }
        val novedad = "El viaje con ID ${event.idViaje} ha sido aceptado"
        novedadService.enviarNovedadDesdeServicio( novedad)
    }


    @Transactional
    fun ongoinTrip(event: OnGoingTrip) {
        tripRepository.findById(event.idViaje.toLong()).let {
            tripRepository.save(
                Trips(
                    id = it.get().id,
                    since = it.get().since,
                    from = it.get().from,
                    distancia = it.get().distancia,
                    code= it.get().code,
                    userId = it.get().userId,
                    updated_at = Instant.now(),
                    is_finished = it.get().is_finished,
                    status = Estado.INICIADO.name,
                    driverId = it.get().driverId,
                    paymentType = it.get().paymentType,
                    precio = it.get().precio,
                )
            )
        }
        val novedad = "El viaje con ID ${event.idViaje} ha sido inciado"
        novedadService.enviarNovedadDesdeServicio( novedad)
    }


    @Transactional
    fun closedTrip(event: ClosedTrip) {
        tripRepository.findById(event.viaje_id).let {
            tripRepository.save(
                Trips(
                    id = it.get().id,
                    since = it.get().since,
                    from = it.get().from,
                    distancia = it.get().distancia,
                    code= it.get().code,
                    userId = it.get().userId,
                    updated_at = Instant.now(),
                    is_finished = true,
                    status = event.status,
                    driverId = it.get().driverId,
                    paymentType = it.get().paymentType,
                    precio = it.get().precio,
                )
            )
        }
        val novedad = "El viaje con ID ${event.viaje_id} ha sido finalizado"
        novedadService.enviarNovedadDesdeServicio( novedad)
    }

    @Transactional
    fun getTripByCode(tripCode: UUID): Trips? = tripRepository.findByCode(tripCode)
}
