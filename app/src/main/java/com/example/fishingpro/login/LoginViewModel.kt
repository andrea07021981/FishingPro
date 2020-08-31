package com.example.fishingpro.login

import android.util.Log
import androidx.annotation.VisibleForTesting
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fishingpro.Event
import com.example.fishingpro.constant.*
import com.example.fishingpro.data.Result
import com.example.fishingpro.data.source.repository.UserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

//TODO create a abstract class for all the viewmodels https://medium.com/kinandcartacreated/kotlin-coroutines-in-android-part-7-65f65f85824d
// Add these handlers for lauch coroutines
// ADd DSL Domain-Specific Language
/*
private val handler = CoroutineExceptionHandler { context, exception ->
        println("Exception thrown somewhere within parent or child: $exception.")
    }

    private val childExceptionHandler = CoroutineExceptionHandler{ _, exception ->
        println("Exception thrown in one of the children: $exception.")
    }

 */
@ExperimentalCoroutinesApi
class LoginViewModel @ViewModelInject constructor(
    private val repository: UserRepository
) : ViewModel() {

    companion object {
        private val TAG = LoginViewModel::class.java.simpleName
    }

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
            //WE could use transform and collect, since we don't need to lunch in another scope
            repository.retrieveUser(emailValue.value.toString(), passwordValue.value.toString())
                .onEach { result ->
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
                .onCompletion { Log.d(TAG, "Done") }
                .launchIn(viewModelScope)
        }
    }

    fun resetState() {
        _loginAuthenticationState.value = Idle()
    }
}