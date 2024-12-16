package com.sobolevkir.aipostcard.data.mapper

import com.sobolevkir.aipostcard.data.network.model.ImageStyleDto
import com.sobolevkir.aipostcard.domain.model.ImageStyle

object ImageStyleMapper {
    fun toDomainList(dtoList: List<ImageStyleDto>): List<ImageStyle> {
        return dtoList.map { dto ->
            ImageStyle(
                styleImageUrl = dto.image,
                name = dto.name,
                title = dto.title
            )
        }
    }
}