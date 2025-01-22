package com.sobolevkir.aipostcard.data.mapper

import com.sobolevkir.aipostcard.data.db.entity.AlbumItemEntity
import com.sobolevkir.aipostcard.domain.model.AlbumItem

object AlbumItemDbMapper {

    fun fromEntityToDomain(entity: AlbumItemEntity): AlbumItem {
        return AlbumItem(
            id = entity.id,
            imageStringUri = entity.imageStringUri,
            prompt = entity.prompt,
            negativePrompt = entity.negativePrompt,
            timeStamp = entity.timeStamp
        )
    }

    fun fromDomainToEntity(domain: AlbumItem): AlbumItemEntity {
        return AlbumItemEntity(
            id = domain.id,
            imageStringUri = domain.imageStringUri,
            prompt = domain.prompt,
            negativePrompt = domain.negativePrompt,
            timeStamp = domain.timeStamp
        )
    }
}