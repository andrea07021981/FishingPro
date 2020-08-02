package com.example.fishingpro.user

import android.view.View
import androidx.lifecycle.*
import com.example.fishingpro.Event
import com.example.fishingpro.data.Result
import com.example.fishingpro.data.domain.LocalWeatherDomain
import com.example.fishingpro.data.domain.WeatherDomain
import com.example.fishingpro.data.source.repository.WeatherRepository
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.util.*

class UserViewModel(
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    private val _userEvent = MutableLiveData<Event<Unit>>()
    val userEvent: LiveData<Event<Unit>>
        get() = _userEvent

    private val _currentWeatherDetail = MutableLiveData<WeatherDomain>()
    val currentWeatherDetail: LiveData<WeatherDomain>
        get() = _currentWeatherDetail

    private val _currentWeather = MutableLiveData<LocalWeatherDomain?>()
    val currentWeather: LiveData<LocalWeatherDomain?>
        get() = _currentWeather

    private val _weatherEvent = MutableLiveData<Event<LocalWeatherDomain>>()
    val weatherEvent: LiveData<Event<LocalWeatherDomain>>
        get() = _weatherEvent

    private val _status = MutableLiveData(View.VISIBLE)
    val status: LiveData<Int>
        get() = _status

    var currentDate = System.currentTimeMillis()


    /* TODO USE LIVE DATA AND COROUTINES FOR LOADING DATA IMMEDIATELY WITHOUT THE METHOD INITDATA
    val calendarEvents: LiveData<Calendar> = liveData {
        val data = repositiry.loadData() // loadUser is a suspend function.
        emit(data)
    }
    */

    private fun initData(latLon: LatLng) {
        try {
            viewModelScope.launch {
                loadWeather(latLon)
                //Add other tasks here

                //End jobs
                _status.postValue(View.GONE)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Throws(Exception::class)
    private suspend fun loadWeather(latLon: LatLng) = withContext(Dispatchers.IO) {
        val liveWeatherResult = weatherRepository.retrieveLiveWeather(latLon.latitude, latLon.longitude)
        if (liveWeatherResult is Result.Success) {
            _currentWeather.postValue(liveWeatherResult.data)
            _currentWeatherDetail.postValue(liveWeatherResult.data.wWeather.getOrNull(0))
        } else {
            //TODO manage it with some live errors
        }
    }

    fun backToRecipeList() {
        _userEvent.value = Event(Unit)
    }

    fun updateLatLong(lat: Double, lon: Double) {
        initData(LatLng(lat, lon))
    }

    fun gpToWeatherDetail() {
        _currentWeather.value?.let {
            _weatherEvent.value = Event(_currentWeather.value!!)
        }
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