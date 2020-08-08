package com.example.fishingpro.data.source.repository

import android.app.Application
import android.content.Context
import com.example.fishingpro.data.Result
import com.example.fishingpro.data.source.UserSource
import com.example.fishingpro.data.source.local.datasource.UserLocalDataSource
import com.example.fishingpro.data.source.remote.datasource.UserRemoteDataSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ExperimentalCoroutinesApi
class UserDataRepository @Inject constructor(
    private val userRemoteDataSource: UserSource,
    val userLocalDataSource: UserLocalDataSource
) : UserRepository{

    override suspend fun retrieveUser(email: String, password: String, ioDispatcher: CoroutineDispatcher): Flow<Result<FirebaseUser>> {
        return withContext(ioDispatcher) {
            userRemoteDataSource.getUser(email, password)
        }
    }

    override suspend fun saveUser(email: String, password: String, ioDispatcher: CoroutineDispatcher): Flow<Result<FirebaseUser>> {
        return withContext(ioDispatcher) {
            userRemoteDataSource.saveUser(email, password)
        }
    }
}