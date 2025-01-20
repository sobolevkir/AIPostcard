package com.sobolevkir.aipostcard.presentation.screen.generate

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dotlottie.dlplayer.Mode
import com.lottiefiles.dotlottie.core.compose.ui.DotLottieAnimation
import com.lottiefiles.dotlottie.core.util.DotLottieSource
import com.sobolevkir.aipostcard.R

@Composable
fun GeneratingLoader(modifier: Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center
    ) {
        DotLottieAnimation(
            source = DotLottieSource.Asset("animation_loading.lottie"),
            autoplay = true,
            loop = true,
            playMode = Mode.FORWARD,
            modifier = Modifier
                .heightIn(max = 256.dp)
                .fillMaxSize()
        )
        Text(
            text = stringResource(R.string.message_generating),
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
        )
    }

}