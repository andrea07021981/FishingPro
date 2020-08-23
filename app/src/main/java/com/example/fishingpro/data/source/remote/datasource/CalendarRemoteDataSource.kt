package com.example.fishingpro.data.source.remote.datasource

import com.example.fishingpro.data.Result
import com.example.fishingpro.data.source.CalendarSource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CalendarRemoteDataSource @Inject constructor(
    val firestore: FirebaseFirestore
) : CalendarSource {
    override suspend fun retrieveCatches(userId: String): Flow<Result<List<Unit>>> {
        TODO("Not yet implemented, change unit with data")
    }
}