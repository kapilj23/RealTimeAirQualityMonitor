package com.kapil.aqiapp.di

import com.kapil.aqiapp.data.remote.AqiApiService
import com.kapil.aqiapp.data.repository.AqiRepositoryImpl
import com.kapil.aqiapp.domain.repository.AqiRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.waqi.info/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideAqiApiService(retrofit: Retrofit): AqiApiService {
        return retrofit.create(AqiApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideAqiRepository(impl: AqiRepositoryImpl): AqiRepository {
        return impl
    }
}