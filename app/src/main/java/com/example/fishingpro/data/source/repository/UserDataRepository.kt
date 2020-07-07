package com.example.fishingpro.data.source.repository

import android.app.Application
import com.example.fishingpro.data.Result
import com.example.fishingpro.data.source.UserDataSource
import com.example.fishingpro.data.source.remote.datasource.UserRemoteDataSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserDataRepository(
    private val userRemoteDataSource: UserDataSource,
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
    override suspend fun retrieveUser(email: String, password: String): Result<FirebaseUser> {
        return withContext(ioDispatcher) {
            userRemoteDataSource.getUser(email, password)
        }
    }

    override suspend fun saveUser(email: String, password: String) {
        return withContext(ioDispatcher) {
            userRemoteDataSource.saveUser(email, password)
        }
    }
}