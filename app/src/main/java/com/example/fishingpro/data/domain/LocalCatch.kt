package com.example.fishingpro.data.domain

import android.location.Location
import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.GeoPoint
import com.google.type.LatLng
import kotlinx.android.parcel.Parceler
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
class LocalCatch (
    var CatchId: String,
    var UserId: String,
    var FishId: String,
    var Date: Date,
    var Location: GeoPoint?,
    var Weight: Double
) : Parcelable {

    constructor(): this ("0", "0", "0", Date(), null, 0.0)

    //Custom creator for GeoPoint, not auto parcelable
    companion object : Parceler<LocalCatch> {
        override fun LocalCatch.write(parcel: Parcel, p1: Int) {
            parcel.writeDouble(Location!!.latitude);
            parcel.writeDouble(Location!!.longitude);
        }

        override fun create(parcel: Parcel): LocalCatch = LocalCatch()
    }
}