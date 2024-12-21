package com.sobolevkir.aipostcard.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.sobolevkir.aipostcard.presentation.imagegeneration.ImageGenerationViewModel
import com.sobolevkir.aipostcard.ui.component.StylesDropdownMenu

@Composable
fun ImageGenerationScreen(viewModel: ImageGenerationViewModel = hiltViewModel()) {

    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

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
                    Text("Запрос")
                }
            },
            modifier = Modifier.fillMaxWidth(),
            supportingText = {
                Text(
                    text = "${uiState.prompt.length} / $REQUEST_MAX_CHAR",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End,
                )
            }
        )

        TextField(
            value = uiState.negativePrompt,
            onValueChange = { viewModel.onNegativePromptChanged(it) },
            enabled = !uiState.isLoading,
            label = { Text("Негативный промпт") },
            modifier = Modifier.fillMaxWidth()
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
                .fillMaxWidth()
                .height(64.dp)
        ) {
            Text(
                text = "Сгенерировать",
                fontSize = 16.sp
            )
        }

        uiState.errorMessage?.let {
            Text(
                text = it,
                color = Color.Red,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }

        if (uiState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        }

        uiState.generatedImage?.let { stringImageUri ->
            Image(
                painter = rememberAsyncImagePainter(stringImageUri.toUri()),
                contentDescription = "Сгенерированное изображение",
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
            )
        }
    }

}

const val REQUEST_MAX_CHAR = 1000

/*
@Composable
@Preview(showBackground = true)
fun ImageGenerationScreenPreview() {
    ImageGenerationScreen()
}*/
