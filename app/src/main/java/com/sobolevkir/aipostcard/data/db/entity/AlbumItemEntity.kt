package com.sobolevkir.aipostcard.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "album_items_table")
data class AlbumItemEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0L,

    @ColumnInfo(name = "image_string_uri")
    val imageStringUri: String,

    @ColumnInfo(name = "prompt")
    val prompt: String,

    @ColumnInfo(name = "negative_prompt")
    val negativePrompt: String? = null,

    @ColumnInfo(name = "time_stamp")
    val timeStamp: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "uuid")
    val uuid: String,

    @ColumnInfo(name = "thumb_string_uri")
    val thumbStringUri: String,

    @ColumnInfo(name = "style_title")
    val styleTitle: String,

)