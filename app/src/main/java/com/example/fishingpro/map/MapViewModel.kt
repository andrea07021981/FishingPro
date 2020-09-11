package com.example.fishingpro.map

import android.util.Log
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.fishingpro.data.Result
import com.example.fishingpro.data.domain.LocalDailyCatch
import com.example.fishingpro.data.domain.LocalMapCatch
import com.example.fishingpro.data.domain.asDataMap
import com.example.fishingpro.data.source.repository.FishRepository
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MapViewModel @ViewModelInject constructor(
    fishRepository: FishRepository,
    private val auth: FirebaseAuth,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        val TAG = MapViewModel::class.java.simpleName
    }

    private val _catchesResult = fishRepository.retrieveCatches(
        auth.uid.toString()
    )
    private val _catches = MutableLiveData<MarkerOptions>()
    val catches: LiveData<MarkerOptions>
        get() = _catches

    init {
        loadLocations()
    }

    //TODO add all markers and change _catches to list
    private fun loadLocations() {
        viewModelScope.launch {
            _catchesResult.collect { catches ->
                if (catches is Result.Success) {
                    catches.data
                        .asFlow()
                        .onEach {
                            delay(100) //TODO; BAD CHANGE. PROBLEM WITH TIME AND MULTIPLE MARKERS
                        }
                        .onCompletion {
                            Log.d(TAG, "DONE")
                        }
                        .collect { localDailyCatch ->
                            val asDataMap = localDailyCatch.asDataMap()
                            val catchesByLocation = asDataMap.groupBy { it.location }
                            catchesByLocation.keys.first()?.let { geoPoint ->
                                val totalCatches = catchesByLocation[geoPoint]?.groupingBy { it.userId}?.eachCount()
                                _catches.postValue(MarkerOptions()
                                    .position(LatLng(geoPoint.latitude, geoPoint.longitude))
                                    .title("Total catches: ${totalCatches?.getOrDefault(auth.uid.toString(), 0)}")
                                )
                            }
                        }
                }
            }
        }
    }
    /**
     * Factory for constructing MapViewModel with parameter
     */
    /*class MapViewModelFactory(
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MapViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MapViewModel() as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }*/
}