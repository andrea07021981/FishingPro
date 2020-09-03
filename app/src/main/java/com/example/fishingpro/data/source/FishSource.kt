package com.example.fishingpro.data.source

import com.example.fishingpro.data.Result
import com.example.fishingpro.data.domain.LocalCatch
import com.example.fishingpro.data.domain.LocalDailyCatch
import kotlinx.coroutines.flow.Flow

interface FishSource {

    fun retrieveCatches(userId: String): Flow<Result<List<LocalDailyCatch?>>>
}