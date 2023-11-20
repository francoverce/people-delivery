package com.example.userint.controller

import com.example.userint.domain.entities.Reviews
import com.example.userint.domain.requests.CreateReviewsRequest
import com.example.userint.v1.JwtReviewsControllerV1
import org.assertj.core.api.Assertions.assertThat
import org.springframework.http.HttpStatus
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.mock.web.MockHttpServletRequest
import java.util.*
import javax.servlet.http.HttpServletRequest

@SpringBootTest
class JwtReviewsControllerV1Tests {
    @Autowired
    lateinit var jwtReviewsControllerV1: JwtReviewsControllerV1

    @Mock
    lateinit var mockRequest: HttpServletRequest

    @Test
    fun createReviewSuccessful() {
        val authorizationHeader = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJUZXN0VXNlckBnbWFpbC5jb20iLCJhdWQiOiJzZWN1cmUtYXBwIiwibmJmIjoxNzAwMzc5MDk2LCJpc3MiOiJzZWN1cmUtYXBpIiwiZXhwIjoxNzAyNTM5MDk2LCJ1c2VySWQiOjIzLCJub21icmUiOiJUZXN0ZXIiLCJpYXQiOjE3MDAzNzkwOTYsImVtYWlsIjoiVGVzdFVzZXJAZ21haWwuY29tIiwidXNlckNvZGUiOiJkZTY1ZmFkYS1hYmRmLTQ4MmItOWUzMi04NzYxOGEyMTZkYjAifQ.WuRPeW98uLH6whXW5ZaFHbDcfssfuYE1DYXYpsB_ax_A_d0Olj6qV5HfQJeX3A0cpzS9kXAeq-7RJE6A4lOBkQ"
        val tripCode = UUID.fromString("7191ec49-9f2e-4a35-bac5-f4d6e3448c9a")
        val rating = 10
        val driverId:Long = 31

        val createReviewsRequest = CreateReviewsRequest(
            tripCode = tripCode,
            rating = rating,
            driverId = driverId,
        )

        // Datos esperados
        val expectedResponse = ResponseEntity<CreateReviewsRequest>(HttpStatus.OK)


        // Crear una instancia de HttpServletRequest simulada
        val simulatedRequest = MockHttpServletRequest()

        // Establecer el encabezado de autorización en la solicitud simulada
        simulatedRequest.addHeader(HttpHeaders.AUTHORIZATION, authorizationHeader)

        // Ejecutar la llamada al método con la solicitud simulada
        val response = jwtReviewsControllerV1.createReview(simulatedRequest, createReviewsRequest)


        // Verificación
        assertThat(response.statusCode).isEqualTo(expectedResponse.statusCode)
    }

}