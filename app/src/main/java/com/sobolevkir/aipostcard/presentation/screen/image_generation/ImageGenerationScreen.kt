package com.sobolevkir.aipostcard.presentation.screen.image_generation

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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.sobolevkir.aipostcard.presentation.component.StylesDropdownMenu

@Composable
fun ImageGenerationScreen(viewModel: ImageGenerationViewModel = hiltViewModel()) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // TODO: вынести код в отдельные компоненты

    // TODO: вынести строки в ресурсы

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
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
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                    .fillMaxWidth()
                    .aspectRatio(1f)
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
            )

            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            uiState.errorMessage?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }

        TextField(
            value = uiState.prompt,
            onValueChange = { if (it.length <= REQUEST_MAX_CHAR) viewModel.onPromptChanged(it) },
            enabled = !uiState.isLoading,
            maxLines = 3,
            label = {
                if (uiState.isCensored) {
                    Text(
                        text = "Запрос не соответствует политике в отношении контента!",
                        color = Color.Red
                    )
                } else {
                    Text("Опишите ваш запрос")
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(16.dp),
            supportingText = {
                Text(
                    text = "${uiState.prompt.length} / $REQUEST_MAX_CHAR",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End,
                )
            },
            trailingIcon = {
                if (uiState.prompt.isNotEmpty()) {
                    IconButton(onClick = { viewModel.onPromptChanged("") }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Очистить ввод"
                        )
                    }
                }
            }
        )

        TextField(
            value = uiState.negativePrompt,
            onValueChange = {
                if (it.length <= REQUEST_MAX_CHAR) viewModel.onNegativePromptChanged(
                    it
                )
            },
            enabled = !uiState.isLoading,
            label = { Text("Что хотели бы исключить?") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(16.dp),
            supportingText = {
                Text(
                    text = "${uiState.negativePrompt.length} / $REQUEST_MAX_CHAR",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End,
                )
            },
            trailingIcon = {
                if (uiState.negativePrompt.isNotEmpty()) {
                    IconButton(onClick = { viewModel.onNegativePromptChanged("") }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Очистить ввод"
                        )
                    }
                }
            }
        )

        StylesDropdownMenu(
            styles = uiState.imageStyles,
            selectedStyle = uiState.selectedStyle,
            onItemSelected = { newStyle ->
                viewModel.onStyleSelected(newStyle)
            },
            enabled = !uiState.isLoading,
        )

        Button(
            onClick = { viewModel.generateImage() },
            enabled = !uiState.isLoading,
            modifier = Modifier
                .padding(top = 16.dp, bottom = 16.dp)
                .fillMaxWidth()
                .height(64.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text(
                text = "Сгенерировать",
                fontSize = 18.sp
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
