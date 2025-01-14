package com.sobolevkir.aipostcard.di

import android.content.Context
import com.google.gson.Gson
import com.sobolevkir.aipostcard.data.storage.FileStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

    @Provides
    @Singleton
    fun provideFileStorage(@ApplicationContext context: Context): FileStorage = FileStorage(context)

}