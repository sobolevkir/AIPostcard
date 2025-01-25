package com.sobolevkir.aipostcard.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LibraryAdd
import androidx.compose.material.icons.filled.SaveAlt
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.sobolevkir.aipostcard.R

@Composable
fun ImageFullScreenView(
    imageUri: String,
    onClick: () -> Unit,
    onSaveToDeviceGallery: () -> Unit,
    onAddToAlbum: (() -> Unit)? = null,
    onShare: () -> Unit,
    isVisible: Boolean = false,
) {

    if (isVisible) {
        Dialog(
            onDismissRequest = { onClick() },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.8f))
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxSize()
                        .clickable { onClick() },
                ) {
                    ZoomableImage(
                        imageStringUri = imageUri,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f, matchHeightConstraintsFirst = true),
                    )
                    Row(
                        horizontalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    ) {
                        SmallImageButton(
                            iconVector = Icons.Filled.Share,
                            text = R.string.action_share,
                            onClick = onShare
                        )
                        SmallImageButton(
                            iconVector = Icons.Filled.SaveAlt,
                            text = R.string.action_save_to_gallery,
                            onClick = onSaveToDeviceGallery
                        )
                        onAddToAlbum?.let {
                            SmallImageButton(
                                iconVector = Icons.Filled.LibraryAdd,
                                text = R.string.action_add_to_faves,
                                onClick = onAddToAlbum
                            )
                        }
                    }
                }
            }
        }
    }
}