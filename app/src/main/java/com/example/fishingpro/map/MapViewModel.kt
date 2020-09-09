package com.example.fishingpro.map

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.fishingpro.data.Result
import com.example.fishingpro.data.source.repository.FishRepository
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MapViewModel @ViewModelInject constructor(
    fishRepository: FishRepository,
    @Assisted userId: SavedStateHandle
) : ViewModel() {

    private val catches = fishRepository.retrieveCatches(
        userId.get<String>("userId") ?: "0"
    )

    init {

        loadLocations()
    }

    private fun loadLocations() {
        viewModelScope.launch {
            catches.collect { catches ->
                if (catches is Result.Success) {
                    catches.data.forEach {
                        //TODO add markers in live data
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