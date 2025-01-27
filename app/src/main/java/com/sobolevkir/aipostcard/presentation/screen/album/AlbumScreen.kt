package com.sobolevkir.aipostcard.presentation.screen.album

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.sobolevkir.aipostcard.R
import com.sobolevkir.aipostcard.domain.model.AlbumItem
import com.sobolevkir.aipostcard.presentation.component.ErrorMessage
import com.sobolevkir.aipostcard.presentation.component.ImageFullScreenView
import com.sobolevkir.aipostcard.presentation.navigation.Routes
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AlbumScreen(onNavigateTo: (Routes) -> Unit = {}) {
    val viewModel: AlbumViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val messageMap = mapOf(
        AlbumMessage.ImageSavedToGallery to stringResource(R.string.message_saved_to_gallery),
        AlbumMessage.ImageSavingError to stringResource(R.string.message_saving_error),
        AlbumMessage.ImageRemovedFromAlbum to stringResource(R.string.message_removed_from_album),
        AlbumMessage.ImageRemovingError to stringResource(R.string.message_removing_error),

        )

    // TODO: Использовать уменьшенные preview изображения!

    LaunchedEffect(Unit) {
        viewModel.news.collectLatest { news ->
            when (news) {
                is AlbumNews.ShowMessage -> {
                    val messageText = messageMap[news.message]
                    Toast.makeText(context, messageText, Toast.LENGTH_SHORT).show()
                }

                is AlbumNews.NavigateTo -> onNavigateTo(news.route)
            }
        }
    }

    AlbumView(
        onEvent = viewModel::onEvent,
        state = uiState
    )
}

@Composable
fun AlbumView(
    onEvent: (AlbumUiEvent) -> Unit = {},
    state: AlbumUiState = AlbumUiState()
) {

    if (state.items.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(state.items, key = { it.id }) { albumItem ->
                SwipeToDismissItem(
                    item = albumItem,
                    onItemClick = { onEvent(AlbumUiEvent.OpenItem(albumItem)) },
                    onDismiss = { onEvent(AlbumUiEvent.RemoveItem(albumItem.id)) }
                )
            }
        }
    } else ErrorMessage(text = stringResource(R.string.message_empty_album))

    ImageFullScreenView(
        imageUri = state.selectedItem?.imageStringUri,
        onClick = { onEvent(AlbumUiEvent.CloseItem) },
        onSaveToDeviceGallery = { onEvent(AlbumUiEvent.SaveToDeviceGalleryClick) },
        onShare = { onEvent(AlbumUiEvent.ShareClick) },
        isVisible = state.selectedItem != null,
    )
}

@Composable
fun SwipeToDismissItem(
    item: AlbumItem,
    onItemClick: () -> Unit,
    onDismiss: () -> Unit
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            if (it == SwipeToDismissBoxValue.EndToStart) {
                onDismiss()
                true
            } else false
        }
    )

    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = false,
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(8.dp)),
        backgroundContent = {
            val color = when (dismissState.dismissDirection) {
                SwipeToDismissBoxValue.EndToStart -> MaterialTheme.colorScheme.error
                else -> Color.Transparent
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color)
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = stringResource(R.string.action_delete),
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .size(32.dp),
                    tint = MaterialTheme.colorScheme.onError
                )
            }
        },
        content = {
            AlbumItemRow(item = item, onItemClick = onItemClick)
        }
    )
}

@Composable
fun AlbumItemRow(
    item: AlbumItem,
    onItemClick: () -> Unit,
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable { onItemClick() },
        verticalAlignment = Alignment.Top
    ) {
        AsyncImage(
            model = Uri.parse(item.imageStringUri),
            contentDescription = null,
            modifier = Modifier
                .size(120.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surface)
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp)
        ) {
            Text(
                text = item.prompt,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurface
            )

            item.negativePrompt?.let {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = it,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}