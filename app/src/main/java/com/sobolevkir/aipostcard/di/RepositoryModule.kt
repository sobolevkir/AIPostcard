package com.sobolevkir.aipostcard.di

import com.sobolevkir.aipostcard.data.repository.FBImageGenerationRepositoryImpl
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
        fbImageGenerationRepositoryImpl: FBImageGenerationRepositoryImpl
    ): ImageGenerationRepository

}
