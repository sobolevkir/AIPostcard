package com.sobolevkir.aipostcard.presentation.screen.generation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.lottiefiles.dotlottie.core.compose.ui.DotLottieAnimation
import com.lottiefiles.dotlottie.core.util.DotLottieSource
import com.sobolevkir.aipostcard.R
import com.sobolevkir.aipostcard.presentation.component.QueryTextField
import com.sobolevkir.aipostcard.presentation.component.StylesDropdownMenu
import com.sobolevkir.aipostcard.presentation.component.SubmitButton

@Composable
fun GenerationScreen(viewModel: GenerationViewModel = hiltViewModel()) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // TODO: вынести код в отдельные компоненты

    // TODO: вынести строки в ресурсы

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = rememberAsyncImagePainter(uiState.generatedImage?.toUri()),
                contentDescription = stringResource(R.string.generated_image),
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                    .fillMaxWidth()
                    .aspectRatio(1f)
            )

            uiState.errorMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            if (uiState.isGenerating) {
                DotLottieAnimation(
                    source = DotLottieSource.Asset("animation_loading.lottie"),
                    autoplay = true,
                    loop = true,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }

        val focusManager = LocalFocusManager.current
        QueryTextField(
            value = uiState.prompt,
            maxChar = REQUEST_MAX_CHAR,
            onQueryChange = viewModel::onPromptChange,
            enabled = !uiState.isGenerating,
            maxLines = 2,
            isError = uiState.isCensored,
            labelText = stringResource(R.string.label_prompt),
            focusManager = focusManager
        )

        QueryTextField(
            value = uiState.negativePrompt,
            maxChar = REQUEST_MAX_CHAR,
            onQueryChange = viewModel::onNegativePromptChange,
            enabled = !uiState.isGenerating,
            maxLines = 1,
            isError = uiState.isCensored,
            labelText = stringResource(R.string.label_negative_prompt),
            focusManager = focusManager
        )

        StylesDropdownMenu(
            styles = uiState.styles,
            selectedStyle = uiState.selectedStyle,
            onItemSelected = { newStyle ->
                viewModel.onStyleSelected(newStyle)
            },
            enabled = !uiState.isGenerating,
        )

        SubmitButton(
            enabled = uiState.isGenerateButtonEnabled,
            text = stringResource(if (uiState.isGenerating) R.string.action_stop else R.string.action_go),
            onClick = viewModel::onGenerateButtonClick,
            backgroundColor = if (uiState.isGenerating) {
                MaterialTheme.colorScheme.tertiary
            } else {
                MaterialTheme.colorScheme.primary
            }
        )
    }

}

const val REQUEST_MAX_CHAR = 500

/*
@Composable
@Preview(showBackground = true)
fun ImageGenerationScreenPreview() {
    ImageGenerationScreen()
}*/
