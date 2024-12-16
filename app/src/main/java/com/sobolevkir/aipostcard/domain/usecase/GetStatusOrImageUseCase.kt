package com.sobolevkir.aipostcard.domain.usecase

import com.sobolevkir.aipostcard.data.repository.FBImageGenerationRepositoryImpl
import javax.inject.Inject

class GetStatusOrImageUseCase @Inject constructor(
    private val repository: FBImageGenerationRepositoryImpl
) {

    fun invoke(id: String) = repository.getStatusOrImage(id)

}