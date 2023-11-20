package com.example.userint.controller

import com.example.userint.exceptions.UserNotFoundException
import com.example.userint.exceptions.UserPasswordWrong
import com.example.userint.jwt.JwtRequestStep2
import com.example.userint.v1.JwtLoginControllerV1
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.mock.web.MockHttpServletRequest

@SpringBootTest
class JwtLoginControllerV1Test {
    @Autowired
    lateinit var controllerV1: JwtLoginControllerV1

    @Test
    fun loginRigth () {
        val jwtRequestStep2 = JwtRequestStep2(
            email = "matias@gmail.com",
            password = "123"
        )

        val simulatedRequest = MockHttpServletRequest()

        // Ejecutar la llamada al método con la solicitud simulada
        val response = controllerV1.Authenticate(simulatedRequest,jwtRequestStep2)
        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
    }

    @Test
    fun loginBadPassword () {
        val jwtRequestStep2 = JwtRequestStep2(
            email = "matias@gmail.com",
            password = "12dasda3"
        )

        val simulatedRequest = MockHttpServletRequest()

        // Ejecutar la llamada al método con la solicitud simulada
        assertThatExceptionOfType(UserPasswordWrong::class.java)
            .isThrownBy { controllerV1.Authenticate(simulatedRequest, jwtRequestStep2) }
            .withMessage("PASSWORD_INVALID")
    }

    @Test
    fun loginBadUser () {
        val jwtRequestStep2 = JwtRequestStep2(
            email = "matidsadfasfafas@gmail.com",
            password = "123"
        )

        val simulatedRequest = MockHttpServletRequest()

        // Ejecutar la llamada al método con la solicitud simulada
        assertThatExceptionOfType(UserNotFoundException::class.java)
            .isThrownBy { controllerV1.Authenticate(simulatedRequest, jwtRequestStep2) }
            .withMessage("USER_NOT_FOUND")
    }
}