package com.example.fishingpro.user

import androidx.lifecycle.*
import com.example.fishingpro.Event
import com.example.fishingpro.data.Result
import com.example.fishingpro.data.domain.LocalWeatherDomain
import com.example.fishingpro.data.domain.WeatherDomain
import com.example.fishingpro.data.source.repository.WeatherRepository
import kotlinx.coroutines.launch

class UserViewModel(
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    private val _userEvent = MutableLiveData<Event<Unit>>()
    val userEvent: LiveData<Event<Unit>>
        get() = _userEvent
    private val _currentWeatherDetail = MutableLiveData<WeatherDomain>()
    val currentWeatherDetail: LiveData<WeatherDomain>
        get() = _currentWeatherDetail

    private val _currentWeather = MutableLiveData<LocalWeatherDomain>()
    val currentWeather: LiveData<LocalWeatherDomain>
        get() = _currentWeather

    init {
        loadWeather()
    }

    private fun loadWeather() {
        viewModelScope.launch {
            val liveWeatherResult = weatherRepository.retrieveLiveWeather(44.389339, 79.685516)
            if (liveWeatherResult is Result.Success) {
                _currentWeather.value = liveWeatherResult.data
                _currentWeatherDetail.value = liveWeatherResult.data.wWeather[0]
            } else {
                //TODO manage it with some live errors
            }
        }
    }

    fun backToRecipeList() {
        _userEvent.value = Event(Unit)
    }

    /**
     * Factory for constructing SignUpViewModel with parameter
     */
    class UserViewModelFactory(
        private val weatherRepository: WeatherRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return UserViewModel(weatherRepository) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}