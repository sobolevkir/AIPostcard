package com.sobolevkir.aipostcard.ui.screen.imagegeneration

sealed interface UiState {

    data object Initial : UiState

    data object Loading : UiState

    data class Success(val outputText: String) : UiState

    data class Error(val errorMessage: String) : UiState

}