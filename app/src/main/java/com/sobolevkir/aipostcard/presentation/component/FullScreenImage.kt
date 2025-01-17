package com.sobolevkir.aipostcard.presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sobolevkir.aipostcard.R

@Composable
fun ImageFullScreenView(
    imageUri: String,
    onShare: () -> Unit,
    onSaveToGallery: () -> Unit,
    onAddToFaves: (() -> Unit)? = null,
    isVisible: Boolean = false,
    onFullScreenToggle: () -> Unit,
) {

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(durationMillis = 300)) + scaleIn(
            initialScale = 0.8f,
            animationSpec = tween(durationMillis = 200)
        ),
        exit = fadeOut(animationSpec = tween(durationMillis = 300)) + scaleOut(
            targetScale = 0.8f,
            animationSpec = tween(durationMillis = 200)
        ),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.8f))
                .clickable { onFullScreenToggle() },
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
                    imageResId = R.drawable.ic_share,
                    text = R.string.action_share,
                    onClick = onShare
                )
                SmallImageButton(
                    imageResId = R.drawable.ic_save_to_gallery,
                    text = R.string.action_save_to_gallery,
                    onClick = onSaveToGallery
                )
                onAddToFaves?.let {
                    SmallImageButton(
                        imageResId = R.drawable.ic_add_to_faves,
                        text = R.string.action_add_to_faves,
                        onClick = onAddToFaves
                    )
                }
            }
        }
    }

}