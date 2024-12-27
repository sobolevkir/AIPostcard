package com.sobolevkir.aipostcard.presentation.screen.image_generation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sobolevkir.aipostcard.domain.model.ErrorType
import com.sobolevkir.aipostcard.domain.model.ImageStyle
import com.sobolevkir.aipostcard.domain.usecase.GenerateImageUseCase
import com.sobolevkir.aipostcard.domain.usecase.GetImageStylesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class ImageGenerationViewModel @Inject constructor(
    private val getImageStylesUseCase: GetImageStylesUseCase,
    private val generateImageUseCase: GenerateImageUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ImageGenerationState())
    val uiState: StateFlow<ImageGenerationState> = _uiState
    private var generationJob: Job? = null
    private var getStylesJob: Job? = null

    init {
        loadImageStyles()
    }

    fun onGenerateButtonClick() {
        generationJob?.cancel()
        if (_uiState.value.prompt.isBlank()) {
            _uiState.update { it.copy(isGenerating = false, isPromptError = true) }
            return
        } else {
            _uiState.update {
                it.copy(
                    errorMessage = null,
                    isPromptError = false,
                    isGenerating = true,
                    generatedImage = null
                )
            }
        }

        generationJob = viewModelScope.launch {
            generateImageUseCase(
                prompt = _uiState.value.prompt,
                negativePrompt = _uiState.value.negativePrompt,
                styleName = _uiState.value.selectedStyle?.name ?: ""
            ).collect { (result, error) ->
                Log.d("VIEWMODEL", "generateImage() -> ErrorType: ${error.toString()}")
                when (error) {
                    ErrorType.CONNECTION_PROBLEM -> _uiState.update {
                        it.copy(
                            errorMessage = "Ожидается подключение к интернету",
                            isGenerateButtonEnabled = false
                        )
                    }

                    null -> {
                        _uiState.update {
                            it.copy(
                                isGenerating = false,
                                errorMessage = null,
                                isPromptError = result?.censored ?: false,
                                generatedImage = result?.generatedImagesUri?.first(),
                                isGenerateButtonEnabled = true
                            )
                        }
                        generationJob?.cancel()
                    }

                    else -> _uiState.update {
                        it.copy(
                            errorMessage = "Что-то пошло не так. Попробуйте ещё раз",
                            isGenerating = false,
                            isGenerateButtonEnabled = true
                        )
                    }
                }
            }
        }

    }

    fun onStopButtonClick() {
        generationJob?.cancel()
        generationJob = null
        _uiState.update { it.copy(isGenerating = false, errorMessage = null) }
    }

    fun onStyleSelected(style: ImageStyle) {
        _uiState.update { it.copy(selectedStyle = style) }
    }

    fun onPromptChange(text: String) {
        _uiState.update { it.copy(prompt = text, isPromptError = false) }
    }

    fun onNegativePromptChange(text: String) {
        _uiState.update { it.copy(negativePrompt = text) }
    }

    private fun loadImageStyles() {
        getStylesJob = viewModelScope.launch {
            getImageStylesUseCase().collect { (styles, error) ->
                if (styles?.isNotEmpty() == true) {
                    _uiState.update {
                        it.copy(
                            imageStyles = styles,
                            selectedStyle = styles.first(),
                            errorMessage = null,
                            isGenerateButtonEnabled = true
                        )
                    }
                    getStylesJob?.cancel()
                } else {
                    _uiState.update {
                        it.copy(
                            errorMessage = "Ожидается подключение к интернету",
                            isGenerateButtonEnabled = false
                        )
                    }
                }
            }
        }
    }

}