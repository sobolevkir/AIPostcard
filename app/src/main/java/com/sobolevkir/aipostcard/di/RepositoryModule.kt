package com.sobolevkir.aipostcard.di

import com.sobolevkir.aipostcard.data.repository.ImageGenerationRepositoryImpl
import com.sobolevkir.aipostcard.domain.ImageGenerationRepository
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
    fun bindImageGenerationRepository(
        imageGenerationRepositoryImpl: ImageGenerationRepositoryImpl
    ): ImageGenerationRepository

}
