package com.sobolevkir.aipostcard.presentation.screen.about

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sobolevkir.aipostcard.R
import com.sobolevkir.aipostcard.presentation.screen.about.component.TagBubble

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AboutScreen() {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row {
            Text(stringResource(R.string.text_stack), modifier= Modifier.padding(end = 16.dp))
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TagBubble(R.string.text_mvi)
                TagBubble(R.string.text_compose)
                TagBubble(R.string.text_kotlin_coroutines)
                TagBubble(R.string.text_retrofit)
                TagBubble(R.string.text_room)
                TagBubble(R.string.text_hilt)
            }
        }
        Row {
            Text(stringResource(R.string.text_app_version))
        }
        Row {
            Text(stringResource(R.string.text_developer))
        }
    }

}