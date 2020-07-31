package com.example.fishingpro.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fishingpro.data.source.repository.UserRepository
import com.example.fishingpro.login.LoginViewModel

class CalendarViewModel(

) : ViewModel() {

    init {

    }
    //TODO from here fish calendar

    class CalendarViewModelFactory() : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CalendarViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CalendarViewModel() as T
            }
            throw IllegalArgumentException("Unable to construct ViewModel")
        }
    }
}