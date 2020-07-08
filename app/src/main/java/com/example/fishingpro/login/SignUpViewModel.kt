package com.example.fishingpro.login

import android.util.Patterns
import androidx.lifecycle.*
import com.example.fishingpro.Event
import com.example.fishingpro.constant.Authenticated
import com.example.fishingpro.constant.Authenticating
import com.example.fishingpro.constant.InvalidAuthentication
import com.example.fishingpro.constant.LoginAuthenticationStates
import com.example.fishingpro.data.Result
import com.example.fishingpro.data.source.repository.UserRepository
import kotlinx.coroutines.*

//Inherit from AndroidViewModel we don't need to use a CustomViewmodelFactory for passing the application
class SignUpViewModel(
    private val repository: UserRepository
) : ViewModel() {

    var emailValue = MutableLiveData<String>()
    var errorEmail = MutableLiveData<Boolean>()
    var passwordValue = MutableLiveData<String>()
    var errorPassword = MutableLiveData<Boolean>()

    private val _loginEvent = MutableLiveData<Event<Unit>>()
    val loginEvent: LiveData<Event<Unit>>
        get() = _loginEvent

    private val _loginAuthenticationState = MutableLiveData<LoginAuthenticationStates>()
    val loginAuthenticationState: LiveData<LoginAuthenticationStates>
        get() = _loginAuthenticationState

    init {
        emailValue.value = ""
        passwordValue.value = ""
    }

    fun onSignUpClick(){
        if (!checkValues()) {
            viewModelScope.launch {
                _loginAuthenticationState.value = Authenticating()
                val result = repository.saveUser(emailValue.value.toString(), passwordValue.value.toString())
                //If the user is correctly created, it also logged in so we can move directly to the home page
                when (result) {
                    is Result.Success -> _loginAuthenticationState.value = Authenticated(user = result.data)
                    is Result.Error -> _loginAuthenticationState.value = InvalidAuthentication(result.message)
                    is Result.ExError -> _loginAuthenticationState.value = InvalidAuthentication(result.exception.toString())
                    else -> _loginAuthenticationState.value = null
                }
            }
        }
    }

    private fun checkValues (): Boolean {
        errorEmail.value = !Patterns.EMAIL_ADDRESS.matcher(emailValue.value.toString()).matches()
        errorPassword.value = passwordValue.value.isNullOrEmpty()
        return errorEmail.value!! && errorPassword.value!!
    }

    /**
     * Factory for constructing SignUpViewModel with parameter
     */
    class SignUpViewModelFactory(
        private val dataRepository: UserRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SignUpViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return SignUpViewModel(dataRepository) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}