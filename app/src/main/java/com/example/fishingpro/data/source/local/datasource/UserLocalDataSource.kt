package com.example.fishingpro.data.source.local.datasource

import com.example.fishingpro.data.Result
import com.example.fishingpro.data.source.UserSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ExperimentalCoroutinesApi
class UserLocalDataSource @Inject constructor(
) : UserSource {
    override suspend fun getUser(email: String, password: String): Flow<Result<FirebaseUser>> {
        TODO("Not yet implemented")
    }

    override suspend fun saveUser(email: String, password: String): Flow<Result<FirebaseUser>> {
        TODO("Not yet implemented")
    }

}