package com.example.fishingpro.home

import android.util.Log
import android.view.View
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.fishingpro.Event
import com.example.fishingpro.data.Result
import com.example.fishingpro.data.domain.LocalDailyCatch
import com.example.fishingpro.data.domain.LocalUser
import com.example.fishingpro.data.domain.LocalWeatherDomain
import com.example.fishingpro.data.domain.WeatherDomain
import com.example.fishingpro.data.source.repository.FishRepository
import com.example.fishingpro.data.source.repository.UserRepository
import com.example.fishingpro.data.source.repository.WeatherRepository
import com.example.fishingpro.data.succeeded
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.lang.Exception

@ExperimentalCoroutinesApi
class HomeViewModel @ViewModelInject constructor(
    private val weatherRepository: WeatherRepository,
    private val userRepository: UserRepository,
    private val fishRepository: FishRepository,
    private val auth: FirebaseAuth,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        private val TAG = HomeViewModel::class.java.simpleName
    }

    private val _userEvent = MutableLiveData<Event<Unit>>()
    val userEvent: LiveData<Event<Unit>>
        get() = _userEvent

    private val _fishEvent = MutableLiveData<Event<String>>()
    val fishEvent: LiveData<Event<String>>
        get() = _fishEvent

    private val _userLogOutEvent = MutableLiveData<Event<Boolean>>()
    val userLogOutEvent: LiveData<Event<Boolean>>
        get() = _userLogOutEvent

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

    private val _userLogged = MutableLiveData<LocalUser?>()
    val userLogged: LiveData<LocalUser?>
        get() = _userLogged

    private val _catches = MutableLiveData<List<LocalDailyCatch>>()
    val catches: LiveData<List<LocalDailyCatch>>
        get() = _catches

    /* TODO USE LIVE DATA AND COROUTINES FOR LOADING DATA IMMEDIATELY WITHOUT THE METHOD INITDATA
    val calendarEvents: LiveData<Calendar> = liveData {
        val data = repositiry.loadData() // loadUser is a suspend function.
        emit(data)
    }
    */

    //TODO add to a future abstract class, add thwow new exceptio and check the effect in children jobs
    private val handler = CoroutineExceptionHandler { _, exception ->
        println("Exception thrown within parent: $exception.")
    }

    private val childExceptionHandler = CoroutineExceptionHandler{ _, exception ->
        println("Exception thrown in one of the children: $exception.")
    }

    /**
     * Init the various sections. We can use launch and parallel coroutines (context of parent if not declared) because they are not related each other, we don't need the withContext or coroutineScope
     */
    private fun initData(latLon: LatLng) {
        try {
            viewModelScope.launch(handler) {
                //With supervisor, if one fails the other jobs keep working
                supervisorScope {
                    // Request weather //TODO can remove the suspend functions and coroutinescope, not nee
                    val jobWeather = launch(childExceptionHandler) { loadWeather(latLon) }
                    // Request data from user
                    val jobUser = launch(childExceptionHandler) { loadUserInfo() }
                    // Request catches data
                    val jobFish = launch(childExceptionHandler) { loadCatchesData() }
                }
            }.invokeOnCompletion {
                //End jobs
                _status.postValue(View.GONE)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun logOutUser() {
        viewModelScope.launch {
            _userLogOutEvent.value = try {
                userRepository.logOut()
                Event(true)
            } catch (e: Exception) {
                e.printStackTrace()
                Event(false)
            }
        }
    }

    private suspend fun loadCatchesData() = coroutineScope{
        fishRepository.retrieveCatches(auth.uid.toString())
            .onEach {
                check(it !is Result.ExError) // Throw an exception
            }
            .catch {
                Log.d(TAG, "Exception loading")
            }
            .transform {
                if (it.succeeded ) { //Emit only success
                    emit(it as Result.Success)
                }
            }
            .onCompletion {
                Log.d(TAG, "Data loaded")
            }
            .collect {
                _catches.value = it.data
            }
    }

    /**
     * Change the context to main
     */
    private suspend fun loadUserInfo() = withContext(Dispatchers.Main){
        userRepository.retrieveCompleteUser(auth.uid.toString())
            .onEach { result ->
                check(result !is Result.Error && result !is Result.ExError)
                when (result) {
                    is Result.Success -> _userLogged.value = result.data
                    is Result.Loading -> _userLogged.value = LocalUser()
                }
            }
            .catch { e ->
                e.printStackTrace()
                _userLogged.value = null
            }
            .onCompletion {
                Log.d(TAG, "Done")
            }
            .launchIn(this@HomeViewModel.viewModelScope)
    }

    /**
     * It requests the weather, we can keep the parent context with coroutineScope
     */
    @Throws(Exception::class)
    private suspend fun loadWeather(latLon: LatLng) = coroutineScope {
        val liveWeatherResult = weatherRepository.retrieveLiveWeather(latLon.latitude, latLon.longitude)
        if (liveWeatherResult is Result.Success) {
            _currentWeather.postValue(liveWeatherResult.data)
            _currentWeatherDetail.postValue(liveWeatherResult.data.wWeather.getOrNull(0))
        } else {
            //TODO manage it with some live errors
        }
    }

    fun userInfo() {
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

    fun goToCatches() {
        _fishEvent.value = Event(_userLogged.value!!.userUID)
    }
}