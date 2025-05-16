package com.sobolevkir.aipostcard.data.mapper

import com.sobolevkir.aipostcard.data.network.ApiConstants.STATUS_DONE
import com.sobolevkir.aipostcard.data.network.ApiConstants.STATUS_INITIAL
import com.sobolevkir.aipostcard.data.network.ApiConstants.STATUS_PROCESSING
import com.sobolevkir.aipostcard.data.network.dto.GenerationResponseDto
import com.sobolevkir.aipostcard.domain.model.GenerationResponse
import com.sobolevkir.aipostcard.domain.model.GenerationStatus

object GenerationResultMapper {
    fun map(dto: GenerationResponseDto, imageStringUri: String? = null): GenerationResponse {
        val convertedStatus = convertStatus(dto.status)
        return GenerationResponse(
            uuid = dto.uuid,
            status = convertedStatus,
            imageStringUri = imageStringUri ?: "",
            censored = dto.result?.censored ?: false
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

