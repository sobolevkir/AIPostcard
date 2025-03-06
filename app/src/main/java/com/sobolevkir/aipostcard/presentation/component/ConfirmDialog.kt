package com.sobolevkir.aipostcard.presentation.component

import androidx.annotation.StringRes
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.sobolevkir.aipostcard.R

@Composable
fun ConfirmDialog(
    @StringRes messageResId: Int,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onCancel,
        properties = DialogProperties(usePlatformDefaultWidth = false),
        text = {
            Text(text = stringResource(messageResId))
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm
            ) {
                Text(text = stringResource(R.string.action_yes))
            }
        },
        dismissButton = {
            TextButton(
                onClick = onCancel
            ) {
                Text(text = stringResource(R.string.action_cancel))
            }
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        tonalElevation = 16.dp,
        shape = MaterialTheme.shapes.medium
    )
}