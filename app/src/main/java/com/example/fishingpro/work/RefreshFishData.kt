package com.example.fishingpro.work

import android.app.Application
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.fishingpro.data.await
import com.example.fishingpro.data.source.remote.datatranferobject.NetworkLocalFish
import com.example.fishingpro.data.source.remote.datatranferobject.asFirebaseModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.lang.Exception

class RefreshFishData(
    val context: Context,
    params: WorkerParameters,
    val application: Application
) : CoroutineWorker(context, params){

    companion object {
        const val WORK_NAME = "RefreshFishData"
    }
    override suspend fun doWork(): Result {
        return try {
            //Fake json with type fish, it updates the firestore db. The main actions will be get data from an api and save it to Fb
            val json = loadJSONFromAsset()
            if (json!!.isEmpty()) {
                throw Exception("Error reading data")
            } else {
                val gson = Gson()

                val data = gson.fromJson(json, NetworkLocalFish::class.java)
                val firebaseFirestore = Firebase.firestore
                data.forEach {
                    val firebaseModel = it.asFirebaseModel()
                    val dataToSave = mapOf(
                        "Id" to firebaseModel.fishId,
                        "Name" to firebaseModel.fishName,
                        "Description" to firebaseModel.fishDescription
                    )
                    firebaseFirestore.collection("Fish").add(dataToSave).await()
                }
            }
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    private fun loadJSONFromAsset(): String? {
        val json: String?
        json = try {
            val inputStream: InputStream = context.assets.open("fish.json")
            val size: Int = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            String(buffer, Charsets.UTF_8)
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return json
    }
}