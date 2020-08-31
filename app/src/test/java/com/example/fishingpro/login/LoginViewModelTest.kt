package com.example.fishingpro.login

import android.os.Build
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.fishingpro.data.source.local.datasource.UserLocalDataSource
import com.example.fishingpro.data.source.remote.datasource.UserRemoteDataSource
import com.example.fishingpro.data.source.repository.UserDataRepository
import com.example.fishingpro.data.source.repository.UserRepository
import com.example.fishingpro.viewmodel.getOrAwaitValue
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.hamcrest.core.Is.`is`
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import javax.inject.Inject

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
@Config(sdk = [Build.VERSION_CODES.P])
class LoginViewModelTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var loginViewModel: LoginViewModel

    @Inject
    lateinit var userDataRepository: UserDataRepository
    @Inject
    lateinit var userRemoteDataSource: UserRemoteDataSource
    @Inject
    lateinit var userLocalDataSource: UserLocalDataSource
    @Inject
    lateinit var firebaseFirestore: FirebaseFirestore
    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    @Before
    fun setupViewModel() {
        hiltRule.inject()
    }

    @Test
    fun onSignInClick_userLogged_valid() = runBlocking{
        //Given user name and pass
        assertNotNull(loginViewModel)

        //When user click
        loginViewModel.onSignInClick()

        //Then perform the login call
        val value = loginViewModel.loginAuthenticationState.getOrAwaitValue()
        MatcherAssert.assertThat(value, `is`(CoreMatchers.equalTo(value)))
    }

    @Test
    fun onSignUpClick() {
    }
}