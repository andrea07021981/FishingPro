package com.example.fishingpro.data.domain

import com.google.firebase.firestore.GeoPoint
import java.util.*

/**
 * Create a local object using map delegation for properties
 */
data class LocalDailyCatch(private val data: Map<String, Any?>) {
    val userId: String by data
    val date: Date by data
    val fish: List<FishData?> by data
}

data class FishData(
    var fishId: String,
    var location: GeoPoint?,
    var weight: Double
)

fun LocalDailyCatch.asDataMap(): List<LocalMapCatch> {
    return fish.filterNotNull().map {
        LocalMapCatch(
            userId = userId,
            date = date,
            fishId = it.fishId,
            location = it.location,
            weight = it.weight
        )
    }
}