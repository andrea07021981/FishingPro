package com.example.fishingpro.data.source.repository

import com.example.fishingpro.data.Result
import com.example.fishingpro.data.domain.LocalDailyCatch
import com.example.fishingpro.data.source.FishSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FishDataRepository @Inject constructor(
    private val fishRemoteDataSource: FishSource
) : FishRepository{

    override fun retrieveCatches(
        userId: String,
        ioDispatcher: CoroutineDispatcher
    ): Flow<Result<List<LocalDailyCatch?>>> {
        return fishRemoteDataSource.retrieveCatches(userId)
    }
}