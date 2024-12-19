package com.sobolevkir.aipostcard.ui.screen.imagegeneration

import android.graphics.BitmapFactory
import android.util.Base64
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

    private val _uiState = MutableStateFlow(ImageGenerationUiState())
    val uiState: StateFlow<ImageGenerationUiState> = _uiState

    init {
        loadImageStyles()
    }

    private fun loadImageStyles() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            getImageStylesUseCase.invoke()
                .collect { resource ->
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

    fun onStyleSelected(style: ImageStyle) {
        _uiState.update { it.copy(selectedStyle = style) }
    }

    fun onPromptChanged(text: String) {
        _uiState.update { it.copy(prompt = text) }
    }

    fun onNegativePromptChanged(text: String) {
        _uiState.update { it.copy(negativePrompt = text) }
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
        repeat(7) {
            if (isCompleted) return
            delay(7_000)
            getStatusOrImageUseCase.invoke(uuid).collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        val result = resource.data
                        Log.d("MY_VM", result?.generatedImages.toString())
                        when (result?.status) {
                            ImageGenerationStatus.DONE -> {
                                _uiState.update { it.copy(isLoading = false) }
                                decodeImage(result.generatedImages.firstOrNull())
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
                    errorMessage = "Превышено количество попыток!",
                    isLoading = false
                )
            }
        }
    }

    private fun decodeImage(base64String: String?) {
        if (base64String.isNullOrEmpty()) {
            _uiState.update { it.copy(errorMessage = "Пустое изображение", isLoading = false) }
            return
        }

        try {
            val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
            val image = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
            _uiState.update {
                it.copy(
                    errorMessage = null,
                    isLoading = false,
                    generatedImage = image
                )
            }
        } catch (e: Exception) {
            _uiState.update {
                it.copy(
                    errorMessage = "Ошибка декодирования изображения",
                    isLoading = false
                )
            }
        }
    }

}