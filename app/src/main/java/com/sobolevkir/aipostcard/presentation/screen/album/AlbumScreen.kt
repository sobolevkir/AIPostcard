package com.sobolevkir.aipostcard.presentation.screen.album

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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

    if (state.items.isEmpty()) {
        ErrorMessage(text = stringResource(R.string.message_empty_album))
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(
            items = state.items,
            key = { it.id }
        ) { albumItem ->
            SwipeableAlbumItem(
                item = albumItem,
                onClick = { onEvent(AlbumUiEvent.OpenItem(albumItem)) },
                onRemove = { onEvent(AlbumUiEvent.RemoveItem(albumItem.id)) },
                modifier = Modifier.animateItem()
            )
        }
    }

    ImageFullScreenView(
        imageUri = state.selectedItem?.imageStringUri,
        onClick = { onEvent(AlbumUiEvent.CloseItem) },
        onSaveToDeviceGallery = { onEvent(AlbumUiEvent.SaveToDeviceGalleryClick) },
        onShare = { onEvent(AlbumUiEvent.ShareClick) },
        isVisible = state.selectedItem != null,
    )

}

