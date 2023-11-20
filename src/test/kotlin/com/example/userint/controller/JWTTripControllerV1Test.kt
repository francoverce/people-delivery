package com.example.userint.controller

import com.example.userint.domain.entities.Trips
import com.example.userint.domain.requests.*
import com.example.userint.repositories.sql.TripRepository
import com.example.userint.v1.JWTTripController
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.mock.web.MockHttpServletRequest
import java.time.LocalDateTime
import java.util.*


@SpringBootTest
class JWTTripControllerV1Test {

    @Autowired
    lateinit var controller: JWTTripController

    val authorizationHeader = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJUZXN0VXNlckBnbWFpbC5jb20iLCJhdWQiOiJzZWN1cmUtYXBwIiwibmJmIjoxNzAwMzc5MDk2LCJpc3MiOiJzZWN1cmUtYXBpIiwiZXhwIjoxNzAyNTM5MDk2LCJ1c2VySWQiOjIzLCJub21icmUiOiJUZXN0ZXIiLCJpYXQiOjE3MDAzNzkwOTYsImVtYWlsIjoiVGVzdFVzZXJAZ21haWwuY29tIiwidXNlckNvZGUiOiJkZTY1ZmFkYS1hYmRmLTQ4MmItOWUzMi04NzYxOGEyMTZkYjAifQ.WuRPeW98uLH6whXW5ZaFHbDcfssfuYE1DYXYpsB_ax_A_d0Olj6qV5HfQJeX3A0cpzS9kXAeq-7RJE6A4lOBkQ"
    val tripCode: UUID = UUID.fromString("7191ec49-9f2e-4a35-bac5-f4d6e3448c9a")
    val idViaje:Long = 127
    val idChofer:Long = 31

    @Test
    fun simularYCrearViaje(){
        val tripsDTO = TripsDTO(
            from = "Av.Test 1",
            since = "Av. Test 2",
            distance = 10F,
            paymentMethod = "TARJETA"
        )

        val newTripDTO = NewTripDTO(
            paymentMethod = "TARJETA",
            numberTarjeta = "12345678910",
            isMobilityReduce = false
        )
        val simulatedRequest = MockHttpServletRequest()

        // Establecer el encabezado de autorización en la solicitud simulada
        simulatedRequest.addHeader(HttpHeaders.AUTHORIZATION, authorizationHeader)

        val response = controller.simulateTrip(simulatedRequest,tripsDTO)
        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.CREATED)

        val bodyResponse = response.body as Trips
        val codeTripResponse = bodyResponse.code

        val responseCrearViaje = controller.createTrip(simulatedRequest,newTripDTO,codeTripResponse)
        Assertions.assertThat(responseCrearViaje.statusCode).isEqualTo(HttpStatus.CREATED)
    }


    @Test
    fun aceptarViaje(){
        val acceptedTrip = AcceptedTrip(
            idViaje = idViaje,
            idChofer = idChofer,
            nombreChofer = "John",
            apellidoChofer = "Doe",
            vehiculo = "Toyota Prius",
            patente = "ABC123",
            date = "1699967693.9362886"
        )

        val simulatedRequest = MockHttpServletRequest()

        // Establecer el encabezado de autorización en la solicitud simulada
        simulatedRequest.addHeader(HttpHeaders.AUTHORIZATION, authorizationHeader)

        val response = controller.acceptedTrip(simulatedRequest,acceptedTrip)
        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    }

    @Test
    fun cancelarViaje(){
        val simulatedRequest = MockHttpServletRequest()

        // Establecer el encabezado de autorización en la solicitud simulada
        simulatedRequest.addHeader(HttpHeaders.AUTHORIZATION, authorizationHeader)

        val response = controller.cancelTrip(simulatedRequest,tripCode,"CANCELADO")
        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.OK)

    }

    @Test
    fun continuarViaje(){
        val ongoingTrip = OnGoingTrip(
            idViaje = idViaje,
            idChofer = idChofer,
            isStarted = true,
            date =  "19/11/2023, 16:55:27"
        )
        val simulatedRequest = MockHttpServletRequest()

        // Establecer el encabezado de autorización en la solicitud simulada
        simulatedRequest.addHeader(HttpHeaders.AUTHORIZATION, authorizationHeader)

        val response = controller.ongoinTrip(simulatedRequest,ongoingTrip)
        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    }

    @Test
    fun terminarViaje(){
        val closedTrip = ClosedTrip(
            viaje_id = idViaje,
            status = "FINISHED",
            finishTimestamp =  "19/11/2023, 16:55:27"
        )
        val simulatedRequest = MockHttpServletRequest()

        // Establecer el encabezado de autorización en la solicitud simulada
        simulatedRequest.addHeader(HttpHeaders.AUTHORIZATION, authorizationHeader)

        val response = controller.closedTrip(simulatedRequest,closedTrip)
        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    }
}