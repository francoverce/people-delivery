package com.example.userint.services

import com.example.userint.client.CoreClient
import com.example.userint.domain.entities.Reviews
import com.example.userint.domain.model.MessageReviewEvent
import com.example.userint.domain.model.ReviewNewEvent
import com.example.userint.domain.requests.CreateReviewsRequest
import com.example.userint.repositories.sql.ReviewsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@Service
class ReviewsService {

    @Autowired
    private lateinit var reviewsRepository: ReviewsRepository

    @Autowired
    lateinit var coreClient: CoreClient

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var tripService: TripService


    fun createReview(userCode: UUID, createReviewsRequest: CreateReviewsRequest): Reviews {
        var trip = tripService.getTripByCode(createReviewsRequest.tripCode)
        val review = reviewsRepository.save(
            Reviews(
                tripCode = createReviewsRequest.tripCode,
                driverId = createReviewsRequest.driverId,
                rating = createReviewsRequest.rating,
            )
        )
        coreClient.sendPostRequest(
            ReviewNewEvent(
                exchange = "new_review",
                MessageReviewEvent(
                    idUsuario = trip!!.userId.id.toString(),
                    nombreUsuario = trip.userId.name!!,
                    idChofer = trip.driverId!!.idChofer,
                    cantidadEstrellas = createReviewsRequest.rating,
                    idViaje = trip.id.toString(),
                )
            )
        )
        return review
    }
}
