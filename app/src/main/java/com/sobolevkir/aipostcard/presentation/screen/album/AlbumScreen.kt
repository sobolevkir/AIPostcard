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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.sobolevkir.aipostcard.R
import com.sobolevkir.aipostcard.domain.model.AlbumItem
import com.sobolevkir.aipostcard.presentation.component.ConfirmDialog
import com.sobolevkir.aipostcard.presentation.component.ErrorMessage
import com.sobolevkir.aipostcard.presentation.component.ImageFullScreenView
import com.sobolevkir.aipostcard.presentation.navigation.Routes
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

@Composable
fun SwipeableAlbumItem(
    item: AlbumItem,
    onClick: () -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showRemoveDialog by rememberSaveable { mutableStateOf(false) }
    if (showRemoveDialog) {
        ConfirmDialog(
            messageResId = R.string.message_removing_confirm,
            onConfirm = {
                onRemove()
                showRemoveDialog = false
            },
            onCancel = { showRemoveDialog = false }
        )
    }
    val dismissState = rememberSwipeToDismissBoxState()
    LaunchedEffect(showRemoveDialog) {
        if (!showRemoveDialog) dismissState.reset()
    }
    LaunchedEffect(dismissState.currentValue) {
        if (dismissState.currentValue != SwipeToDismissBoxValue.Settled) {
            showRemoveDialog = true
        }
    }

    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = false,
        modifier = modifier
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
            AlbumItemRow(
                item = item,
                modifier = Modifier.clickable { onClick() },
            )
        }
    )
}

@Composable
fun AlbumItemRow(
    item: AlbumItem,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(128.dp)
            .background(MaterialTheme.colorScheme.surfaceContainerHigh),
        verticalAlignment = Alignment.Top
    ) {
        AsyncImage(
            model = Uri.parse(item.thumbStringUri),
            contentDescription = null,
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surface)
        )

        Column(
            modifier = Modifier
                .padding(16.dp)
                .weight(1f)
        ) {
            Text(
                text = item.prompt,
                maxLines = 3,
                fontSize = 16.sp,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurface
            )

            item.negativePrompt?.let {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = it,
                    maxLines = 2,
                    lineHeight = 13.sp,
                    fontSize = 12.sp,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        Column(
            modifier = Modifier
                .wrapContentWidth()
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.primary)
                .padding(horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = item.styleTitle,
                maxLines = 2,
                lineHeight = 12.sp,
                fontSize = 12.sp,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .rotateLayout(Rotation.ROT_270)
                    .fillMaxWidth()
            )
        }

    }
}

