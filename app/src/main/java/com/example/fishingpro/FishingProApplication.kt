package com.example.fishingpro

import android.app.Application
import androidx.work.*
import com.example.fishingpro.data.await
import com.example.fishingpro.data.source.remote.datatranferobject.NetworkLocalFish
import com.example.fishingpro.data.source.remote.datatranferobject.asFirebaseModel
import com.example.fishingpro.work.RefreshFishData
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.io.InputStream
import java.util.concurrent.TimeUnit

@HiltAndroidApp
class FishingProApplication : Application() {

    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private val applicationScope = CoroutineScope(Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        //FirebaseApp.initializeApp(this)
        // Obtain the FirebaseAnalytics instance.
        firebaseAnalytics = Firebase.analytics
        // Test
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN) {
            param(FirebaseAnalytics.Param.VALUE, "1")
        }
        delayedInit()
    }

    private fun delayedInit() {
        applicationScope.launch {
            setUpRecurringWork()
        }
    }

    private fun setUpRecurringWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .setRequiresBatteryNotLow(true)
            .setRequiresCharging(false)
            .apply {
                setRequiresDeviceIdle(false)
            }.build()

        val repeatingRequest
                = PeriodicWorkRequestBuilder<RefreshFishData>(1, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance().enqueueUniquePeriodicWork(
            RefreshFishData.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingRequest
        )
    }
}