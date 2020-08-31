package com.example.fishingpro.data.source.repository

import com.example.fishingpro.data.Result
import com.example.fishingpro.data.domain.LocalUser
import com.example.fishingpro.data.source.UserSource
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*

@ExperimentalCoroutinesApi
class FakeUserDataRepositoryTest(
    private val userRemoteDataSource: UserSource, //Add fake data source
    private val userLocalDataSource: UserSource
) : UserRepository{

    @Before
    fun setUp() {
    }

    override suspend fun retrieveUser(
        email: String,
        password: String,
        ioDispatcher: CoroutineDispatcher
    ): Flow<Result<FirebaseUser>> {
        TODO("Not yet implemented")
    }

    override suspend fun retrieveCompleteUser(
        userUID: String,
        ioDispatcher: CoroutineDispatcher
    ): Flow<Result<LocalUser?>> {
        TODO("Not yet implemented")
    }

    override suspend fun saveUser(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        ioDispatcher: CoroutineDispatcher
    ): Flow<Result<FirebaseUser>> {
        TODO("Not yet implemented")
    }

    override suspend fun logOut() {
        TODO("Not yet implemented")
    }
}