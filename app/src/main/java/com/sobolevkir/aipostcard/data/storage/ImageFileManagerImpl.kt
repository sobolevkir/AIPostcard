package com.sobolevkir.aipostcard.data.storage

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import com.sobolevkir.aipostcard.domain.api.ImageFileManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException
import javax.inject.Inject

class ImageFileManagerImpl @Inject constructor(private val context: Context) : ImageFileManager {

    private val cacheDir: File = context.cacheDir

    override suspend fun saveBase64ToCache(fileName: String, base64String: String): String? {
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

    override suspend fun copyImageToAlbum(stringUri: String): String? {
        return withContext(Dispatchers.IO) {
            try {
                val sourceFile = File(Uri.parse(stringUri).path ?: return@withContext null)
                if (!sourceFile.exists()) return@withContext null
                val albumDir = File(context.getExternalFilesDir(null), ALBUM_DIRECTORY_NAME)
                if (!albumDir.exists()) albumDir.mkdirs()
                val destinationFile = File(albumDir, sourceFile.name)
                sourceFile.inputStream().use { input ->
                    destinationFile.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
                Uri.fromFile(destinationFile).toString()
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
        }
    }

    override suspend fun saveToDeviceGallery(imageStringUri: String): Boolean {
        return withContext(Dispatchers.IO) {
            val contentResolver = context.contentResolver
            val fileName = Uri.parse(imageStringUri).lastPathSegment
                ?: "generated_${System.currentTimeMillis()}.jpg"
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
            contentResolver.openInputStream(Uri.parse(imageStringUri))?.use { inputStream ->
                contentResolver.openOutputStream(galleryUri)?.use { outputStream ->
                    inputStream.copyTo(outputStream)
                } ?: return@withContext false
            } ?: return@withContext false
            true
        }
    }

    override suspend fun deleteFile(uri: String) {
        withContext(Dispatchers.IO) {
            try {
                Uri.parse(uri).path?.let {
                    File(it).delete()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    override suspend fun cleanCache() {
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
        private const val MAX_CACHE_FILE_AGE_MILLIS = 3 * 24 * 60 * 60 * 1000L
        private const val ALBUM_DIRECTORY_NAME = "album"
    }

}