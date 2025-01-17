package com.sobolevkir.aipostcard.data.external

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.FileProvider
import com.sobolevkir.aipostcard.R
import com.sobolevkir.aipostcard.domain.ExternalNavigator
import java.io.File
import java.net.URI
import javax.inject.Inject

class ExternalNavigatorImpl @Inject constructor(private val context: Context) : ExternalNavigator {

    override fun shareImage(imageStringUri: String) {
        try {
            val imageFile = File(URI(imageStringUri))
            val contentUri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                imageFile
            )
            Intent(Intent.ACTION_SEND).run {
                type = "image/*"
                putExtra(Intent.EXTRA_STREAM, contentUri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                val shareIntent =
                    Intent.createChooser(this, context.getString(R.string.action_share))
                startIntent(shareIntent)
            }
        } catch (e: Exception) {
            Log.e("ExternalNavigator", "Ошибка: ${e.message}", e)
        }
    }

    private fun startIntent(intent: Intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

}