package com.sobolevkir.aipostcard.ui.screen.imagegeneration

sealed interface ImageGenerationUiState {

    data object Initial : ImageGenerationUiState

    data object Loading : ImageGenerationUiState

    data class Success(val outputText: String) : ImageGenerationUiState

    data class Error(val errorMessage: String) : ImageGenerationUiState

}