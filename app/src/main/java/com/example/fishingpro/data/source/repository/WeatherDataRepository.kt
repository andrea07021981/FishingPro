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

class WeatherDataRepository(
    private val remoteDataSource: WeatherSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : WeatherRepository {

    companion object {

        @Volatile
        private var INSTANCE: WeatherDataRepository? = null

        fun getRepository(app: Application): WeatherDataRepository {
            return INSTANCE ?: synchronized(this) {
                return@synchronized WeatherDataRepository(
                    WeatherRemoteDataSource(ApiClient.retrofitWeatherService)
                ).also {
                    INSTANCE = it
                }
            }
        }
    }

    @Throws(Exception::class)
    override suspend fun retrieveLiveWeather(lat: Double, lon: Double): Result<LocalWeatherDomain> {
        return withContext(ioDispatcher) {
            remoteDataSource.getLiveWeather(lat, lon)
        }
    }
}