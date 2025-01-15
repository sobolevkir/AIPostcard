package com.sobolevkir.aipostcard.presentation.screen.generate

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sobolevkir.aipostcard.domain.model.GenerationResult
import com.sobolevkir.aipostcard.domain.model.Style
import com.sobolevkir.aipostcard.domain.usecase.GenerateUseCase
import com.sobolevkir.aipostcard.domain.usecase.GetStylesUseCase
import com.sobolevkir.aipostcard.domain.usecase.SaveImageToGalleryUseCase
import com.sobolevkir.aipostcard.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GenerateViewModel @Inject constructor(
    private val getStylesUseCase: GetStylesUseCase,
    private val generateUseCase: GenerateUseCase,
    private val saveImageToGalleryUseCase: SaveImageToGalleryUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(GenerateUiState())
    val uiState: StateFlow<GenerateUiState> = _uiState

    private var generateJob: Job? = null

    init {
        loadImageStyles()
    }

    fun onSubmitButtonClick() {
        if (uiState.value.isGenerating) {
            generateJob?.cancel()
            _uiState.update { it.copy(isGenerating = false, error = null) }
        } else {
            startGeneration()
        }
    }

    fun onRetryButtonClick() {
        when {
            _uiState.value.styles.isEmpty() -> loadImageStyles()
            _uiState.value.prompt.isNotEmpty() -> startGeneration()
        }
    }

    fun onStyleSelect(style: Style) {
        _uiState.update { it.copy(selectedStyle = style) }
    }

    fun onPromptChange(text: String) {
        _uiState.update {
            it.copy(
                prompt = text,
                isCensored = false,
                error = if (it.styles.isNotEmpty()) null else it.error
            )
        }
    }

    fun onNegativePromptChange(text: String) {
        _uiState.update { it.copy(negativePrompt = text) }
    }

    fun onSaveToGalleryClick() {
        uiState.value.generatedImage?.let {
            viewModelScope.launch {
                val isSuccess = saveImageToGalleryUseCase(it)
                if (isSuccess) {
                    _uiState.update { it.copy(isImageSaved = true) }
                } else {
                    _uiState.update { it.copy(isImageSaved = false) }
                }
            }
        }
    }

    fun onSavedMessageShown() {
        _uiState.value = _uiState.value.copy(isImageSaved = false)
    }

    private fun startGeneration() {
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

    private fun processResult(result: Resource<GenerationResult>) {
        when (result) {
            is Resource.Success -> _uiState.update {
                it.copy(
                    generatedImage = result.data.generatedImageUri,
                    isGenerating = false,
                    isCensored = result.data.censored,
                    error = null
                )
            }

            is Resource.Error -> _uiState.update {
                it.copy(isGenerating = false, error = result.error)
            }

            is Resource.Loading -> _uiState.update {
                it.copy(
                    isGenerating = true,
                    generatedImage = null,
                    error = null,
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
                                error = null
                            )
                        }
                    }

                    is Resource.Error -> _uiState.update {
                        it.copy(error = result.error)
                    }

                    is Resource.Loading -> {}
                }
            }
        }
    }

}