package com.example.fishingpro.calendar

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.fishingpro.data.Result
import com.example.fishingpro.data.domain.LocalCatch
import com.example.fishingpro.data.source.repository.CalendarRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@InternalCoroutinesApi
class CalendarViewModel @ViewModelInject constructor(
    private val calendarRepository: CalendarRepository,
    @Assisted val userId: SavedStateHandle
) : ViewModel() {

    private val _catches = MutableLiveData<Result<List<LocalCatch?>>>()
    val catches: LiveData<Result<List<LocalCatch?>>>
        get() = _catches

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            coroutineScope {
                userId.get<String>("userId")?.let {
                    calendarRepository.retrieveCatches(it)
                        .collect { data ->
                            _catches.value = data
                        }
                }
            }
        }
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