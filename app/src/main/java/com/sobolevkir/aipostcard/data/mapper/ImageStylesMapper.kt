package com.sobolevkir.aipostcard.data.mapper

import com.sobolevkir.aipostcard.data.network.model.ImageStyleDto
import com.sobolevkir.aipostcard.domain.model.ImageStyle
import javax.inject.Inject

class ImageStylesMapper @Inject constructor() : ToDomainMapper<List<ImageStyleDto>, List<ImageStyle>> {
    override fun toDomain(dto: List<ImageStyleDto>): List<ImageStyle> {
        return dto.map {
            ImageStyle(
                styleImageUrl = it.image,
                name = it.name,
                title = it.title
            )
        }
    }
}