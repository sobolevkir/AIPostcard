package com.sobolevkir.aipostcard.presentation.screen.generation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sobolevkir.aipostcard.domain.model.ErrorType
import com.sobolevkir.aipostcard.domain.model.GenerationResult
import com.sobolevkir.aipostcard.domain.model.Style
import com.sobolevkir.aipostcard.domain.usecase.GenerateUseCase
import com.sobolevkir.aipostcard.domain.usecase.GetStylesUseCase
import com.sobolevkir.aipostcard.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class GenerationViewModel @Inject constructor(
    private val getStylesUseCase: GetStylesUseCase,
    private val generateUseCase: GenerateUseCase
) : ViewModel() {

    private var generationJob: Job? = null
    private val _uiState = MutableStateFlow(GenerationUiState())
    val uiState: StateFlow<GenerationUiState> = _uiState

    init {
        loadImageStyles()
    }

    fun onGenerateButtonClick() {
        generationJob?.cancel()
        if (_uiState.value.prompt.isBlank()) {
            _uiState.update { it.copy(isGenerating = false, isPromptError = true) }
            return
        }
        generationJob = viewModelScope.launch {
            generateUseCase(
                prompt = _uiState.value.prompt,
                negativePrompt = _uiState.value.negativePrompt,
                styleName = _uiState.value.selectedStyle?.name ?: ""
            ).collect { resource ->
                Log.d("VIEWMODEL", resource.toString())
                processResult(resource)
            }
        }

    }

    private fun processResult(result: Resource<GenerationResult?>) {
        when (result) {
            is Resource.Success -> {
                _uiState.update {
                    it.copy(
                        generatedImage = result.data?.generatedImagesUri?.first(),
                        isGenerating = false,
                        isPromptError = result.data?.censored ?: false,
                        errorMessage = null
                    )
                }
                generationJob?.cancel()
            }

            is Resource.Error -> {
                if (result.error != ErrorType.CONNECTION_PROBLEM) generationJob?.cancel()
                _uiState.update {
                    it.copy(isGenerating = false, errorMessage = result.error.toString())
                }
            }

            is Resource.Loading -> _uiState.update {
                it.copy(
                    isGenerating = true,
                    generatedImage = null,
                    errorMessage = null,
                    isPromptError = false,
                )
            }

        }
    }

    fun onStopButtonClick() {
        generationJob?.cancel()
        generationJob = null
        _uiState.update { it.copy(isGenerating = false, errorMessage = null) }
    }

    fun onStyleSelected(style: Style) {
        _uiState.update { it.copy(selectedStyle = style) }
    }

    fun onPromptChange(text: String) {
        _uiState.update { it.copy(prompt = text, isPromptError = false) }
    }

    fun onNegativePromptChange(text: String) {
        _uiState.update { it.copy(negativePrompt = text) }
    }

    private fun loadImageStyles() {
        viewModelScope.launch {
            getStylesUseCase().collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                styles = result.data,
                                selectedStyle = result.data.first(),
                                errorMessage = null,
                                isGenerateButtonEnabled = true
                            )
                        }
                        this.cancel()
                    }

                    is Resource.Error -> _uiState.update {
                        it.copy(
                            errorMessage = result.error.toString(),
                            isGenerateButtonEnabled = false
                        )
                    }

                    is Resource.Loading -> {}
                }
            }
        }
    }

}