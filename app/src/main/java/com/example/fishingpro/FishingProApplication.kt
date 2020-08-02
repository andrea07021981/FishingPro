package com.example.fishingpro

import android.R.id
import android.app.Application
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase


class FishingProApplication : Application() {

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate() {
        //TODO ADD https://developer.android.com/training/dependency-injection/hilt-android
        super.onCreate()
        // Obtain the FirebaseAnalytics instance.
        firebaseAnalytics = Firebase.analytics
        // Test
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN) {
            param(FirebaseAnalytics.Param.VALUE, "1")
        }
    }
}