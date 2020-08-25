package com.example.fishingpro.calendar

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.fishingpro.data.Result
import com.example.fishingpro.data.domain.LocalCatch
import com.example.fishingpro.data.source.repository.CalendarRepository
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CalendarViewModel @ViewModelInject constructor(
    private val calendarRepository: CalendarRepository,
    @Assisted val userId: SavedStateHandle
) : ViewModel() {

    private val _catches: LiveData<Result<List<LocalCatch?>>> =
            calendarRepository.retrieveCatches(userId.get<String>("userId") ?: "0").asLiveData(viewModelScope.coroutineContext)

    val catches: LiveData<Result<List<LocalCatch?>>>
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