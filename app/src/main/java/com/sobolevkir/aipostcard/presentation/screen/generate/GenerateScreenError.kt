package com.sobolevkir.aipostcard.presentation.screen.generate

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sobolevkir.aipostcard.R
import com.sobolevkir.aipostcard.presentation.component.SmallTextButton

@Composable
fun GenerateScreenError(
    message: String,
    onRetryButtonClick: (() -> Unit)? = null
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.tertiary,
            textAlign = TextAlign.Center,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        onRetryButtonClick?.let {
            SmallTextButton(
                textResId = R.string.action_retry,
                onClick = it
            )
        }
    }
}