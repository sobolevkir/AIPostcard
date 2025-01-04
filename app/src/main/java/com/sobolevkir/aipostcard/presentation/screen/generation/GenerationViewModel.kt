package com.sobolevkir.aipostcard.presentation.screen.generation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sobolevkir.aipostcard.domain.model.GenerationResult
import com.sobolevkir.aipostcard.domain.model.Style
import com.sobolevkir.aipostcard.domain.usecase.GenerateUseCase
import com.sobolevkir.aipostcard.domain.usecase.GetStylesUseCase
import com.sobolevkir.aipostcard.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GenerationViewModel @Inject constructor(
    private val getStylesUseCase: GetStylesUseCase,
    private val generateUseCase: GenerateUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(GenerationUiState())
    val uiState: StateFlow<GenerationUiState> = _uiState

    private var generateJob: Job? = null

    init {
        loadImageStyles()
    }

    fun onGenerateButtonClick() {
        generateJob = viewModelScope.launch {
            generateUseCase(
                prompt = _uiState.value.prompt,
                negativePrompt = _uiState.value.negativePrompt,
                styleName = _uiState.value.selectedStyle?.name
            ).collect { result ->
                Log.d("VIEWMODEL", result.toString())
                processResult(result)
            }
        }
    }

    fun onStopButtonClick() {
        generateJob?.cancel()
        _uiState.update { it.copy(isGenerating = false, errorMessage = null) }
    }

    fun onStyleSelected(style: Style) {
        _uiState.update { it.copy(selectedStyle = style) }
    }

    fun onPromptChange(text: String) {
        _uiState.update {
            it.copy(
                prompt = text,
                isCensored = false,
                isGenerateButtonEnabled = text.isNotEmpty()
            )
        }
    }

    fun onNegativePromptChange(text: String) {
        _uiState.update { it.copy(negativePrompt = text) }
    }

    private fun processResult(result: Resource<GenerationResult?>) {
        when (result) {
            is Resource.Success -> _uiState.update {
                it.copy(
                    generatedImage = result.data?.generatedImageUri,
                    isGenerating = false,
                    isCensored = result.data?.censored ?: false,
                    errorMessage = null
                )
            }

            is Resource.Error -> _uiState.update {
                it.copy(isGenerating = false, errorMessage = result.error.toString())
            }

            is Resource.Loading -> _uiState.update {
                it.copy(
                    isGenerating = true,
                    generatedImage = null,
                    errorMessage = null,
                    isCensored = false,
                )
            }

        }
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
                            )
                        }
                    }

                    is Resource.Error -> _uiState.update {
                        it.copy(errorMessage = result.error.toString())
                    }

                    is Resource.Loading -> {}
                }
            }
        }
    }

}