package com.example.fishingpro.data.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LocalWeatherDomain(
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
) : Parcelable

@Parcelize
data class MainDomain(
    val wFeelsLike: Double,
    val wHumidity: Int,
    val wPressure: Int,
    val wTemp: Double,
    val wTempMax: Double,
    val wTempMin: Double
) : Parcelable

@Parcelize
data class CloudsDomain(
    val wAll: Int
) : Parcelable

@Parcelize
data class CoordDomain(
    val wLat: Double,
    val wLon: Double
) : Parcelable

@Parcelize
data class SysDomain(
    val wCountry: String,
    val wId: Int?,
    val wMessage: Double?,
    val wSunrise: Int,
    val wSunset: Int,
    val wType: Int?
) : Parcelable

@Parcelize
data class WeatherDomain(
    val wDescription: String,
    val wIcon: String,
    val wId: Int,
    val wMain: String
) : Parcelable

@Parcelize
data class WindDomain(
    val wDeg: Double,
    val wSpeed: Double
) : Parcelable
