package com.sobolevkir.aipostcard.presentation.screen.album

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sobolevkir.aipostcard.R
import com.sobolevkir.aipostcard.presentation.component.ConfirmDialog
import com.sobolevkir.aipostcard.presentation.component.ErrorMessage
import com.sobolevkir.aipostcard.presentation.component.ImageFullScreenView
import com.sobolevkir.aipostcard.presentation.navigation.Routes
import com.sobolevkir.aipostcard.presentation.screen.album.component.SwipeableAlbumItem
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AlbumScreen(onNavigateTo: (Routes) -> Unit = {}) {

    val viewModel: AlbumViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.news.collectLatest { news ->
            when (news) {
                is AlbumNews.ShowMessage -> Toast.makeText(
                    context, news.messageResId, Toast.LENGTH_SHORT
                ).show()

                is AlbumNews.NavigateTo -> onNavigateTo(news.route)
            }
        }
    }

    AlbumView(onEvent = viewModel::onEvent, state = uiState)
}

@Composable
fun AlbumView(
    onEvent: (AlbumUiEvent) -> Unit = {},
    state: AlbumUiState = AlbumUiState()
) {

    if (state.isEmpty) {
        ErrorMessage(text = stringResource(R.string.message_empty_album))
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(
                items = state.items,
                key = { it.id }
            ) { albumItem ->
                val dismissState = rememberSwipeToDismissBoxState()
                LaunchedEffect(dismissState.targetValue) {
                    if (dismissState.targetValue == SwipeToDismissBoxValue.EndToStart) {
                        onEvent(AlbumUiEvent.SwipeItemToRemove(albumItem))
                    }
                }
                LaunchedEffect(state.itemToRemove) {
                    if (state.itemToRemove == null) {
                        dismissState.snapTo(SwipeToDismissBoxValue.Settled)
                    }
                }
                SwipeableAlbumItem(
                    dismissState = dismissState,
                    item = albumItem,
                    onClick = { onEvent(AlbumUiEvent.OpenItem(albumItem)) },
                    modifier = Modifier.animateItem()
                )
            }
        }

        if (state.itemToRemove != null) {
            ConfirmDialog(
                messageResId = R.string.message_removing_confirm,
                onConfirm = {
                    onEvent(AlbumUiEvent.ConfirmRemoving)
                },
                onCancel = { onEvent(AlbumUiEvent.CancelRemoving) }
            )
        }

        ImageFullScreenView(
            imageUri = state.selectedItem?.imageStringUri,
            onClick = { onEvent(AlbumUiEvent.CloseItem) },
            onSaveToDeviceGallery = { onEvent(AlbumUiEvent.SaveToDeviceGalleryClick) },
            onShare = { onEvent(AlbumUiEvent.ShareClick) },
            isVisible = state.selectedItem != null,
        )
    }

}

