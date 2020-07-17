package com.example.fishingpro.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fishingpro.data.source.repository.UserRepository
import com.example.fishingpro.login.SignUpViewModel

class MapViewModel(

) : ViewModel() {



    /**
     * Factory for constructing MapViewModel with parameter
     */
    class MapViewModelFactory(
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MapViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MapViewModel() as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}