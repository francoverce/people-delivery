package com.example.userint.domain.entities

import java.time.Instant
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "reviews")
open class Reviews(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    open var id: Long = 0,

    @Column(name = "trip_code")
    open var tripCode: UUID,

    @Column(name = "driver_id")
    open var driverId: Long,

    @Column(name = "rating")
    open var rating: Int,

    @Column(name = "created_at", nullable = false)
    open var created_at: Instant = Instant.now(),
)
