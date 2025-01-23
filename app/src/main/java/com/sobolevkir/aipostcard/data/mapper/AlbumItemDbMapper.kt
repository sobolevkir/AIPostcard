package com.sobolevkir.aipostcard.data.mapper

import com.sobolevkir.aipostcard.data.db.entity.AlbumItemEntity
import com.sobolevkir.aipostcard.domain.model.AlbumItem

object AlbumItemDbMapper {

    fun map(entity: AlbumItemEntity): AlbumItem {
        return AlbumItem(
            id = entity.id,
            uuid = entity.uuid,
            imageStringUri = entity.imageStringUri,
            prompt = entity.prompt,
            negativePrompt = entity.negativePrompt,
            timeStamp = entity.timeStamp
        )
    }

}