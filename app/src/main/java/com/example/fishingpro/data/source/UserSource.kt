package com.example.fishingpro.data.source

import com.example.fishingpro.data.Result
import com.example.fishingpro.data.domain.LocalUser
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

@ExperimentalCoroutinesApi
interface UserSource {

    suspend fun getUser(email: String, password: String): Flow<Result<FirebaseUser>>

    suspend fun getCompleteUser(userUID: String): Flow<Result<LocalUser?>>

    suspend fun saveUser(email: String, password: String, firstName: String, lastName: String): Flow<Result<FirebaseUser>>
}