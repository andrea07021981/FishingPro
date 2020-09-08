package com.example.fishingpro.fish

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.fishingpro.data.Result
import com.example.fishingpro.data.domain.LocalDailyCatch
import com.example.fishingpro.data.source.repository.FishRepository
import kotlinx.coroutines.flow.transform
import java.util.*

class FishViewModel @ViewModelInject constructor(
    private val fishRepository: FishRepository,
    @Assisted val userId: SavedStateHandle
) : ViewModel() {

    companion object {
        private val TAG = FishViewModel::class.java.simpleName
        const val MONTH_NONE: Long = -1
    }
    private val _monthFilter = MutableLiveData<Long?>(MONTH_NONE)

    private val _activeFilter = MutableLiveData<Boolean>(false)
    val activeFilter: LiveData<Boolean>
        get() = _activeFilter

    //With transform we can emit only success
    private val _catches: LiveData<List<LocalDailyCatch?>> =
            fishRepository.retrieveCatches(
                userId.get<String>("userId") ?: "0"
            ).transform { value ->
                if (value is Result.Success) {
                    emit(value.data)
                }
            }.asLiveData(viewModelScope.coroutineContext)

    // This is the observable property, it changes every time the month filter changes (the original values never changes)
    val catches = Transformations.switchMap(_monthFilter) {
        if (it != null && it >= 0) {
            _activeFilter.value = true
            return@switchMap _catches.map { list ->
                list.filter { catch ->
                    with(Calendar.getInstance()) {
                        time = catch?.date ?: Date()
                        get(Calendar.MONTH).toLong() == it
                    }
                }
            }
        } else {
            _activeFilter.value = false
            return@switchMap _catches
        }
    }

    fun refreshListByMonth(monthId: Long) {
        _monthFilter.value = monthId
    }

    fun resetFilter() {
        _monthFilter.value = MONTH_NONE
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