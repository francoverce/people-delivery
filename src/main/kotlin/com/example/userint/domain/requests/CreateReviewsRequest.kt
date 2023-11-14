package com.example.userint.domain.requests

import java.util.*

data class   CreateReviewsRequest(
    val tripCode: UUID,
    val rating: Int,
    val driverId: String
)