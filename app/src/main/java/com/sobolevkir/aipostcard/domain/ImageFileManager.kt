package com.sobolevkir.aipostcard.domain

interface ImageFileManager {

    suspend fun saveBase64ToCache(fileName: String, base64String: String): String?

    suspend fun saveToGallery(imageStringUri: String): Boolean

    suspend fun deleteFile(uri: String)

    suspend fun cleanCache()

}