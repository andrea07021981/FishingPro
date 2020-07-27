package com.example.fishingpro.data.source

import com.example.fishingpro.data.Result
import com.example.fishingpro.data.domain.LocalWeatherDomain
import com.example.fishingpro.data.domain.WeatherDomain

interface WeatherSource {

    suspend fun getLiveWeather(lat: Double, lon: Double): Result<LocalWeatherDomain>

    suspend fun getLiveWeeklyWeather(lat: Double, lon: Double): Result<List<LocalWeatherDomain>>
}