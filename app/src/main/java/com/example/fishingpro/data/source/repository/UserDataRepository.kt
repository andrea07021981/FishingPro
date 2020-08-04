package com.example.fishingpro.data.source.repository

import android.app.Application
import com.example.fishingpro.data.Result
import com.example.fishingpro.data.source.UserSource
import com.example.fishingpro.data.source.remote.datasource.UserRemoteDataSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

@ExperimentalCoroutinesApi
class UserDataRepository(
    private val userRemoteDataSource: UserSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : UserRepository{

    companion object {

        @Volatile
        private var INSTANCE: UserDataRepository? = null

        fun getRepository(app: Application): UserDataRepository {
            return INSTANCE ?: synchronized(this) {
                val firebaseAuth = FirebaseAuth.getInstance()
                return@synchronized UserDataRepository(
                    UserRemoteDataSource(
                        firebaseAuth
                    )
                ).also {
                    INSTANCE = it
                }
            }
        }

    }
    override suspend fun retrieveUser(email: String, password: String): Flow<Result<FirebaseUser>> {
        return withContext(ioDispatcher) {
            userRemoteDataSource.getUser(email, password)
        }
    }

    override suspend fun saveUser(email: String, password: String): Flow<Result<FirebaseUser>> {
        return withContext(ioDispatcher) {
            userRemoteDataSource.saveUser(email, password)
        }
    }
}