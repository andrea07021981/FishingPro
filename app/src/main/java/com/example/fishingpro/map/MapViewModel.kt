package com.example.fishingpro.map

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.fishingpro.data.Result
import com.example.fishingpro.data.domain.LocalDailyCatch
import com.example.fishingpro.data.domain.LocalMapCatch
import com.example.fishingpro.data.domain.asDataMap
import com.example.fishingpro.data.source.repository.FishRepository
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MapViewModel @ViewModelInject constructor(
    fishRepository: FishRepository,
    auth: FirebaseAuth,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

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
                    catches.data.forEach { dailyCatch ->
                        val asDataMap = dailyCatch.asDataMap()
                        val catchesByLocation = asDataMap.groupBy { it.location }
                        catchesByLocation.keys.first()?.let { geoPoint ->
                            _catches.value = MarkerOptions()
                                .position(LatLng(geoPoint.latitude ?: 0.0, geoPoint.latitude ?: 0.0 ))
                                .title("Total catches: ${catchesByLocation[geoPoint]?.groupingBy { it.fishId}?.eachCount()}")
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