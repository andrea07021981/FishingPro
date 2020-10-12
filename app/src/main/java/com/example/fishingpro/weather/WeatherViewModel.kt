package com.example.fishingpro.weather

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.fishingpro.data.Result
import com.example.fishingpro.data.domain.LocalWeatherDomain
import com.example.fishingpro.data.domain.WeatherDomain
import com.example.fishingpro.data.source.repository.WeatherRepository
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch

class WeatherViewModel @ViewModelInject constructor(
    private val weatherRepository: WeatherRepository,
    @Assisted private val savedStateHandle: SavedStateHandle // IMP It contains the navargs and it avoid manual injection
) : ViewModel() {

    private var localWeatherDomain = savedStateHandle.get<LocalWeatherDomain>("localWeatherDomain") ?: LocalWeatherDomain()

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