package com.sobolevkir.aipostcard.presentation.screen.album.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sobolevkir.aipostcard.R
import com.sobolevkir.aipostcard.domain.model.AlbumItem
import com.sobolevkir.aipostcard.presentation.component.ConfirmDialog

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
                    imageVector = Icons.Rounded.Delete,
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