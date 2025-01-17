package com.sobolevkir.aipostcard.data.mapper

import com.sobolevkir.aipostcard.data.network.ApiConstants.STATUS_DONE
import com.sobolevkir.aipostcard.data.network.ApiConstants.STATUS_INITIAL
import com.sobolevkir.aipostcard.data.network.ApiConstants.STATUS_PROCESSING
import com.sobolevkir.aipostcard.data.network.dto.GenerationResultDto
import com.sobolevkir.aipostcard.domain.model.GenerationResult
import com.sobolevkir.aipostcard.domain.model.GenerationStatus

object GenerationResultMapper {
    fun map(dto: GenerationResultDto, imageStringUri: String? = null): GenerationResult {
        val convertedStatus = convertStatus(dto.status)
        return GenerationResult(
            uuid = dto.uuid,
            status = convertedStatus,
            imageStringUri = imageStringUri,
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

