package com.sobolevkir.aipostcard.data.mapper

import com.sobolevkir.aipostcard.data.network.ApiConstants.STATUS_DONE
import com.sobolevkir.aipostcard.data.network.ApiConstants.STATUS_INITIAL
import com.sobolevkir.aipostcard.data.network.ApiConstants.STATUS_PROCESSING
import com.sobolevkir.aipostcard.data.network.model.GenerationResultDto
import com.sobolevkir.aipostcard.domain.model.GenerationResult
import com.sobolevkir.aipostcard.domain.model.GenerationStatus
import com.sobolevkir.aipostcard.util.Base64Decoder
import javax.inject.Inject

class GenerationResultMapper @Inject constructor(private val decoder: Base64Decoder) :
    ToDomainMapper<GenerationResultDto, GenerationResult> {
    override fun toDomain(dto: GenerationResultDto): GenerationResult {
        return GenerationResult(
            uuid = dto.uuid,
            status = convertStatus(dto.status),
            generatedImagesUri = dto.images.map { decoder.decodeBase64ToStringUri(it) ?: "" },
            censored = dto.censored
        )
    }

    private fun convertStatus(status: String): GenerationStatus {
        return when (status.uppercase()) {
            STATUS_INITIAL, STATUS_PROCESSING -> GenerationStatus.IN_PROGRESS
            STATUS_DONE -> GenerationStatus.DONE
            else -> GenerationStatus.FAIL
        }
    }
}

