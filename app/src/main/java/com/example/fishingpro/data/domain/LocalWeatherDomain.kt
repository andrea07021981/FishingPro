package com.example.fishingpro.data.domain

import android.os.Parcelable
import com.example.fishingpro.data.source.remote.datatranferobject.Sys
import com.example.fishingpro.data.source.remote.datatranferobject.Weather
import kotlinx.android.parcel.Parcelize
import javax.inject.Inject

@Parcelize
data class LocalWeatherDomain @Inject constructor(
    val wBase: String,
    val wClouds: CloudsDomain,
    val wCod: Int,
    val wCoord: CoordDomain,
    val wDt: Int,
    val wId: Int,
    val wMain: MainDomain,
    val wName: String,
    val wSys: SysDomain,
    val wTimezone: Int,
    val wWeather: List<WeatherDomain>,
    val wWind: WindDomain
) : Parcelable {
    constructor(): this("", CloudsDomain(0), 0, CoordDomain(0.0, 0.0), 0, 0,
        MainDomain(0.0, 0, 0 ,0.0, 0.0, 0.0), "",
        SysDomain("", null, null, 0, 0, null), 0, listOf<WeatherDomain>(), WindDomain(0.0, 0.0))
}

@Parcelize
data class MainDomain @Inject constructor(
    val wFeelsLike: Double,
    val wHumidity: Int,
    val wPressure: Int,
    val wTemp: Double,
    val wTempMax: Double,
    val wTempMin: Double
) : Parcelable

@Parcelize
data class CloudsDomain @Inject constructor(
    val wAll: Int
) : Parcelable

@Parcelize
data class CoordDomain @Inject constructor(
    val wLat: Double,
    val wLon: Double
) : Parcelable

@Parcelize
data class SysDomain @Inject constructor(
    val wCountry: String,
    val wId: Int?,
    val wMessage: Double?,
    val wSunrise: Int,
    val wSunset: Int,
    val wType: Int?
) : Parcelable

@Parcelize
data class WeatherDomain @Inject constructor(
    val wDescription: String,
    val wIcon: String,
    val wId: Int,
    val wMain: String
) : Parcelable

@Parcelize
data class WindDomain @Inject constructor(
    val wDeg: Double,
    val wSpeed: Double
) : Parcelable
