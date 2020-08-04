package com.example.fishingpro.data.source

import com.example.fishingpro.data.Result
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

@ExperimentalCoroutinesApi
interface UserSource {

    suspend fun getUser(email: String, password: String): Flow<Result<FirebaseUser>>

    suspend fun saveUser(email: String, password: String): Flow<Result<FirebaseUser>>
}