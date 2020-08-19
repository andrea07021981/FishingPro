package com.example.fishingpro.login

import android.util.Log
import android.util.Patterns
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.fishingpro.Event
import com.example.fishingpro.constant.Authenticated
import com.example.fishingpro.constant.Authenticating
import com.example.fishingpro.constant.InvalidAuthentication
import com.example.fishingpro.constant.LoginAuthenticationStates
import com.example.fishingpro.data.Result
import com.example.fishingpro.data.source.repository.UserRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

//Inherit from AndroidViewModel we don't need to use a CustomViewmodelFactory for passing the application
@ExperimentalCoroutinesApi
class SignUpViewModel @ViewModelInject constructor(
    private val repository: UserRepository
) : ViewModel() {

    companion object {
        private val TAG = SignUpViewModel::class.java.simpleName
    }

    var emailValue = MutableLiveData<String>()
    var errorEmail = MutableLiveData<Boolean>()
    var passwordValue = MutableLiveData<String>()
    var errorPassword = MutableLiveData<Boolean>()
    var firstNameValue = MutableLiveData<String>()
    var errorFirstName = MutableLiveData<Boolean>()
    var lastNameValue = MutableLiveData<String>()
    var errorLastName = MutableLiveData<Boolean>()

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
        if (checkValidValues()) {
            viewModelScope.launch {
                //If the user is correctly created, it also logged in so we can move directly to the home page
                repository.saveUser(
                    emailValue.value.toString(),
                    passwordValue.value.toString(),
                    firstNameValue.value.toString(),
                    lastNameValue.value.toString()).onEach { result ->
                    when (result) {
                        is Result.Success -> _loginAuthenticationState.value = Authenticated(user = result.data)
                        is Result.Error -> _loginAuthenticationState.value = InvalidAuthentication(result.message)
                        is Result.ExError -> _loginAuthenticationState.value = InvalidAuthentication(result.exception.toString())
                        is Result.Loading -> _loginAuthenticationState.value = Authenticating()
                    }
                }
                .catch { e ->
                    e.printStackTrace()
                }
                .onCompletion {
                    Log.d(SignUpViewModel.TAG, "Done")
                }
                .collect {
                    println(it)
                }
            }
        }
    }

    private fun checkValidValues (): Boolean {
        errorEmail.value = !Patterns.EMAIL_ADDRESS.matcher(emailValue.value.toString()).matches()
        errorPassword.value = passwordValue.value.isNullOrEmpty()
        errorFirstName.value = firstNameValue.value.isNullOrEmpty()
        errorLastName.value = lastNameValue.value.isNullOrEmpty()
        return !(errorEmail.value!! || errorPassword.value!! || errorFirstName.value!! || errorLastName.value!!)
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