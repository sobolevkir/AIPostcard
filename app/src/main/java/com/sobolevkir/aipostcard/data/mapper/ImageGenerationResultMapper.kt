package com.sobolevkir.aipostcard.data.mapper

import com.sobolevkir.aipostcard.data.network.ApiConstants.STATUS_DONE
import com.sobolevkir.aipostcard.data.network.ApiConstants.STATUS_INITIAL
import com.sobolevkir.aipostcard.data.network.ApiConstants.STATUS_PROCESSING
import com.sobolevkir.aipostcard.data.network.model.ImageGenerationResultDto
import com.sobolevkir.aipostcard.domain.model.ImageGenerationResult
import com.sobolevkir.aipostcard.domain.model.ImageGenerationStatus

object ImageGenerationResultMapper {
    fun toDomain(dto: ImageGenerationResultDto): ImageGenerationResult {
        return ImageGenerationResult(
            uuid = dto.uuid,
            status = convertStatus(dto.status),
            generatedImages = dto.images,
            censored = dto.censored
        )
    }

    private fun convertStatus(status: String): ImageGenerationStatus {
        return when (status.uppercase()) {
            STATUS_INITIAL, STATUS_PROCESSING -> ImageGenerationStatus.IN_PROGRESS
            STATUS_DONE -> ImageGenerationStatus.DONE
            else -> ImageGenerationStatus.FAIL
        }
    }
}