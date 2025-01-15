package com.sobolevkir.aipostcard.data.storage

import android.content.Context
import android.net.Uri
import android.util.Base64
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException
import javax.inject.Inject

class FileStorage @Inject constructor(context: Context) {

    private val cacheDir: File = context.cacheDir

    suspend fun saveBase64ImageToCache(fileName: String, base64String: String): String? {
        return withContext(Dispatchers.IO) {
            try {
                val file = File(cacheDir, "$fileName.jpg")
                val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
                file.outputStream().use { outputStream ->
                    outputStream.write(decodedBytes)
                }
                Uri.fromFile(file).toString()
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
        }
    }

    suspend fun deleteFile(uri: String) {
        val file = Uri.parse(uri).path?.let {
            File(it).delete()
        }
    }

    suspend fun cleanCache() {
        withContext(Dispatchers.IO) {
            try {
                val currentTime = System.currentTimeMillis()
                cacheDir.listFiles()?.forEach { file ->
                    if (file.isFile && currentTime - file.lastModified() > MAX_CACHE_FILE_AGE_MILLIS) {
                        Log.d("FILESTORAGE", "Удаляем файл ${file.nameWithoutExtension}")
                        file.delete()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    companion object {
        private const val MAX_CACHE_FILE_AGE_MILLIS = 7 * 24 * 60 * 60 * 1000L
    }

}