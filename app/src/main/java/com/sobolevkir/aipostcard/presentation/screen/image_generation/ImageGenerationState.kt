package com.sobolevkir.aipostcard.presentation.screen.image_generation

import com.sobolevkir.aipostcard.domain.model.ImageStyle

data class ImageGenerationState(
    val prompt: String = "",
    val negativePrompt: String = "",
    val imageStyles: List<ImageStyle> = emptyList(),
    val selectedStyle: ImageStyle? = null,
    val generatedImage: String? = null,
    val errorMessage: String? = null,
    val isGenerating: Boolean = false,
    val isPromptError: Boolean = false,
    val isGenerateButtonEnabled: Boolean = false
)