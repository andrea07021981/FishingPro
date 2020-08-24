package com.example.fishingpro.data.domain

import android.location.Location
import com.google.firebase.firestore.GeoPoint
import com.google.type.LatLng
import java.util.*

class LocalCatch (
    var CatchId: String,
    var UserId: String,
    var FishId: String,
    var Date: Date,
    var Location: GeoPoint?,//TODO save a LatLng, don't use Firebase type here
    var Weight: Double
)  {
    constructor(): this ("0", "0", "0", Date(), null, 0.0)
}