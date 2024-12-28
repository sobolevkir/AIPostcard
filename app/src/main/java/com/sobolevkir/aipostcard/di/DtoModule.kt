package com.sobolevkir.aipostcard.di

import com.sobolevkir.aipostcard.data.mapper.GenerationResultMapper
import com.sobolevkir.aipostcard.data.mapper.StylesMapper
import com.sobolevkir.aipostcard.data.mapper.ToDomainMapper
import com.sobolevkir.aipostcard.data.network.model.GenerationResultDto
import com.sobolevkir.aipostcard.data.network.model.StyleDto
import com.sobolevkir.aipostcard.domain.model.GenerationResult
import com.sobolevkir.aipostcard.domain.model.Style
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
    abstract fun bindGenerationResultMapper(impl: GenerationResultMapper):
            ToDomainMapper<GenerationResultDto, GenerationResult>

    @Binds
    @Singleton
    abstract fun bindStylesMapper(impl: StylesMapper):
            ToDomainMapper<List<StyleDto>, List<Style>>

}