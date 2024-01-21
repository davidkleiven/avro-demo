package com.github.davidkleiven.avrodemo

import java.time.Instant

data class Plan(
    val mrid: String,
    val power: Float,
    val componentMrid: String,
    val startTime: Instant,
    val endTime: Instant
)