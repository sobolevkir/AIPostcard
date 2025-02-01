package com.sobolevkir.aipostcard.presentation.screen.about.component

import androidx.annotation.StringRes
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TagBubble(@StringRes text: Int) {
    Box(
        modifier = Modifier
            .height(24.dp)
            .border(
                1.dp,
                color = MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(text),
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            maxLines = 1,
        )
    }
}