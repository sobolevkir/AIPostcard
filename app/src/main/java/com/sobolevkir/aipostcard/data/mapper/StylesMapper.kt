package com.sobolevkir.aipostcard.data.mapper

import com.sobolevkir.aipostcard.data.network.model.StyleDto
import com.sobolevkir.aipostcard.domain.model.Style
import javax.inject.Inject

class StylesMapper @Inject constructor() : ToDomainMapper<List<StyleDto>, List<Style>> {
    override fun toDomain(dto: List<StyleDto>): List<Style> {
        return dto.map {
            Style(
                styleImageUrl = it.image,
                name = it.name,
                title = it.title
            )
        }
    }
}