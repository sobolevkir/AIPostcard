package com.sobolevkir.aipostcard.di

import android.content.Context
import com.google.gson.Gson
import com.sobolevkir.aipostcard.data.external.ExternalNavigatorImpl
import com.sobolevkir.aipostcard.data.storage.ImageFileManagerImpl
import com.sobolevkir.aipostcard.domain.ExternalNavigator
import com.sobolevkir.aipostcard.domain.ImageFileManager
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
    fun provideImageFileManager(@ApplicationContext context: Context): ImageFileManager {
        return ImageFileManagerImpl(context)
    }

    @Provides
    @Singleton
    fun provideExternalNavigator(@ApplicationContext context: Context): ExternalNavigator {
        return ExternalNavigatorImpl(context)
    }

}