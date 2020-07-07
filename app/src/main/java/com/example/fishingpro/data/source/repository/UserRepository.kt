package com.example.fishingpro.data.source.repository

import com.example.fishingpro.data.Result
import com.google.firebase.auth.FirebaseUser

interface UserRepository {

    suspend fun retrieveUser(email: String, password: String): Result<FirebaseUser>

    suspend fun saveUser(email: String, password: String)
}