package com.example.fishingpro.data.source.local.datasource

import com.example.fishingpro.data.Result
import com.example.fishingpro.data.domain.LocalUser
import com.example.fishingpro.data.source.UserSource
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@ExperimentalCoroutinesApi
class UserLocalDataSource @Inject constructor(
) : UserSource {
    override suspend fun getUser(email: String, password: String): Flow<Result<FirebaseUser>> {
        TODO("Not yet implemented")
    }

    override suspend fun getCompleteUser(userUID: String): Flow<Result<LocalUser?>> {
        TODO("Not yet implemented")
    }

    override suspend fun saveUser(
        email: String,
        password: String,
        firstName: String,
        lastName: String
    ): Flow<Result<FirebaseUser>> {
        TODO("Not yet implemented")
    }

    override suspend fun logUserOut(coroutineContext: CoroutineContext) {
        TODO("Not yet implemented")
    }

}