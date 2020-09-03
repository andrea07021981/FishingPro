package com.example.fishingpro.fish

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.fishingpro.data.Result
import com.example.fishingpro.data.domain.LocalDailyCatch
import com.example.fishingpro.data.source.repository.FishRepository

class FishViewModel @ViewModelInject constructor(
    private val fishRepository: FishRepository,
    @Assisted val userId: SavedStateHandle
) : ViewModel() {

    private val _catches: LiveData<Result<List<LocalDailyCatch?>>> =
            fishRepository.retrieveCatches(
                userId.get<String>("userId") ?: "0"
            ).asLiveData(viewModelScope.coroutineContext)

    val catches: LiveData<Result<List<LocalDailyCatch?>>>
        get() = _catches

    init {
    }

    /*class CalendarViewModelFactory() : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CalendarViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CalendarViewModel() as T
            }
            throw IllegalArgumentException("Unable to construct ViewModel")
        }
    }*/
}