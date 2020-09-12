package com.example.fishingpro.data.domain

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.GeoPoint
import kotlinx.android.parcel.Parceler
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
import java.util.*

/**
 * Create a local object using map delegation for properties
 */
@Parcelize
data class LocalDailyCatch(private val data: @RawValue Map<String, Any?>) : Parcelable {
    val userId: String by data
    val date: Date by data
    val fish: List<FishData?> by data

    constructor(): this(mapOf())
}

@Parcelize
data class FishData(
    var fishId: String,
    var location: GeoPoint?,
    var weight: Double
) : Parcelable {

    constructor(): this("", null, 0.0)

    //Custom creator for GeoPoint, not auto parcelable
    companion object : Parceler<FishData> {
        override fun FishData.write(parcel: Parcel, p1: Int) {
            parcel.writeDouble(location!!.latitude);
            parcel.writeDouble(location!!.longitude);
        }

        override fun create(parcel: Parcel): FishData = FishData()
    }
}

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