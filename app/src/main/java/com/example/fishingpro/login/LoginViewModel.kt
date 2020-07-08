package com.example.fishingpro.login

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.*
import com.example.fishingpro.Event
import com.example.fishingpro.constant.Authenticated
import com.example.fishingpro.constant.Authenticating
import com.example.fishingpro.constant.InvalidAuthentication
import com.example.fishingpro.constant.LoginAuthenticationStates
import com.example.fishingpro.data.Result
import com.example.fishingpro.data.source.repository.UserRepository
import kotlinx.coroutines.launch


class LoginViewModel(
    private val repository: UserRepository
) : ViewModel() {

    var emailValue = MutableLiveData<String>()
        @VisibleForTesting set // this allow us to use this set only for test
    var passwordValue = MutableLiveData<String>()
        @VisibleForTesting set // this allow us to use this set only for test
    var errorPassword = MutableLiveData<Boolean>()
    var errorEmail = MutableLiveData<Boolean>()

    private val _loginAuthenticationState = MutableLiveData<LoginAuthenticationStates>()
    val loginAuthenticationState: LiveData<LoginAuthenticationStates>
        get() = _loginAuthenticationState

    private val _signUpEvent = MutableLiveData<Event<Unit>>()
    val signUpEvent: LiveData<Event<Unit>>
        get() = _signUpEvent

    init {
        emailValue.value = "a@a.com"
        passwordValue.value = "aaaaaa"
    }

    fun onSignInClick(){
        errorEmail.value = emailValue.value.isNullOrEmpty()
        errorPassword.value = passwordValue.value.isNullOrEmpty()
        if (errorEmail.value == false && errorPassword.value == false) {
            doLogin()
        }
    }

    fun onSignUpClick() {
        _signUpEvent.value = Event(Unit)
    }

    private fun doLogin() {
        viewModelScope.launch {
            _loginAuthenticationState.value = Authenticating()
            val result = repository.retrieveUser(emailValue.value.toString(), passwordValue.value.toString())
            when (result) {
                is Result.Success -> _loginAuthenticationState.value = Authenticated(user = result.data)
                is Result.Error -> _loginAuthenticationState.value = InvalidAuthentication(result.message)
                is Result.ExError -> _loginAuthenticationState.value = InvalidAuthentication(result.exception.toString())
                else -> _loginAuthenticationState.value = null
            }
        }
    }

    fun resetState() {
        _loginAuthenticationState.value = null
    }

    class LoginViewModelFactory(
        private val repository: UserRepository
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return LoginViewModel(repository) as T
            }
            throw IllegalArgumentException("Unable to construct ViewModel")
        }
    }
}