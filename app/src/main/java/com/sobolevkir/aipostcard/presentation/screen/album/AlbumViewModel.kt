package com.sobolevkir.aipostcard.presentation.screen.album

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sobolevkir.aipostcard.R
import com.sobolevkir.aipostcard.domain.usecase.GetAlbumItemsUseCase
import com.sobolevkir.aipostcard.domain.usecase.RemoveFromAlbumUseCase
import com.sobolevkir.aipostcard.domain.usecase.SaveToDeviceGalleryUseCase
import com.sobolevkir.aipostcard.domain.usecase.ShareImageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    private val getAlbumItemsUseCase: GetAlbumItemsUseCase,
    private val saveToDeviceGalleryUseCase: SaveToDeviceGalleryUseCase,
    private val shareImageUseCase: ShareImageUseCase,
    private val removeFromAlbumUseCase: RemoveFromAlbumUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AlbumUiState())
    val uiState: StateFlow<AlbumUiState> = _uiState
    private val _news = MutableSharedFlow<AlbumNews>()
    val news = _news.asSharedFlow()

    init {
        loadAlbumItems()
    }

    fun onEvent(event: AlbumUiEvent) {
        when (event) {
            is AlbumUiEvent.OpenItem -> _uiState.update { it.copy(selectedItem = event.item) }
            is AlbumUiEvent.RemoveItem -> {
                viewModelScope.launch {
                    val isSuccess = removeFromAlbumUseCase(event.itemId)
                    showMessage(
                        if (isSuccess) R.string.message_removed_from_album else R.string.message_removing_error
                    )
                }
            }

            is AlbumUiEvent.CloseItem -> _uiState.update { it.copy(selectedItem = null) }
            is AlbumUiEvent.SaveToDeviceGalleryClick -> uiState.value.selectedItem?.let { item ->
                viewModelScope.launch {
                    val isSuccess = saveToDeviceGalleryUseCase(item.imageStringUri)
                    showMessage(
                        if (isSuccess) R.string.message_saved_to_gallery else R.string.message_saving_error
                    )
                }
            }

            is AlbumUiEvent.ShareClick -> uiState.value.selectedItem?.let { item ->
                shareImageUseCase(item.imageStringUri)
            }
        }
    }

    private fun loadAlbumItems() {
        getAlbumItemsUseCase().onEach { items ->
            if (items.isEmpty()) {
                _uiState.update { it.copy(items = items, selectedItem = null) }
            } else {
                _uiState.update { it.copy(items = items) }
            }
        }.launchIn(viewModelScope)
    }


    private fun showMessage(@StringRes messageResId: Int) {
        viewModelScope.launch {
            _news.emit(AlbumNews.ShowMessage(messageResId))
        }
    }

}