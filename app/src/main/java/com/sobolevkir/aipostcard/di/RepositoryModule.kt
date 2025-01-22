package com.sobolevkir.aipostcard.di

import com.sobolevkir.aipostcard.data.repository.AlbumRepositoryImpl
import com.sobolevkir.aipostcard.data.repository.GenerationRepositoryImpl
import com.sobolevkir.aipostcard.domain.api.AlbumRepository
import com.sobolevkir.aipostcard.domain.api.GenerationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    @Singleton
    fun bindGenerationRepository(
        generationRepositoryImpl: GenerationRepositoryImpl
    ): GenerationRepository

    @Binds
    @Singleton
    fun bindAlbumRepository(
        albumRepositoryImpl: AlbumRepositoryImpl
    ): AlbumRepository

}
