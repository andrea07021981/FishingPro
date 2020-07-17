package com.example.fishingpro.data.source.remote.datasource

import com.example.fishingpro.data.Result
import com.example.fishingpro.data.domain.LocalWeatherDomain
import com.example.fishingpro.data.source.WeatherSource
import com.example.fishingpro.data.source.remote.datatranferobject.asDomainModel
import com.example.fishingpro.data.source.remote.service.weather.WeatherService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WeatherRemoteDataSource(
    private val weatherService: WeatherService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : WeatherSource {

    override suspend fun getLiveWeather(lat: Double, lon: Double): Result<LocalWeatherDomain> = withContext(ioDispatcher) {
        try {
            val weatherResult = weatherService.getLocalWeather(lat, lon)
            if (weatherResult.id != 0) {
                return@withContext Result.Success(weatherResult.asDomainModel())
            } else {
                return@withContext Result.Error("Login Failed")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext Result.ExError(e)
        }
    }
}