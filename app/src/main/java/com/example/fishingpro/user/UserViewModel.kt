package com.example.fishingpro.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fishingpro.Event
import com.example.fishingpro.login.SignUpViewModel

class UserViewModel(

) : ViewModel() {

    private val _userEvent = MutableLiveData<Event<Unit>>()
    val userEvent: LiveData<Event<Unit>>
        get() = _userEvent

    fun backToRecipeList() {
        _userEvent.value = Event(Unit)
    }

    /**
     * Factory for constructing SignUpViewModel with parameter
     */
    class UserViewModelFactory(
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return UserViewModel() as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}