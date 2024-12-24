package com.sobolevkir.aipostcard.util

import android.content.Context
import android.net.Uri
import android.util.Base64
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class Base64Decoder(private val context: Context) {

    fun decodeBase64ToStringUri(base64String: String): String? {
        return try {
            val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)

            val outputFile = createTempFile()

            FileOutputStream(outputFile).use {
                it.write(decodedBytes)
            }

            Uri.fromFile(outputFile).toString()
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    private fun createTempFile(): File {
        val tempDir = context.cacheDir
        return File(tempDir, "image_${System.currentTimeMillis()}.png")
    }
}