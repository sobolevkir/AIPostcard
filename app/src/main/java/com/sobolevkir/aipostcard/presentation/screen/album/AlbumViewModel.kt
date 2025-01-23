package com.sobolevkir.aipostcard.presentation.screen.album

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sobolevkir.aipostcard.domain.usecase.GetAlbumItemsUseCase
import com.sobolevkir.aipostcard.domain.usecase.SaveToDeviceGalleryUseCase
import com.sobolevkir.aipostcard.domain.usecase.ShareImageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    private val getAlbumItemsUseCase: GetAlbumItemsUseCase,
    private val saveToDeviceGalleryUseCase: SaveToDeviceGalleryUseCase,
    private val shareImageUseCase: ShareImageUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AlbumUiState())
    val uiState: StateFlow<AlbumUiState> = _uiState

    init {
        loadAlbumItems()
    }

    fun onEvent(event: AlbumUiEvent) {
        when (event) {
            is AlbumUiEvent.OpenItem -> _uiState.update { it.copy(selectedItemId = event.itemId) }
            AlbumUiEvent.CloseItem -> _uiState.update { it.copy(selectedItemId = null) }
            AlbumUiEvent.SaveToDeviceGalleryClick -> uiState.value.selectedItemId?.let { itemId ->
                val image = uiState.value.items.first { it.id == itemId }.imageStringUri
                viewModelScope.launch {
                    val isSuccess = saveToDeviceGalleryUseCase(image)
                    if (isSuccess) {
                        _uiState.update { it.copy(showMessage = true) }
                    } else {
                        _uiState.update { it.copy(showMessage = false) }
                    }
                }
            }

            AlbumUiEvent.ShareClick -> uiState.value.selectedItemId?.let { itemId ->
                val image = uiState.value.items.first { it.id == itemId }.imageStringUri
                shareImageUseCase(image)
            }
        }
    }

    private fun loadAlbumItems() {
        viewModelScope.launch {
            getAlbumItemsUseCase().onEach { items ->
                if (items.isEmpty()) {
                    _uiState.update {
                        it.copy(
                            isAlbumEmpty = true,
                            items = items,
                            selectedItemId = null
                        )
                    }
                } else {
                    _uiState.update { it.copy(isAlbumEmpty = false, items = items) }
                }
            }
        }
    }

}