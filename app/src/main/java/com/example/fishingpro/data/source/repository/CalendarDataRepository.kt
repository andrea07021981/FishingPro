package com.example.fishingpro.data.source.repository

import com.example.fishingpro.data.Result
import com.example.fishingpro.data.domain.LocalCatch
import com.example.fishingpro.data.source.CalendarSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CalendarDataRepository @Inject constructor(
    private val calendarRemoteDataSource: CalendarSource
) : CalendarRepository{

    override fun retrieveCatches(
        userId: String,
        ioDispatcher: CoroutineDispatcher
    ): Flow<Result<List<LocalCatch?>>> {
        return calendarRemoteDataSource.retrieveCatches(userId)
    }
}