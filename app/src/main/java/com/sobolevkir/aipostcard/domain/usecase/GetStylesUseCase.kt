package com.sobolevkir.aipostcard.domain.usecase

import com.sobolevkir.aipostcard.domain.api.GenerationRepository
import javax.inject.Inject

class GetStylesUseCase @Inject constructor(private val repository: GenerationRepository) {

    operator fun invoke() = repository.getStyles()

}