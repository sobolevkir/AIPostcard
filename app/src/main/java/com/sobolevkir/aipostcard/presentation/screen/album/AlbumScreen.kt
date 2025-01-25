package com.sobolevkir.aipostcard.presentation.screen.album

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
        AlbumList(state.items)
    } else ErrorMessage(text = stringResource(R.string.message_empty_album))

}

@Composable
fun AlbumList(albumItems: List<AlbumItem>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(albumItems, key = { it.id }) { albumItem ->
            AlbumItemRow(albumItem = albumItem)
        }
    }
}

@Composable
fun AlbumItemRow(albumItem: AlbumItem) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        AsyncImage(
            model = Uri.parse(albumItem.imageStringUri),
            contentDescription = null,
            modifier = Modifier
                .size(128.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surface)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = albumItem.prompt,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurface
            )

            albumItem.negativePrompt?.let {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = it,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}