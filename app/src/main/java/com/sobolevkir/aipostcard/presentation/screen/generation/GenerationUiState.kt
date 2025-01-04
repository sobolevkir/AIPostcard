package com.sobolevkir.aipostcard.presentation.screen.generation

import com.sobolevkir.aipostcard.domain.model.Style

data class GenerationUiState(
    val prompt: String = "",
    val negativePrompt: String = "",
    val styles: List<Style> = emptyList(),
    val selectedStyle: Style? = null,
    val generatedImage: String? = null,
    val errorMessage: String? = null,
    val isGenerating: Boolean = false,
    val isCensored: Boolean = false,
    val isGenerateButtonEnabled: Boolean = false
)