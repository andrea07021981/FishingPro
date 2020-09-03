package com.example.fishingpro.work

import android.app.Application
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class RefreshFishData(
    context: Context,
    params: WorkerParameters,
    val application: Application
) : CoroutineWorker(context, params){

    override suspend fun doWork(): Result {
        TODO("Not yet implemented")
    }
}