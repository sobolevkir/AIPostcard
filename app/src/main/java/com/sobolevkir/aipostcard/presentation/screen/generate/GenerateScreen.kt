package com.sobolevkir.aipostcard.presentation.screen.generate

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.sobolevkir.aipostcard.R
import com.sobolevkir.aipostcard.domain.model.ErrorType
import com.sobolevkir.aipostcard.presentation.component.ErrorMessage
import com.sobolevkir.aipostcard.presentation.component.ImageFullScreenView
import com.sobolevkir.aipostcard.presentation.component.Loader
import com.sobolevkir.aipostcard.presentation.component.QueryTextField
import com.sobolevkir.aipostcard.presentation.component.StylesDropdownMenu
import com.sobolevkir.aipostcard.presentation.component.SubmitButton
import com.sobolevkir.aipostcard.presentation.navigation.NavGraph

@Composable
fun GenerateScreen(onNavigateTo: (NavGraph) -> Unit = {}) {

    val viewModel: GenerateViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    GenerateView(
        onNavigateTo = onNavigateTo,
        onEvent = viewModel::onEvent,
        state = uiState
    )
}

@Composable
fun GenerateView(
    onNavigateTo: (NavGraph) -> Unit = {},
    onEvent: (GenerateScreenEvent) -> Unit = {},
    state: GenerateScreenState = GenerateScreenState()
) {

    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val generatedImage = state.generatedImage

    if (state.isImageSaved) {
        Toast.makeText(context, R.string.message_saved_to_gallery, Toast.LENGTH_SHORT).show()
        onEvent(GenerateScreenEvent.SavedMessageShown)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Box(
            modifier = Modifier
                .padding(bottom = 16.dp)
                .align(Alignment.CenterHorizontally)
                .weight(1f)
                .aspectRatio(1f)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {

            Loader(
                isLoading = state.isGenerating,
                modifier = Modifier.fillMaxSize()
            )

            generatedImage?.let {
                AsyncImage(
                    model = it,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable {
                            onEvent(GenerateScreenEvent.FullScreenToggle)
                            focusManager.clearFocus()
                            keyboardController?.hide()
                        }
                )
                Icon(
                    imageVector = Icons.Filled.Fullscreen,
                    modifier = Modifier
                        .padding(8.dp)
                        .size(40.dp)
                        .align(Alignment.BottomEnd)
                        .alpha(0.6f),
                    contentDescription = null,
                    tint = Color.White,
                )
            }

            if (!state.isGenerating && state.generatedImage.isNullOrEmpty() && state.error == null) {
                Icon(
                    imageVector = Icons.Filled.Image,
                    modifier = Modifier.fillMaxSize(),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.surface,
                )
            }

            state.error?.let {
                ErrorMessage(
                    text = when (it) {
                        ErrorType.CONNECTION_PROBLEM -> stringResource(R.string.message_connection_problem)
                        ErrorType.UNKNOWN_ERROR -> stringResource(R.string.message_unknown_error)
                    },
                    onRetryButtonClick = { onEvent(GenerateScreenEvent.RetryButtonClick) }
                )
            }
        }

        QueryTextField(
            value = state.prompt,
            onQueryChange = { onEvent(GenerateScreenEvent.PromptChange(it)) },
            enabled = !state.isGenerating,
            isError = state.isCensored,
            labelTextResId = if (state.isCensored) R.string.message_censored else R.string.label_prompt
        )

        QueryTextField(
            value = state.negativePrompt,
            onQueryChange = { onEvent(GenerateScreenEvent.NegativePromptChange(it)) },
            enabled = !state.isGenerating,
            labelTextResId = R.string.label_negative_prompt
        )

        StylesDropdownMenu(
            styles = state.styles,
            selectedStyle = state.selectedStyle,
            onItemSelected = { newStyle -> onEvent(GenerateScreenEvent.StyleSelect(newStyle.name)) },
            enabled = !state.isGenerating,
        )

        SubmitButton(
            enabled = state.styles.isNotEmpty() && state.prompt.isNotEmpty(),
            textResId = if (state.isGenerating) R.string.action_stop else R.string.action_go,
            iconVector = if (!state.isGenerating) Icons.Default.AutoAwesome else null,
            onClick = { onEvent(GenerateScreenEvent.SubmitButtonClick) },
            backgroundColor = if (state.isGenerating) {
                MaterialTheme.colorScheme.tertiary
            } else {
                MaterialTheme.colorScheme.primary
            }
        )
    }

    generatedImage?.let {
        ImageFullScreenView(
            isVisible = state.isFullScreen,
            imageUri = generatedImage,
            onShare = { onEvent(GenerateScreenEvent.ShareClick) },
            onSaveToGallery = { onEvent(GenerateScreenEvent.SaveToGalleryClick) },
            onAddToAlbum = { onNavigateTo(NavGraph.Album) },
            onFullScreenToggle = { onEvent(GenerateScreenEvent.FullScreenToggle) },
        )
    }

}