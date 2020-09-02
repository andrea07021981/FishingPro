package com.example.fishingpro.data.domain

import com.google.firebase.firestore.GeoPoint
import java.util.*

data class LocalDailyCatch(
    var userId: String,
    var date: Date,
    var fish: List<FishData?>
)

data class FishData(
    var fishId: String,
    var location: GeoPoint?,
    var weight: Double
)