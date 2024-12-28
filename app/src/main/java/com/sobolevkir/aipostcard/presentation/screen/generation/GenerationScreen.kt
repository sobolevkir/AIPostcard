package com.sobolevkir.aipostcard.presentation.screen.generation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.sobolevkir.aipostcard.presentation.component.StylesDropdownMenu

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
                contentDescription = "Сгенерированное изображение",
                modifier = Modifier
                    .then(
                        if (uiState.generatedImage.isNullOrEmpty()) {
                            Modifier.border(
                                width = 8.dp,
                                brush = SolidColor(MaterialTheme.colorScheme.surfaceContainerLow),
                                shape = RoundedCornerShape(16.dp)
                            )
                        } else {
                            Modifier
                        }
                    )
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                    .fillMaxWidth()
                    .aspectRatio(1f)
            )

            if (uiState.isGenerating) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(64.dp)
                )
            }
        }

        uiState.errorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }

        val focusRequester = remember { FocusRequester() }
        val focusManager = LocalFocusManager.current
        TextField(
            value = uiState.prompt,
            onValueChange = { if (it.length <= REQUEST_MAX_CHAR) viewModel.onPromptChange(it) },
            enabled = !uiState.isGenerating,
            maxLines = 2,
            isError = uiState.isPromptError,
            label = { Text(text = "Опишите ваш запрос") },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(16.dp),
            supportingText = {
                Text(
                    text = "${uiState.prompt.length} / $REQUEST_MAX_CHAR",
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.secondary,
                    textAlign = TextAlign.End,
                )
            },
            trailingIcon = {
                if (uiState.prompt.isNotEmpty()) {
                    IconButton(onClick = { if (!uiState.isGenerating) viewModel.onPromptChange("") }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Очистить ввод"
                        )
                    }
                }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) })
        )

        TextField(
            value = uiState.negativePrompt,
            onValueChange = {
                if (it.length <= REQUEST_MAX_CHAR) viewModel.onNegativePromptChange(it)
            },
            enabled = !uiState.isGenerating,
            maxLines = 1,
            label = { Text("Что хотели бы исключить?") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(16.dp),
            supportingText = {
                Text(
                    text = "${uiState.negativePrompt.length} / $REQUEST_MAX_CHAR",
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.secondary,
                    textAlign = TextAlign.End,
                )
            },
            trailingIcon = {
                if (uiState.negativePrompt.isNotEmpty()) {
                    IconButton(onClick = {
                        if (!uiState.isGenerating) viewModel.onNegativePromptChange("")
                    }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Очистить ввод"
                        )
                    }
                }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) })
        )

        StylesDropdownMenu(
            styles = uiState.styles,
            selectedStyle = uiState.selectedStyle,
            onItemSelected = { newStyle ->
                viewModel.onStyleSelected(newStyle)
            },
            enabled = !uiState.isGenerating,
        )

        Button(
            onClick = {
                if (uiState.isGenerating) {
                    viewModel.onStopButtonClick()
                } else {
                    viewModel.onGenerateButtonClick()
                    if (uiState.isPromptError) focusRequester.requestFocus()
                }
            },
            enabled = uiState.isGenerateButtonEnabled,
            modifier = Modifier
                .padding(top = 24.dp)
                .fillMaxWidth()
                .height(64.dp),
            colors = if (uiState.isGenerating) {
                ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
            } else {
                ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            }

        ) {
            Text(
                text = if (uiState.isGenerating) "Остановить" else "Сгенерировать",
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal
            )
        }
    }

}

const val REQUEST_MAX_CHAR = 500

/*
@Composable
@Preview(showBackground = true)
fun ImageGenerationScreenPreview() {
    ImageGenerationScreen()
}*/
