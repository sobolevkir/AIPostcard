package com.sobolevkir.aipostcard.data.mapper

import com.sobolevkir.aipostcard.data.network.ApiConstants.STATUS_DONE
import com.sobolevkir.aipostcard.data.network.ApiConstants.STATUS_INITIAL
import com.sobolevkir.aipostcard.data.network.ApiConstants.STATUS_PROCESSING
import com.sobolevkir.aipostcard.data.network.model.ImageGenerationResultDto
import com.sobolevkir.aipostcard.domain.model.ImageGenerationResult
import com.sobolevkir.aipostcard.domain.model.ImageGenerationStatus
import com.sobolevkir.aipostcard.util.Base64Decoder
import javax.inject.Inject

class ImageGenerationResultMapper @Inject constructor(private val decoder: Base64Decoder) :
    ToDomainMapper<ImageGenerationResultDto, ImageGenerationResult> {
    override fun toDomain(dto: ImageGenerationResultDto): ImageGenerationResult {
        return ImageGenerationResult(
            uuid = dto.uuid,
            status = convertStatus(dto.status),
            generatedImagesUri = dto.images.map { decoder.decodeBase64ToStringUri(it) ?: "" },
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

