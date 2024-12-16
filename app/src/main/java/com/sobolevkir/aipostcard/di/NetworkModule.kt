package com.sobolevkir.aipostcard.di

import com.sobolevkir.aipostcard.BuildConfig
import com.sobolevkir.aipostcard.data.network.ApiConstants
import com.sobolevkir.aipostcard.data.network.ApiErrorHandler
import com.sobolevkir.aipostcard.data.network.AuthInterceptor
import com.sobolevkir.aipostcard.data.network.FBApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideBaseUrl(): String = ApiConstants.BASE_URL

    @Provides
    fun provideAuthInterceptor(): AuthInterceptor {
        return AuthInterceptor(apiKey = BuildConfig.apiKey, apiSecret = BuildConfig.apiSecret)
    }

    @Provides
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()
    }

    @Provides
    fun provideFusionBrainApiService(
        baseUrl: String,
        httpClient: OkHttpClient
    ): FBApiService {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FBApiService::class.java)
    }

    @Provides
    fun provideApiErrorHandler(): ApiErrorHandler = ApiErrorHandler()

}
