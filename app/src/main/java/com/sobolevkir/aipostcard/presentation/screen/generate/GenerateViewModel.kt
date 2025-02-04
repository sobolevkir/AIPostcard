package com.sobolevkir.aipostcard.presentation.screen.generate

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sobolevkir.aipostcard.R
import com.sobolevkir.aipostcard.domain.model.GenerationResult
import com.sobolevkir.aipostcard.domain.usecase.AddToAlbumUseCase
import com.sobolevkir.aipostcard.domain.usecase.GenerateUseCase
import com.sobolevkir.aipostcard.domain.usecase.GetStylesUseCase
import com.sobolevkir.aipostcard.domain.usecase.SaveToDeviceGalleryUseCase
import com.sobolevkir.aipostcard.domain.usecase.ShareImageUseCase
import com.sobolevkir.aipostcard.presentation.navigation.Routes
import com.sobolevkir.aipostcard.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GenerateViewModel @Inject constructor(
    private val getStylesUseCase: GetStylesUseCase,
    private val generateUseCase: GenerateUseCase,
    private val saveToDeviceGalleryUseCase: SaveToDeviceGalleryUseCase,
    private val shareImageUseCase: ShareImageUseCase,
    private val addToAlbumUseCase: AddToAlbumUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(GenerateUiState())
    val uiState: StateFlow<GenerateUiState> = _uiState
    private val _news = MutableSharedFlow<GenerateNews>()
    val news = _news.asSharedFlow()
    private var generateJob: Job? = null

    init {
        loadImageStyles()
    }

    fun onEvent(event: GenerateUiEvent) {
        when (event) {
            is GenerateUiEvent.PromptChange -> _uiState.update {
                it.copy(
                    prompt = event.prompt,
                    error = if (it.styles.isNotEmpty()) null else it.error
                )
            }

            is GenerateUiEvent.NegativePromptChange -> _uiState.update {
                it.copy(negativePrompt = event.negativePrompt)
            }

            is GenerateUiEvent.StyleSelect -> _uiState.update {
                val selectedStyle = it.styles.firstOrNull { style -> style.name == event.styleName }
                it.copy(selectedStyle = selectedStyle)
            }

            is GenerateUiEvent.FullScreenToggle -> _uiState.update {
                it.copy(isFullScreenOpened = !it.isFullScreenOpened)
            }

            is GenerateUiEvent.RetryButtonClick -> when {
                _uiState.value.styles.isEmpty() -> loadImageStyles()
                _uiState.value.prompt.isNotEmpty() -> startGeneration()
            }

            is GenerateUiEvent.SubmitButtonClick -> if (uiState.value.isGenerating) {
                generateJob?.cancel()
                _uiState.update { it.copy(isGenerating = false, error = null) }
            } else startGeneration()

            is GenerateUiEvent.SaveToDeviceGalleryClick -> viewModelScope.launch {
                val isSuccess = saveToDeviceGalleryUseCase(
                    uiState.value.result?.imageStringUri ?: ""
                )
                showMessage(
                    if (isSuccess) R.string.message_saved_to_gallery else R.string.message_saving_error
                )
            }

            is GenerateUiEvent.AddToAlbumClick -> uiState.value.result?.let {
                viewModelScope.launch {
                    val isSuccess = addToAlbumUseCase(
                        uuid = it.uuid,
                        cachedImageStringUri = it.imageStringUri,
                        prompt = uiState.value.prompt,
                        negativePrompt = uiState.value.negativePrompt,
                        styleTitle = uiState.value.selectedStyle?.title ?: ""
                    )
                    if (isSuccess) {
                        _news.emit(GenerateNews.NavigateTo(Routes.Album))
                        showMessage(R.string.message_added_to_album)
                        _uiState.update { it.copy(isFullScreenOpened = false) }
                    } else showMessage(R.string.message_exists_in_album)
                }
            }

            is GenerateUiEvent.ShareClick -> uiState.value.result?.let {
                shareImageUseCase(it.imageStringUri)
            }
        }
    }

    private fun startGeneration() {
        generateJob = viewModelScope.launch {
            generateUseCase(
                prompt = _uiState.value.prompt,
                negativePrompt = _uiState.value.negativePrompt,
                styleName = _uiState.value.selectedStyle?.name
            ).collect { result -> processResult(result) }
        }
    }

    private fun processResult(result: Resource<GenerationResult>) {
        _uiState.update {
            when (result) {
                is Resource.Error -> it.copy(isGenerating = false, error = result.error)
                is Resource.Loading -> it.copy(isGenerating = true, result = null, error = null)
                is Resource.Success -> it.copy(
                    isGenerating = false,
                    error = null,
                    result = result.data
                )
            }
        }
        if (result is Resource.Success && result.data.censored) showMessage(R.string.message_censored)
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

                    is Resource.Error -> _uiState.update { it.copy(error = result.error) }
                    is Resource.Loading -> _uiState.update { it.copy(error = null) }
                }
            }
        }
    }

    private fun showMessage(@StringRes messageRedId: Int) {
        viewModelScope.launch {
            _news.emit(GenerateNews.ShowMessage(messageRedId))
        }
    }

}