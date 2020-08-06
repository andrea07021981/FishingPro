package com.example.fishingpro.di

import android.content.Context
import com.example.fishingpro.data.source.UserSource
import com.example.fishingpro.data.source.remote.datasource.UserRemoteDataSource
import com.example.fishingpro.data.source.repository.UserDataRepository
import com.example.fishingpro.data.source.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.migration.DisableInstallInCheck
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Module
@InstallIn(ApplicationComponent::class)
abstract class AppModule {

    //TODO review bind and interfaces https://developer.android.com/training/dependency-injection/hilt-android#inject-interfaces
    @Binds
    abstract fun bindUserSource(
        userRemoteDataSource: UserRemoteDataSource
    ): UserSource
}

@ExperimentalCoroutinesApi
@Module
@InstallIn(ApplicationComponent::class)
object BaseModule {

    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    fun provideUserRemoteDataSource(firebaseAuth: FirebaseAuth): UserSource {
        return UserRemoteDataSource(
            firebaseAuth
        )
    }

    @Singleton
    @Provides
    fun provideUserDataRepository(@ApplicationContext appContext: Context): UserRepository {
        return UserDataRepository.getRepository(appContext)
    }
}
