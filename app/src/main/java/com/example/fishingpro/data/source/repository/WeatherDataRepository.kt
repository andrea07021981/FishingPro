package com.example.fishingpro.data.source.repository

import android.app.Application
import com.example.fishingpro.data.Result
import com.example.fishingpro.data.domain.LocalWeatherDomain
import com.example.fishingpro.data.domain.WeatherDomain
import com.example.fishingpro.data.source.WeatherSource
import com.example.fishingpro.data.source.remote.datasource.UserRemoteDataSource
import com.example.fishingpro.data.source.remote.datasource.WeatherRemoteDataSource
import com.example.fishingpro.data.source.remote.service.weather.ApiClient
import com.example.fishingpro.data.source.remote.service.weather.WeatherService
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

class WeatherDataRepository @Inject constructor(
    private val remoteDataSource: WeatherSource
) : WeatherRepository {

    @Throws(Exception::class)
    override suspend fun retrieveLiveWeather(lat: Double, lon: Double): Result<LocalWeatherDomain> {
        return withContext(Dispatchers.IO) {
            remoteDataSource.getLiveWeather(lat, lon)
        }
    }

    override suspend fun retrieveLiveWeeklyWeather(
        lat: Double,
        lon: Double
    ): Result<List<LocalWeatherDomain>> {
        return withContext(Dispatchers.IO) {
            remoteDataSource.getLiveWeeklyWeather(lat, lon)
        }
    }
}