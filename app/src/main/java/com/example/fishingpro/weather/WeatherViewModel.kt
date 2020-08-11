package com.example.fishingpro.weather

import androidx.lifecycle.*
import com.example.fishingpro.data.Result
import com.example.fishingpro.data.domain.LocalWeatherDomain
import com.example.fishingpro.data.domain.WeatherDomain
import com.example.fishingpro.data.source.repository.WeatherRepository
import com.example.fishingpro.user.UserViewModel
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val weatherRepository: WeatherRepository,
    private val localWeatherDomain: LocalWeatherDomain
) : ViewModel() {

    private val _currentWeatherDomain: MutableLiveData<LocalWeatherDomain>
        get() = MutableLiveData(localWeatherDomain)
    val currentWeatherDomain: LiveData<LocalWeatherDomain>
        get() = _currentWeatherDomain

    private val _weeklyWeatherDomain = MutableLiveData<List<LocalWeatherDomain>?>()
    val weeklyWeatherDomain: LiveData<List<LocalWeatherDomain>?>
        get() = _weeklyWeatherDomain

    private val _currentWeather = MutableLiveData<WeatherDomain>()
    val currentWeather: LiveData<WeatherDomain>
        get() = _currentWeather

    init {
        //TODO add check for emty
        if (localWeatherDomain.wWeather.isNotEmpty()) {
            _currentWeather.value = localWeatherDomain.wWeather.getOrNull(0)
            loadWeeklyWeather(LatLng(localWeatherDomain.wCoord.wLat, localWeatherDomain.wCoord.wLon))
        }
    }

    private fun loadWeeklyWeather(latLon: LatLng) {
        viewModelScope.launch {
            val liveWeatherResult = weatherRepository.retrieveLiveWeeklyWeather(latLon.latitude, latLon.longitude)
            if (liveWeatherResult is Result.Success) {
                _weeklyWeatherDomain.value = liveWeatherResult.data
            } else {
                //TODO manage it with some live errors
            }
        }
    }
}