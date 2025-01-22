package com.sobolevkir.aipostcard.di

import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import com.sobolevkir.aipostcard.data.db.AlbumDao
import com.sobolevkir.aipostcard.data.db.AppDatabase
import com.sobolevkir.aipostcard.data.external.ExternalNavigatorImpl
import com.sobolevkir.aipostcard.data.storage.ImageFileManagerImpl
import com.sobolevkir.aipostcard.domain.api.ExternalNavigator
import com.sobolevkir.aipostcard.domain.api.ImageFileManager
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
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "app_database.db")
            .build()
    }

    @Provides
    @Singleton
    fun provideAlbumDao(appDatabase: AppDatabase): AlbumDao {
        return appDatabase.getAlbumDao()
    }

    @Provides
    @Singleton
    fun provideExternalNavigator(@ApplicationContext context: Context): ExternalNavigator {
        return ExternalNavigatorImpl(context)
    }

}