package com.example.fishingpro.data.source.remote.datasource

import com.example.fishingpro.data.Result
import com.example.fishingpro.data.domain.LocalWeatherDomain
import com.example.fishingpro.data.source.WeatherSource
import com.example.fishingpro.data.source.remote.datatranferobject.asDomainModel
import com.example.fishingpro.data.source.remote.service.weather.WeatherService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WeatherRemoteDataSource @Inject constructor(
    private val weatherService: WeatherService
) : WeatherSource {

    override suspend fun getLiveWeather(lat: Double, lon: Double): Result<LocalWeatherDomain> = withContext(Dispatchers.IO) {
        try {
            //TODO manage unit with preferences
            val weatherResult = weatherService.getLocalWeather(lat, lon, "metric")
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

    override suspend fun getLiveWeeklyWeather(
        lat: Double,
        lon: Double
    ): Result<List<LocalWeatherDomain>> = withContext(Dispatchers.IO) {
        //TODO Fake data for first steps, API is not free
        try {
            val weatherResult = weatherService.getLocalWeather(lat, lon, "metric")
            val weatherList = mutableListOf<LocalWeatherDomain>()
            for (i in 0 until 7) {
                weatherList.add(weatherResult.asDomainModel().copy())
            }
            if (weatherList.isNullOrEmpty()) {
                return@withContext Result.Error("Login Failed")
            } else {
                return@withContext Result.Success(weatherList)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext Result.ExError(e)
        }
    }
}