package com.example.fishingpro.data.source.remote.datasource

import com.example.fishingpro.data.Result
import com.example.fishingpro.data.await
import com.example.fishingpro.data.source.UserDataSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRemoteDataSource internal constructor(
    private val firebaseAuth: FirebaseAuth,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : UserDataSource{

    override suspend fun getUser(email: String, password: String): Result<FirebaseUser> = withContext(ioDispatcher){
        try {
            val authResultAwait = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            if (authResultAwait.user != null) {
                return@withContext Result.Success(authResultAwait.user!!)
            } else {
                return@withContext return@withContext Result.Error("Login Failed")
            }
        } catch (e: Exception) {
            //Print the message but send a login failed info
            e.printStackTrace()
            return@withContext Result.Error("Login Failed")
        }
    }

    override suspend fun saveUser(email: String, password: String): Result<FirebaseUser> = withContext(ioDispatcher) {
        try {
            val authResultAwait = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            if (authResultAwait.user != null) {
                return@withContext Result.Success(authResultAwait.user!!)
            } else {
                return@withContext Result.Error("Login Failed")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext Result.Error("Login Failed")
        }
    }
}