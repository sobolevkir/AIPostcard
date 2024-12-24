package com.sobolevkir.aipostcard.di

import com.sobolevkir.aipostcard.data.mapper.ImageGenerationResultMapper
import com.sobolevkir.aipostcard.data.mapper.ImageStylesMapper
import com.sobolevkir.aipostcard.data.mapper.ToDomainMapper
import com.sobolevkir.aipostcard.data.network.model.ImageGenerationResultDto
import com.sobolevkir.aipostcard.data.network.model.ImageStyleDto
import com.sobolevkir.aipostcard.domain.model.ImageGenerationResult
import com.sobolevkir.aipostcard.domain.model.ImageStyle
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DtoModule {

    @Binds
    @Singleton
    abstract fun bindImageGenerationResultMapper(impl: ImageGenerationResultMapper):
            ToDomainMapper<ImageGenerationResultDto, ImageGenerationResult>

    @Binds
    @Singleton
    abstract fun bindImageStylesMapper(impl: ImageStylesMapper):
            ToDomainMapper<List<ImageStyleDto>, List<ImageStyle>>

}