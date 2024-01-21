package com.github.davidkleiven.avrodemo

import java.time.Instant

data class TimeseriesItem(
    val quantity: Double,
    val time: Instant
)

data class Timeseries(val id: String, val values: Iterable<TimeseriesItem>) {
    fun sort(): Timeseries {
        return Timeseries(id, values.sortedBy { item -> item.time })
    }
}

data class ComponentTimeseries(val componentId: String, val timeseries: Timeseries)