package com.example.fishingpro.data.source.repository

import com.example.fishingpro.data.Result
import com.example.fishingpro.data.domain.LocalUser
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

@ExperimentalCoroutinesApi
interface UserRepository {

    suspend fun retrieveUser(email: String, password: String, ioDispatcher: CoroutineDispatcher = Dispatchers.IO): Flow<Result<FirebaseUser>>

    suspend fun retrieveCompleteUser(userUID: String, ioDispatcher: CoroutineDispatcher = Dispatchers.IO): Flow<Result<LocalUser?>>

    suspend fun saveUser(email: String, password: String, firstName: String, lastName: String, ioDispatcher: CoroutineDispatcher = Dispatchers.IO): Flow<Result<FirebaseUser>>
}