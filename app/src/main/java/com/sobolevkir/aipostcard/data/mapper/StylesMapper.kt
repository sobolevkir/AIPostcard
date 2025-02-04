package com.sobolevkir.aipostcard.data.mapper

import com.sobolevkir.aipostcard.data.network.dto.StyleDto
import com.sobolevkir.aipostcard.domain.model.Style

object StylesMapper {
    fun map(dto: List<StyleDto>): List<Style> {
        return dto.map {
            Style(
                styleImageUrl = it.image,
                name = it.name,
                titleRu = it.title,
                titleEn = it.titleEn
            )
        }
    }
}