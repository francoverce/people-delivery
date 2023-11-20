package com.example.userint.controller

import com.example.userint.domain.entities.Cards
import com.example.userint.domain.entities.Claim
import com.example.userint.domain.requests.CardsDTO
import com.example.userint.domain.requests.CreateReviewsRequest
import com.example.userint.v1.CardsController
import com.example.userint.v1.JWTClaimsController
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.mock.web.MockHttpServletRequest
import java.util.*
import javax.servlet.http.HttpServletRequest
import org.junit.jupiter.api.Order;


@SpringBootTest
class JWTCardControllerTest {
    @Autowired
    lateinit var controller: CardsController

    val authorizationHeader = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJUZXN0VXNlckBnbWFpbC5jb20iLCJhdWQiOiJzZWN1cmUtYXBwIiwibmJmIjoxNzAwMzc5MDk2LCJpc3MiOiJzZWN1cmUtYXBpIiwiZXhwIjoxNzAyNTM5MDk2LCJ1c2VySWQiOjIzLCJub21icmUiOiJUZXN0ZXIiLCJpYXQiOjE3MDAzNzkwOTYsImVtYWlsIjoiVGVzdFVzZXJAZ21haWwuY29tIiwidXNlckNvZGUiOiJkZTY1ZmFkYS1hYmRmLTQ4MmItOWUzMi04NzYxOGEyMTZkYjAifQ.WuRPeW98uLH6whXW5ZaFHbDcfssfuYE1DYXYpsB_ax_A_d0Olj6qV5HfQJeX3A0cpzS9kXAeq-7RJE6A4lOBkQ"

    @Test
    fun createAndDeleteCard () {
        val cardsDTO = CardsDTO (
            cardNumber= "666",
            cardOperator= "CardTester",
            cardExpirationDate= "12/25",
            cardCVV= "123"
        )
        // Crear una instancia de HttpServletRequest simulada
        val simulatedRequest = MockHttpServletRequest()

        // Establecer el encabezado de autorización en la solicitud simulada
        simulatedRequest.addHeader(HttpHeaders.AUTHORIZATION, authorizationHeader)

        // Ejecutar la llamada al método con la solicitud simulada
        val responseCreate = controller.createCard(simulatedRequest,cardsDTO)
        Assertions.assertThat(responseCreate.statusCode).isEqualTo(HttpStatus.CREATED)

        val responseDelete = controller.deleteCard(simulatedRequest,cardsDTO.cardNumber)
        Assertions.assertThat(responseDelete.statusCode).isEqualTo(HttpStatus.OK)
    }


    @Test
    fun getCard () {
        val cardsDTOTestGet = CardsDTO (
            cardNumber= "777",
            cardOperator= "CardTester",
            cardExpirationDate= "12/25",
            cardCVV= "123"
        )

        val simulatedRequest = MockHttpServletRequest()

        simulatedRequest.addHeader(HttpHeaders.AUTHORIZATION, authorizationHeader)

        val response = controller.getCardByUserCode(simulatedRequest)

        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(response.body as List<CardsDTO>).contains(cardsDTOTestGet)
    }


    @Test
    fun updateCard () {
        val cardsDTOOriginalTest = CardsDTO (
            cardNumber= "777",
            cardOperator= "CardTester",
            cardExpirationDate= "12/25",
            cardCVV= "123"
        )

        val cardsDTOUpdateTest = CardsDTO (
            cardNumber= "777",
            cardOperator= "CardTester2",
            cardExpirationDate= "12/30",
            cardCVV= "451"
        )

        // Crear una instancia de HttpServletRequest simulada
        val simulatedRequest = MockHttpServletRequest()

        // Establecer el encabezado de autorización en la solicitud simulada
        simulatedRequest.addHeader(HttpHeaders.AUTHORIZATION, authorizationHeader)

        // Ejecutar la llamada al método con la solicitud simulada
        val responseUpdate = controller.updateCard(simulatedRequest,cardsDTOUpdateTest)
        val responseGetUpdate = controller.getCardByUserCode(simulatedRequest)
        Assertions.assertThat(responseUpdate.statusCode).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(responseGetUpdate.statusCode).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(responseGetUpdate.body as List<CardsDTO>).contains(cardsDTOUpdateTest)

        // Restauro al original
        val responseOriginalUpdate = controller.updateCard(simulatedRequest,cardsDTOOriginalTest)
        val responseOriginalGetUpdate = controller.getCardByUserCode(simulatedRequest)
        Assertions.assertThat(responseOriginalUpdate.statusCode).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(responseOriginalGetUpdate.statusCode).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(responseOriginalGetUpdate.body as List<CardsDTO>).contains(cardsDTOOriginalTest)
    }

}