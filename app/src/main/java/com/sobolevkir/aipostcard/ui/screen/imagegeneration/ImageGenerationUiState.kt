package com.sobolevkir.aipostcard.ui.screen.imagegeneration

import android.graphics.Bitmap
import com.sobolevkir.aipostcard.domain.model.ImageStyle

data class ImageGenerationUiState(
    val prompt: String = "",
    val negativePrompt: String = "",
    val imageStyles: List<ImageStyle> = emptyList(),
    val errorMessage: String? = null,
    val selectedStyle: ImageStyle? = null,
    val isLoading: Boolean = false,
    val generatedImage: Bitmap? = null
)