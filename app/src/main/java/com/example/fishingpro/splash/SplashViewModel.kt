package com.example.fishingpro.splash

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fishingpro.Event
import com.example.fishingpro.R

class SplashViewModel : ViewModel() {

    val textScreen = R.string.flavored_app_name

    private val _loginEvent = MutableLiveData<Event<Unit>>()
    val loginEvent: LiveData<Event<Unit>>
        get() = _loginEvent

    init {
        val timer = object: CountDownTimer(3000, 1000) {
            override fun onTick(millisUntilFinished: Long) {}

            override fun onFinish() {
                _loginEvent.value = Event(Unit)
            }
        }
        timer.start()
    }
}