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
import com.sobolevkir.aipostcard.presentation.component.ImageFullScreenView
import com.sobolevkir.aipostcard.presentation.component.QueryTextField
import com.sobolevkir.aipostcard.presentation.component.StylesDropdownMenu
import com.sobolevkir.aipostcard.presentation.component.SubmitButton

@Composable
fun GenerationScreen(viewModel: GenerateViewModel = hiltViewModel()) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    val generatedImage = uiState.generatedImage

    if (uiState.isImageSaved) {
        Toast.makeText(context, R.string.message_saved_to_gallery, Toast.LENGTH_SHORT).show()
        viewModel.onSavedMessageShown()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .aspectRatio(1f)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {

            GeneratingLoader(
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(if (uiState.isGenerating) 1f else 0f)
            )

            generatedImage?.let {
                AsyncImage(
                    model = it,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable {
                            viewModel.onFullScreenToggle()
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

            if (!uiState.isGenerating && uiState.generatedImage.isNullOrEmpty() && uiState.error == null) {
                Icon(
                    imageVector = Icons.Filled.Image,
                    modifier = Modifier.fillMaxSize(),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.surface,
                )
            }

            uiState.error?.let {
                GenerateScreenError(
                    message = when (it) {
                        ErrorType.CONNECTION_PROBLEM -> stringResource(R.string.message_connection_problem)
                        ErrorType.UNKNOWN_ERROR -> stringResource(R.string.message_unknown_error)
                    },
                    onRetryButtonClick = viewModel::onRetryButtonClick
                )
            }
        }

        QueryTextField(
            value = uiState.prompt,
            onQueryChange = viewModel::onPromptChange,
            enabled = !uiState.isGenerating,
            linesNumber = 2,
            isError = uiState.isCensored,
            labelTextResId = R.string.label_prompt
        )

        QueryTextField(
            value = uiState.negativePrompt,
            onQueryChange = viewModel::onNegativePromptChange,
            enabled = !uiState.isGenerating,
            linesNumber = 1,
            isError = uiState.isCensored,
            labelTextResId = R.string.label_negative_prompt
        )

        StylesDropdownMenu(
            styles = uiState.styles,
            selectedStyle = uiState.selectedStyle,
            onItemSelected = { newStyle -> viewModel.onStyleSelect(newStyle) },
            enabled = !uiState.isGenerating,
        )

        SubmitButton(
            enabled = uiState.styles.isNotEmpty() && uiState.prompt.isNotEmpty(),
            textResId = if (uiState.isGenerating) R.string.action_stop else R.string.action_go,
            iconVector = if (!uiState.isGenerating) Icons.Default.AutoAwesome else null,
            onClick = viewModel::onSubmitButtonClick,
            backgroundColor = if (uiState.isGenerating) {
                MaterialTheme.colorScheme.tertiary
            } else {
                MaterialTheme.colorScheme.primary
            }
        )
    }
    generatedImage?.let {
        ImageFullScreenView(
            isVisible = uiState.isFullScreen,
            imageUri = generatedImage,
            onShare = viewModel::onShareClick,
            onSaveToGallery = viewModel::onSaveToGalleryClick,
            onAddToFaves = {},
            onFullScreenToggle = viewModel::onFullScreenToggle,
        )
    }
}