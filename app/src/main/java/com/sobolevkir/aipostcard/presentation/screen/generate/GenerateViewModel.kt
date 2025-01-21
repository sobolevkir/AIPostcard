package com.sobolevkir.aipostcard.presentation.screen.generate

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sobolevkir.aipostcard.domain.model.GenerationResult
import com.sobolevkir.aipostcard.domain.usecase.GenerateUseCase
import com.sobolevkir.aipostcard.domain.usecase.GetStylesUseCase
import com.sobolevkir.aipostcard.domain.usecase.SaveToGalleryUseCase
import com.sobolevkir.aipostcard.domain.usecase.ShareImageUseCase
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
    private val saveToGalleryUseCase: SaveToGalleryUseCase,
    private val shareImageUseCase: ShareImageUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(GenerateScreenState())
    val uiState: StateFlow<GenerateScreenState> = _uiState

    private var generateJob: Job? = null

    init {
        loadImageStyles()
    }

    fun onEvent(event: GenerateScreenEvent) {
        when (event) {
            is GenerateScreenEvent.PromptChange -> _uiState.update {
                it.copy(
                    prompt = event.prompt,
                    isCensored = false,
                    error = if (it.styles.isNotEmpty()) null else it.error
                )
            }

            is GenerateScreenEvent.NegativePromptChange -> _uiState.update {
                it.copy(negativePrompt = event.negativePrompt)
            }

            is GenerateScreenEvent.StyleSelect -> _uiState.update {
                val selectedStyle = it.styles.firstOrNull { style -> style.name == event.styleName }
                it.copy(selectedStyle = selectedStyle)
            }

            is GenerateScreenEvent.SavedMessageShown -> _uiState.update {
                it.copy(isImageSaved = false)
            }

            is GenerateScreenEvent.FullScreenToggle -> _uiState.update {
                it.copy(isFullScreen = !it.isFullScreen)
            }

            is GenerateScreenEvent.RetryButtonClick -> when {
                _uiState.value.styles.isEmpty() -> loadImageStyles()
                _uiState.value.prompt.isNotEmpty() -> startGeneration()
            }

            is GenerateScreenEvent.SubmitButtonClick -> if (uiState.value.isGenerating) {
                generateJob?.cancel()
                _uiState.update { it.copy(isGenerating = false, error = null) }
            } else {
                startGeneration()
            }

            is GenerateScreenEvent.SaveToGalleryClick -> uiState.value.generatedImage?.let {
                viewModelScope.launch {
                    val isSuccess = saveToGalleryUseCase(it)
                    if (isSuccess) {
                        _uiState.update { it.copy(isImageSaved = true) }
                    } else {
                        _uiState.update { it.copy(isImageSaved = false) }
                    }
                }
            }

            is GenerateScreenEvent.ShareClick -> uiState.value.generatedImage?.let {
                shareImageUseCase(it)
            }
        }
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
                    generatedImage = result.data.imageStringUri,
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

                    is Resource.Loading -> _uiState.update {
                        it.copy(error = null)
                    }
                }
            }
        }
    }

}