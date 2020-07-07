package com.example.fishingpro.constant

import com.google.firebase.auth.FirebaseUser

sealed class LoginAuthenticationStates(open val message: String)

// Authentication failed
data class Authenticating(
    override val message: String = "Loggin in"
) : LoginAuthenticationStates(message)

// Initial state, the user needs to authenticate
data class Unauthenticated(
    override val message: String = "Not logged in"
) : LoginAuthenticationStates(message)

// Initial state, the user needs to authenticate
data class Authenticated(
    override val message: String = "Authentication ok",
    val user: FirebaseUser
) : LoginAuthenticationStates(message)

// Authentication failed
data class InvalidAuthentication(
    override val message: String = "Authentication error"
) : LoginAuthenticationStates(message)
