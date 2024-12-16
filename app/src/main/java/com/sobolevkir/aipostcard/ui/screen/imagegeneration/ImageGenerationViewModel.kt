package com.sobolevkir.aipostcard.ui.screen.imagegeneration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sobolevkir.aipostcard.domain.ImageGenerationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImageGenerationViewModel @Inject constructor(
    private val imageGenerationRepository: ImageGenerationRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<ImageGenerationUiState> =
        MutableStateFlow(ImageGenerationUiState.Initial)
    val uiState: StateFlow<ImageGenerationUiState> =
        _uiState.asStateFlow()

    fun sendPrompt(
        prompt: String
    ) {
        _uiState.value = ImageGenerationUiState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            try {
                _uiState.value = ImageGenerationUiState.Success(prompt)
            } catch (e: Exception) {
                _uiState.value = ImageGenerationUiState.Error(e.localizedMessage ?: "")
            }
        }
    }
}