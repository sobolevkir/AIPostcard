package com.sobolevkir.aipostcard.presentation.imagegeneration

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sobolevkir.aipostcard.domain.model.ImageGenerationStatus
import com.sobolevkir.aipostcard.domain.model.ImageStyle
import com.sobolevkir.aipostcard.domain.usecase.GetImageStylesUseCase
import com.sobolevkir.aipostcard.domain.usecase.GetStatusOrImageUseCase
import com.sobolevkir.aipostcard.domain.usecase.RequestImageGenerationUseCase
import com.sobolevkir.aipostcard.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class ImageGenerationViewModel @Inject constructor(
    private val getImageStylesUseCase: GetImageStylesUseCase,
    private val requestImageGenerationUseCase: RequestImageGenerationUseCase,
    private val getStatusOrImageUseCase: GetStatusOrImageUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ImageGenerationState())
    val uiState: StateFlow<ImageGenerationState> = _uiState

    init {
        loadImageStyles()
    }

    fun generateImage() {
        if (_uiState.value.prompt.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Введите текст запроса!", isLoading = false) }
            return
        }
        _uiState.update { it.copy(errorMessage = null, isLoading = true, generatedImage = null) }

        viewModelScope.launch {
            requestImageGenerationUseCase.invoke(
                prompt = _uiState.value.prompt,
                negativePrompt = _uiState.value.negativePrompt,
                styleName = _uiState.value.selectedStyle?.name ?: ""
            ).collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        val uuid = resource.data?.uuid.orEmpty()
                        getStatusOrImage(uuid)
                    }

                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                errorMessage = resource.errorType.toString(),
                                isLoading = false,
                            )
                        }
                    }

                }
            }
        }
    }

    private suspend fun getStatusOrImage(uuid: String) {
        var isCompleted = false
        repeat(10) {
            if (isCompleted) return
            delay(8_000)
            getStatusOrImageUseCase.invoke(uuid).collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        val result = resource.data
                        Log.d("MY_VM", result?.generatedImagesStringUri.toString())
                        when (result?.status) {
                            ImageGenerationStatus.DONE -> {
                                _uiState.update {
                                    it.copy(
                                        isLoading = false,
                                        isCensored = result.censored,
                                        generatedImage = result.generatedImagesStringUri.firstOrNull()
                                    )
                                }
                                isCompleted = true
                                return@collect
                            }

                            ImageGenerationStatus.FAIL -> {
                                _uiState.update {
                                    it.copy(
                                        isLoading = false,
                                        errorMessage = "Генерация не удалась: ${result.status}"
                                    )
                                }
                                isCompleted = true
                                return@collect
                            }

                            else -> {}
                        }
                    }

                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = "Генерация не удалась: ${resource.errorType.toString()}"
                            )
                        }
                        return@collect
                    }
                }
            }
        }
        if (!isCompleted) {
            _uiState.update {
                it.copy(
                    errorMessage = "Превышено количество попыток!", isLoading = false
                )
            }
        }

    }

    fun onStyleSelected(style: ImageStyle) {
        _uiState.update { it.copy(selectedStyle = style) }
    }

    fun onPromptChanged(text: String) {
        _uiState.update { it.copy(prompt = text) }
    }

    fun onNegativePromptChanged(text: String) {
        _uiState.update { it.copy(negativePrompt = text) }
    }

    private fun loadImageStyles() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            getImageStylesUseCase.invoke().collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        val stylesList = resource.data.orEmpty()
                        _uiState.update {
                            it.copy(
                                imageStyles = stylesList,
                                selectedStyle = stylesList.first(),
                                errorMessage = null,
                                isLoading = false
                            )
                        }
                    }

                    is Resource.Error -> {
                        val errorType = resource.errorType.toString()
                        _uiState.update {
                            it.copy(errorMessage = errorType, isLoading = false)
                        }
                    }
                }
            }
        }
    }

}