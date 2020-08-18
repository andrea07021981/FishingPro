package com.example.fishingpro.di

import android.content.Context
import com.example.fishingpro.BuildConfig
import com.example.fishingpro.data.source.UserSource
import com.example.fishingpro.data.source.WeatherSource
import com.example.fishingpro.data.source.local.datasource.UserLocalDataSource
import com.example.fishingpro.data.source.remote.datasource.UserRemoteDataSource
import com.example.fishingpro.data.source.remote.datasource.WeatherRemoteDataSource
import com.example.fishingpro.data.source.remote.service.weather.WeatherService
import com.example.fishingpro.data.source.repository.UserDataRepository
import com.example.fishingpro.data.source.repository.UserRepository
import com.example.fishingpro.data.source.repository.WeatherDataRepository
import com.example.fishingpro.data.source.repository.WeatherRepository
import com.example.fishingpro.map.MapFragment
import com.example.fishingpro.user.UserFragment
import com.example.fishingpro.weather.WeatherFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
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
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = Firebase.auth

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore = Firebase.firestore

    @Provides
    fun provideUserRemoteDataSource(firebaseAuth: FirebaseAuth, firebaseFirestore: FirebaseFirestore): UserSource {
        return UserRemoteDataSource(
            firebaseAuth,
            firebaseFirestore
        )
    }

    @Singleton
    @Provides
    fun provideUserDataRepository(
        @ApplicationContext appContext: Context,
        userRemoteDataSource: UserRemoteDataSource,
        userLocalDataSource: UserLocalDataSource): UserRepository {
        return UserDataRepository(userRemoteDataSource, userLocalDataSource)
    }

    // Weather DI
    @Provides
    fun provideWeatherRemoteDataSource(weatherService: WeatherService): WeatherSource {
        return WeatherRemoteDataSource(
            weatherService
        )
    }

    //TODO add a factory for fragments
    @Singleton
    @Provides
    fun provideMainFragmentFactory(): WeatherFragment {
        return WeatherFragment()
    }

    @Singleton
    @Provides
    fun provideUserFragmentFactory(): UserFragment {
        return UserFragment()
    }

    @Singleton
    @Provides
    fun provideMapFragmentFactory(): MapFragment {
        return MapFragment()
    }

    @Provides
    fun provideWeatherRepository(weatherSource: WeatherSource): WeatherRepository {
        return WeatherDataRepository(
            weatherSource
        )
    }
}

@Module
@InstallIn(ApplicationComponent::class)
object NetworkModule {

    @Provides
    fun provideBaseUrl() = BuildConfig.BASE_WEATHER_URL
    @Provides
    @Singleton
    fun provideKotlinJsonAdapterFactory(): KotlinJsonAdapterFactory = KotlinJsonAdapterFactory()

    @Provides
    @Singleton
    fun provideInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
    }

    @Provides
    @Singleton
    fun provideOkHttp(interceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient
            .Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .build()
    }

    @Provides
    fun provideMoshi(kotlinJsonAdapterFactory: KotlinJsonAdapterFactory): Moshi {
        return Moshi.Builder()
            .add(kotlinJsonAdapterFactory)
            .build()
    }

    @Provides
    @Singleton
    fun provideMoshiConverterFactory(moshi: Moshi): MoshiConverterFactory =
        MoshiConverterFactory.create(moshi)

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, moshiConverterFactory: MoshiConverterFactory, BASE_URL: String): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(moshiConverterFactory)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .build()
    }

    @Provides
    fun provideRetrofitService(retrofit: Retrofit): WeatherService = retrofit.create(
        WeatherService::class.java)

}

