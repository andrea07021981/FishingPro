package com.example.fishingpro.data.source.repository

import com.example.fishingpro.data.Result
import com.example.fishingpro.data.domain.LocalCatch
import com.example.fishingpro.data.source.CalendarSource
import com.example.fishingpro.data.source.remote.datasource.CalendarRemoteDataSource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

class CalendarDataRepository @Inject constructor(
    private val calendarRemoteDataSource: CalendarSource
) : CalendarRepository{

    @Throws(Exception::class)
    override suspend fun retrieveCatches(
        userId: String,
        ioDispatcher: CoroutineDispatcher
    ): Flow<Result<List<LocalCatch?>>> {
        return withContext(ioDispatcher) {
            calendarRemoteDataSource.retrieveCatches(userId)
        }
    }
}