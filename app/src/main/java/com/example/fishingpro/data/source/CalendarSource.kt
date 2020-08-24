package com.example.fishingpro.data.source

import com.example.fishingpro.data.Result
import com.example.fishingpro.data.domain.LocalCatch
import kotlinx.coroutines.flow.Flow

interface CalendarSource {

    suspend fun retrieveCatches(userId: String): Flow<Result<List<LocalCatch?>>>
}