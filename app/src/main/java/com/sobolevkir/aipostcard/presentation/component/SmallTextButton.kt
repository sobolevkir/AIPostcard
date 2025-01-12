package com.sobolevkir.aipostcard.presentation.component

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SmallTextButton(
    textResId: Int,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(top = 24.dp)
            .height(40.dp),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onBackground),
    ) {
        Text(
            text = stringResource(textResId),
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.background,
            fontWeight = FontWeight.Normal
        )
    }
}