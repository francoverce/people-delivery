package com.example.userint.controller

import com.example.userint.domain.requests.CardsDTO
import com.example.userint.domain.requests.PatchUserRequest
import com.example.userint.domain.responses.RegisterResponse
import com.example.userint.domain.responses.UsersResponse
import com.example.userint.jwt.JwtRequestStep1
import com.example.userint.jwt.JwtRequestStep2
import com.example.userint.v1.JwtLoginControllerV1
import com.example.userint.v1.JwtUserControllerV1
import com.example.userint.v1.RegisterControllerV1
import org.apache.catalina.User
import org.assertj.core.api.Assertions
import org.springframework.http.HttpStatus
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.mock.web.MockHttpServletRequest
import javax.servlet.http.HttpServletRequest

@SpringBootTest
class JwtUserControllerV1Tests {

    @Autowired
    lateinit var controller: JwtUserControllerV1

    val authorizationHeader = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJUZXN0VXNlckBnbWFpbC5jb20iLCJhdWQiOiJzZWN1cmUtYXBwIiwibmJmIjoxNzAwMzc5MDk2LCJpc3MiOiJzZWN1cmUtYXBpIiwiZXhwIjoxNzAyNTM5MDk2LCJ1c2VySWQiOjIzLCJub21icmUiOiJUZXN0ZXIiLCJpYXQiOjE3MDAzNzkwOTYsImVtYWlsIjoiVGVzdFVzZXJAZ21haWwuY29tIiwidXNlckNvZGUiOiJkZTY1ZmFkYS1hYmRmLTQ4MmItOWUzMi04NzYxOGEyMTZkYjAifQ.WuRPeW98uLH6whXW5ZaFHbDcfssfuYE1DYXYpsB_ax_A_d0Olj6qV5HfQJeX3A0cpzS9kXAeq-7RJE6A4lOBkQ"

    val userResponseOriginal = UsersResponse(
        name = "Tester",
        userName = "TesterUsuario",
        lastName = "Usuario",
        phone = "1234",
        adress = "",
        email = "TestUser@gmail.com",
    )

    @Test
    fun getUser() {
        val simulatedRequest = MockHttpServletRequest()

        // Establecer el encabezado de autorización en la solicitud simulada
        simulatedRequest.addHeader(HttpHeaders.AUTHORIZATION, authorizationHeader)

        // Ejecutar la llamada al método con la solicitud simulada
        val responseCreate = controller.getUser(simulatedRequest)
        Assertions.assertThat(responseCreate.statusCode).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(responseCreate.body as UsersResponse).isEqualTo(userResponseOriginal)
    }

    @Test
    fun updateUser() {
        val userResponseUpdate = UsersResponse(
            name = "Tester",
            userName = "TesterUsuario",
            lastName = "Usuario",
            phone = "1234",
            adress = "Casa",
            email = "TestUser@gmail.com"
        )

        val patchUserRequestUpdate = PatchUserRequest(
            name = "Tester",
            userName = "TesterUsuario",
            lastName = "Usuario",
            phone = "1234",
            adress = "Casa"
        )

        val patchUserRequestOriginal = PatchUserRequest(
            name = "Tester",
            userName = "TesterUsuario",
            lastName = "Usuario",
            phone = "1234",
            adress = ""
        )

        val simulatedRequest = MockHttpServletRequest()

        // Establecer el encabezado de autorización en la solicitud simulada
        simulatedRequest.addHeader(HttpHeaders.AUTHORIZATION, authorizationHeader)

        // Ejecutar la llamada al método con la solicitud simulada
        val responseUpdate = controller.pathUser(simulatedRequest,patchUserRequestUpdate)
        val responseGetUpdate = controller.getUser(simulatedRequest)
        Assertions.assertThat(responseUpdate.statusCode).isEqualTo(HttpStatus.CREATED)
        Assertions.assertThat(responseGetUpdate.statusCode).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(responseGetUpdate.body as UsersResponse).isEqualTo(userResponseUpdate)

        // Retornar estado
        val responseOriginal = controller.pathUser(simulatedRequest,patchUserRequestOriginal)
        val responseGetOriginal = controller.getUser(simulatedRequest)
        Assertions.assertThat(responseOriginal.statusCode).isEqualTo(HttpStatus.CREATED)
        Assertions.assertThat(responseGetOriginal.statusCode).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(responseGetOriginal.body as UsersResponse).isEqualTo(userResponseOriginal)
    }
}