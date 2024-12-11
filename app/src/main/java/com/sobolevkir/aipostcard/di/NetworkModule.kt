package com.sobolevkir.aipostcard.di

import com.sobolevkir.aipostcard.BuildConfig
import com.sobolevkir.aipostcard.data.network.ApiConstants
import com.sobolevkir.aipostcard.data.network.ApiErrorHandler
import com.sobolevkir.aipostcard.data.network.AuthInterceptor
import com.sobolevkir.aipostcard.data.network.FusionBrainApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideBaseUrl(): String = ApiConstants.BASE_URL

    @Provides
    @Singleton
    fun provideAuthInterceptor(): AuthInterceptor {
        return AuthInterceptor(apiKey = BuildConfig.apiKey, apiSecret = BuildConfig.apiSecret)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideFusionBrainApiService(
        baseUrl: String,
        httpClient: OkHttpClient
    ): FusionBrainApiService {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FusionBrainApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideApiErrorHandler(): ApiErrorHandler = ApiErrorHandler()

}
