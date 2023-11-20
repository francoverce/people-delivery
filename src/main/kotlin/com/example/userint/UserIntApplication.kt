package com.example.userint

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker

@SpringBootApplication
@EnableWebSocketMessageBroker
class UserIntApplication

fun main(args: Array<String>) {
    runApplication<UserIntApplication>(*args)
}
