package com.example.fishingpro.data.source

import com.example.fishingpro.data.Result
import com.google.firebase.auth.FirebaseUser

interface UserDataSource {

    suspend fun getUser(email: String, password: String): Result<FirebaseUser>

    suspend fun saveUser(email: String, password: String): Result<FirebaseUser>
}