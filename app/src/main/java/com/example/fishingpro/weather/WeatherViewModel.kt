package com.example.fishingpro.weather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fishingpro.data.domain.LocalWeatherDomain
import com.example.fishingpro.data.domain.WeatherDomain
import com.example.fishingpro.data.source.repository.WeatherRepository
import com.example.fishingpro.user.UserViewModel

class WeatherViewModel(
    private val weatherRepository: WeatherRepository,
    private val localWeatherDomain: LocalWeatherDomain
) : ViewModel() {

    private val _currentWeatherDomain: MutableLiveData<LocalWeatherDomain>
        get() = MutableLiveData(localWeatherDomain)
    val currentWeatherDomain: LiveData<LocalWeatherDomain>
        get() = _currentWeatherDomain

    private val _currentWeather = MutableLiveData<WeatherDomain>()
    val currentWeather: LiveData<WeatherDomain>
        get() = _currentWeather

    init {
        //TODO add check for emty
        if (localWeatherDomain.wWeather.isNotEmpty()) {
            _currentWeather.value = localWeatherDomain.wWeather[0]
        }
    }

    /**
     * Factory for constructing WeatherViewModel with parameter
     */
    class WeatherViewModelFactory(
        private val weatherRepository: WeatherRepository,
        private val weatherDomain: LocalWeatherDomain
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return WeatherViewModel(weatherRepository, weatherDomain) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}