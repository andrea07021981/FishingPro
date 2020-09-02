package com.example.fishingpro.data.source.repository

import com.example.fishingpro.data.Result
import com.example.fishingpro.data.domain.LocalCatch
import com.example.fishingpro.data.domain.LocalDailyCatch
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

interface CalendarRepository {

    fun retrieveCatches(userId: String, ioDispatcher: CoroutineDispatcher = Dispatchers.IO): Flow<Result<List<LocalDailyCatch?>>>
}