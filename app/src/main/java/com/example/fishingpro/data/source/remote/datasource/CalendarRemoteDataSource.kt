package com.example.fishingpro.data.source.remote.datasource

import android.util.Log
import com.example.fishingpro.data.Result
import com.example.fishingpro.data.await
import com.example.fishingpro.data.domain.LocalCatch
import com.example.fishingpro.data.source.CalendarSource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import javax.inject.Inject

class CalendarRemoteDataSource @Inject constructor(
    val firestore: FirebaseFirestore
) : CalendarSource {
    override suspend fun retrieveCatches(userId: String): Flow<Result<List<LocalCatch?>>> = flow {
        emit(Result.Loading)
        try {
            val request = firestore.collection("Catch").whereEqualTo("UserId", userId).get().await()
            val dataMapped = request.documents.map {
                val singleCatch = it.toObject(LocalCatch::class.java)
                singleCatch?.CatchId = it.id
                singleCatch
            }
            emit(Result.Success(dataMapped))
        } catch (e: FirebaseFirestoreException) {
            emit(Result.ExError(e))
        }
    }
}