package com.example.fishingpro.data.source.remote.datatranferobject

import com.example.fishingpro.data.domain.*
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkLocalWeather(
    val base: String,
    val clouds: Clouds,
    val cod: Int,
    val coord: Coord,
    val dt: Int,
    val id: Int,
    val main: Main,
    val name: String,
    val sys: Sys,
    val timezone: Int,
    val weather: List<Weather>,
    val wind: Wind
)

@JsonClass(generateAdapter = true)
data class Main(
    @Json(name = "feels_like") val feelsLike: Double,
    val humidity: Int,
    val pressure: Int,
    val temp: Double,
    @Json(name = "temp_max") val tempMax: Double,
    @Json(name = "temp_min") val tempMin: Double
)

@JsonClass(generateAdapter = true)
data class Clouds(
    val all: Int
)

@JsonClass(generateAdapter = true)
data class Coord(
    val lat: Int,
    val lon: Int
)

@JsonClass(generateAdapter = true)
data class Sys(
    val country: String,
    val id: Int,
    val message: Double,
    val sunrise: Int,
    val sunset: Int,
    val type: Int
)

@JsonClass(generateAdapter = true)
data class Weather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)

@JsonClass(generateAdapter = true)
data class Wind(
    val deg: Double,
    val speed: Double
)

fun NetworkLocalWeather.asDomainModel(): LocalWeatherDomain {
    return LocalWeatherDomain(
        wBase =  base,
        wClouds = CloudsDomain(clouds.all),
        wCod = cod,
        wCoord = CoordDomain(coord.lat, coord.lon),
        wDt = dt,
        wId = id,
        wMain = MainDomain(main.feelsLike, main.humidity, main.pressure, main.temp, main.tempMax, main.tempMin),
        wName = name,
        wSys = SysDomain(sys.country, sys.id, sys.message, sys.sunrise, sys.sunset, sys.type),
        wTimezone = timezone,
        wWeather = weather.map {
            WeatherDomain(it.description, it.icon, it.id, it.main)
        },
        wWind = WindDomain(wind.deg, wind.speed)
    )
}