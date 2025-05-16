package com.sobolevkir.aipostcard.presentation.screen.generate

import com.sobolevkir.aipostcard.domain.model.GenerationErrorType
import com.sobolevkir.aipostcard.domain.model.GenerationResponse
import com.sobolevkir.aipostcard.domain.model.Style

data class GenerateUiState(
    val prompt: String = "",
    val negativePrompt: String = "",
    val styles: List<Style> = emptyList(),
    val selectedStyle: Style? = null,
    val result: GenerationResponse? = null,
    val error: GenerationErrorType? = null,
    val isGenerating: Boolean = false,
    val isFullScreenOpened: Boolean = false,
)