package com.example.fishingpro.data.source.remote.datasource

import android.util.Log
import com.example.fishingpro.data.Result
import com.example.fishingpro.data.await
import com.example.fishingpro.data.domain.LocalUser
import com.example.fishingpro.data.source.UserSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@ExperimentalCoroutinesApi
class UserRemoteDataSource @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : UserSource{

    override suspend fun getUser(email: String, password: String): Flow<Result<FirebaseUser>> = flow {
        try {
            emit(Result.Loading)
            val authResultAwait = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            if (authResultAwait.user != null) {
                emit(Result.Success(authResultAwait.user!!))
            } else {
                emit(Result.Error("Login Failed"))
            }
        } catch (e: Exception) {
            //Print the message but send a login failed info
            e.printStackTrace()
            emit(Result.Error("Login Error"))
        }
    }

    override suspend fun getCompleteUser(userUID: String): Flow<Result<LocalUser?>> = flow {
        try {
            emit(Result.Loading)
            val document = firestore.collection("User").document(userUID).get().await()
            if (document.data.isNullOrEmpty()) {
                emit(Result.Error("No Data"))
            } else {
                emit(Result.Success(document.toObject(LocalUser::class.java)))
            }
        } catch (e: FirebaseFirestoreException) {
            emit(Result.ExError(e))
        }
    }

    override suspend fun saveUser(email: String, password: String): Flow<Result<FirebaseUser>> = flow{
        try {
            emit(Result.Loading)
            val authResult = coroutineScope {
                return@coroutineScope firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .await()
            }
            if (authResult.user != null) {
                //Save the realdb
                val dbUser = hashMapOf(
                    "FirstName" to "Pippo",
                    "LastName" to "Franco"
                )

                val saveUser = firestore.collection("User").document(authResult.user!!.uid).set(
                    dbUser
                )
                val getUser = firestore.collection("User").document(authResult.user!!.uid).get()
                saveUser.await()
                val user = getUser.await()
                if (user.data.isNullOrEmpty()) {
                    throw Exception()
                } else {
                    emit(Result.Success(authResult.user!!))
                }
            } else {
                emit(Result.Error("Login Save Failed"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error("Login Save Failed"))
        }
    }
}