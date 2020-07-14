package com.example.fishingpro.data.source.remote.service.weather

import com.example.fishingpro.data.source.remote.datatranferobject.NetworkLocalWeather
import com.example.fishingpro.data.source.remote.service.weather.ApiKey.API_KEY
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("weather")
    suspend fun getLocalWeather(@Query("lat") lat: Double, @Query("lon") lon: Double, @Query("appid") key: String = API_KEY): NetworkLocalWeather
}
