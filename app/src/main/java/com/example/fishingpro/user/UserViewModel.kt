package com.example.fishingpro.user

import android.util.Log
import android.view.View
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.fishingpro.Event
import com.example.fishingpro.data.Result
import com.example.fishingpro.data.domain.LocalWeatherDomain
import com.example.fishingpro.data.domain.WeatherDomain
import com.example.fishingpro.data.source.repository.UserRepository
import com.example.fishingpro.data.source.repository.WeatherRepository
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.lang.Exception
import java.util.*

@ExperimentalCoroutinesApi
class UserViewModel @ViewModelInject constructor(
    private val weatherRepository: WeatherRepository,
    private val userRepository: UserRepository
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
                loadUserInfo()
                //End jobs
                _status.postValue(View.GONE)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //TODO manage all states result
    private suspend fun loadUserInfo() = withContext(Dispatchers.IO){
        userRepository.retrieveCompleteUser(Firebase.auth.uid.toString()).onEach {result ->
            when (result) {
                is Result.Success -> Log.d("TAG","Test->>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ${result.data}")
                else -> print("To complete")
            }
        }.launchIn(viewModelScope)
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

    fun backToLogin() {
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
}