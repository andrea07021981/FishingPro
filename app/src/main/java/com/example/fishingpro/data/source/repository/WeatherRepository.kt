package com.example.fishingpro.data.source.repository

import com.example.fishingpro.data.Result
import com.example.fishingpro.data.domain.LocalWeatherDomain
import com.example.fishingpro.data.domain.WeatherDomain

interface WeatherRepository {

    suspend fun retrieveLiveWeather(lat: Double, lon: Double): Result<LocalWeatherDomain>
}