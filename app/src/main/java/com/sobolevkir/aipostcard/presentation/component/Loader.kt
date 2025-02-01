package com.sobolevkir.aipostcard.presentation.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dotlottie.dlplayer.Mode
import com.lottiefiles.dotlottie.core.compose.runtime.DotLottieController
import com.lottiefiles.dotlottie.core.compose.ui.DotLottieAnimation
import com.lottiefiles.dotlottie.core.util.DotLottieSource
import com.sobolevkir.aipostcard.R
import kotlinx.coroutines.isActive

@Composable
fun Loader(isLoading: Boolean, modifier: Modifier) {

    val animationController = remember { DotLottieController() }
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(isLoading) {
        if (isLoading) {
            animationController.play()
            while (isActive) {
                alpha.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis = 500, easing = LinearEasing)
                )
                alpha.animateTo(
                    targetValue = 0.4f,
                    animationSpec = tween(durationMillis = 500, easing = LinearEasing)
                )
            }
        } else {
            animationController.pause()
            alpha.snapTo(0f)
        }
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DotLottieAnimation(
            source = DotLottieSource.Asset("animation_loading.lottie"),
            autoplay = isLoading,
            loop = true,
            playMode = Mode.FORWARD,
            modifier = Modifier
                .alpha(if (isLoading) 1f else 0f)
                .fillMaxWidth()
                .wrapContentHeight(Alignment.CenterVertically)
                .heightIn(max = 400.dp)
                .weight(1f),
            controller = animationController
        )

        Text(
            text = stringResource(R.string.message_generating),
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = alpha.value),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            softWrap = true
        )
    }

}