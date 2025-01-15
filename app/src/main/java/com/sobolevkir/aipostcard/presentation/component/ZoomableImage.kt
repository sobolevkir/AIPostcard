package com.sobolevkir.aipostcard.presentation.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import coil.compose.AsyncImage

@Composable
fun ZoomableImage(
    imageStringUri: String,
    modifier: Modifier = Modifier
) {
    var scale by remember { mutableFloatStateOf(1f) }

    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }

    var isScaling by remember { mutableStateOf(false) }

    val animatedScale by animateFloatAsState(targetValue = scale)
    val animatedOffsetX by animateFloatAsState(targetValue = offsetX)
    val animatedOffsetY by animateFloatAsState(targetValue = offsetY)

    AsyncImage(
        model = imageStringUri,
        contentDescription = null,
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    isScaling = true
                    scale *= zoom
                    scale = scale.coerceIn(0.5f, 3f)
                    offsetX += pan.x * zoom
                    offsetY += pan.y * zoom
                }
            }
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()
                        if (event.changes.all { it.pressed.not() }) {
                            if (isScaling) {
                                isScaling = false
                                scale = 1f
                                offsetX = 0f
                                offsetY = 0f
                            }
                        }
                    }
                }
            }
            .graphicsLayer(
                scaleX = animatedScale,
                scaleY = animatedScale,
                translationX = animatedOffsetX,
                translationY = animatedOffsetY
            )
    )
}