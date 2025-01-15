package com.sobolevkir.aipostcard.data.storage

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import androidx.core.net.toUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException
import javax.inject.Inject

class FileStorage @Inject constructor(private val context: Context) {

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

    suspend fun saveToGallery(imageStringUri: String): Boolean {
        return withContext(Dispatchers.IO) {
            val contentResolver = context.contentResolver
            val fileName = "generated_${System.currentTimeMillis()}.jpg"
            val appName = context.applicationInfo.loadLabel(context.packageManager).toString()
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                put(
                    MediaStore.MediaColumns.RELATIVE_PATH,
                    "${Environment.DIRECTORY_PICTURES}/$appName"
                )
            }
            val galleryUri =
                contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                    ?: return@withContext false
            contentResolver.openInputStream(imageStringUri.toUri())?.use { inputStream ->
                contentResolver.openOutputStream(galleryUri)?.use { outputStream ->
                    inputStream.copyTo(outputStream)
                } ?: return@withContext false
            } ?: return@withContext false
            true
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