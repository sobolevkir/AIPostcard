package com.sobolevkir.aipostcard.presentation.screen.generate

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Fullscreen
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.sobolevkir.aipostcard.domain.model.GenerationErrorType
import com.sobolevkir.aipostcard.presentation.component.ErrorMessage
import com.sobolevkir.aipostcard.presentation.component.ImageFullScreenView
import com.sobolevkir.aipostcard.presentation.navigation.Routes
import com.sobolevkir.aipostcard.presentation.screen.generate.component.Loader
import com.sobolevkir.aipostcard.presentation.screen.generate.component.QueryTextField
import com.sobolevkir.aipostcard.presentation.screen.generate.component.StylesDropdownMenu
import com.sobolevkir.aipostcard.presentation.screen.generate.component.SubmitButton
import kotlinx.coroutines.flow.collectLatest

@Composable
fun GenerateScreen(onNavigateTo: (Routes) -> Unit = {}) {

    val viewModel: GenerateViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.news.collectLatest { news ->
            when (news) {
                is GenerateNews.ShowMessage -> Toast.makeText(
                    context, news.messageResId, Toast.LENGTH_SHORT
                ).show()

                is GenerateNews.NavigateTo -> onNavigateTo(news.route)
            }
        }
    }

    GenerateView(
        onEvent = viewModel::onEvent,
        state = uiState
    )
}

@Composable
fun GenerateView(
    onEvent: (GenerateUiEvent) -> Unit = {},
    state: GenerateUiState = GenerateUiState()
) {

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val generationResult = state.result

    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .weight(1f)
                .aspectRatio(1f)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Loader(isLoading = state.isGenerating, modifier = Modifier.fillMaxSize())

            generationResult?.let {
                AsyncImage(
                    model = Uri.parse(it.imageStringUri),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable {
                            onEvent(GenerateUiEvent.FullScreenToggle)
                            focusManager.clearFocus()
                            keyboardController?.hide()
                        }
                )
                Icon(
                    imageVector = Icons.Rounded.Fullscreen,
                    modifier = Modifier
                        .padding(8.dp)
                        .size(40.dp)
                        .align(Alignment.BottomEnd)
                        .alpha(0.6f),
                    contentDescription = null,
                    tint = Color.White,
                )
            }

            if (!state.isGenerating && state.result == null && state.error == null) {
                Icon(
                    imageVector = Icons.Rounded.Image,
                    modifier = Modifier.fillMaxSize(),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.surface,
                )
            }

            state.error?.let { error ->
                ErrorMessage(
                    text = when (error) {
                        GenerationErrorType.CONNECTION_PROBLEM -> stringResource(R.string.message_connection_problem)
                        GenerationErrorType.UNKNOWN_ERROR -> stringResource(R.string.message_unknown_error)
                    },
                    onRetryButtonClick = { onEvent(GenerateUiEvent.RetryButtonClick) }
                )
            }
        }

        SubmitButton(
            enabled = state.styles.isNotEmpty() && state.prompt.trim().isNotEmpty(),
            textResId = if (state.isGenerating) R.string.action_stop else R.string.action_go,
            iconVector = if (!state.isGenerating) Icons.Rounded.AutoAwesome else null,
            onClick = { onEvent(GenerateUiEvent.SubmitButtonClick) },
            backgroundColor = if (state.isGenerating) {
                MaterialTheme.colorScheme.tertiary
            } else {
                MaterialTheme.colorScheme.primary
            }
        )

        StylesDropdownMenu(
            styles = state.styles,
            selectedStyle = state.selectedStyle,
            onItemSelected = { newStyle -> onEvent(GenerateUiEvent.StyleSelect(newStyle.name)) },
            enabled = !state.isGenerating,
        )

        QueryTextField(
            value = state.prompt,
            onQueryChange = { onEvent(GenerateUiEvent.PromptChange(it)) },
            enabled = !state.isGenerating,
            labelTextResId = R.string.label_prompt
        )

        QueryTextField(
            value = state.negativePrompt,
            onQueryChange = { onEvent(GenerateUiEvent.NegativePromptChange(it)) },
            enabled = !state.isGenerating,
            labelTextResId = R.string.label_negative_prompt
        )

        Spacer(Modifier.height(4.dp))
    }

    ImageFullScreenView(
        imageUri = generationResult?.imageStringUri,
        onClick = { onEvent(GenerateUiEvent.FullScreenToggle) },
        onSaveToDeviceGallery = { onEvent(GenerateUiEvent.SaveToDeviceGalleryClick) },
        onAddToAlbum = { onEvent(GenerateUiEvent.AddToAlbumClick) },
        onShare = { onEvent(GenerateUiEvent.ShareClick) },
        isVisible = state.isFullScreenOpened,
    )

}