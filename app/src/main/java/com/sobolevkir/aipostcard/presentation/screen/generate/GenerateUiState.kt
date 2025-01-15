package com.sobolevkir.aipostcard.presentation.screen.generate

import com.sobolevkir.aipostcard.domain.model.ErrorType
import com.sobolevkir.aipostcard.domain.model.Style

data class GenerateUiState(
    val prompt: String = "",
    val negativePrompt: String = "",
    val styles: List<Style> = emptyList(),
    val selectedStyle: Style? = null,
    val generatedImage: String? = null,
    val error: ErrorType? = null,
    val isGenerating: Boolean = false,
    val isCensored: Boolean = false,
    val isImageSaved: Boolean = false
)