package com.example.fishingpro.data.domain

import com.google.firebase.firestore.GeoPoint
import java.util.*

data class LocalMapCatch(
    val userId: String,
    val date: Date,
    var fishId: String,
    var location: GeoPoint?,
    var weight: Double,
)