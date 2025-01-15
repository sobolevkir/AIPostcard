package com.sobolevkir.aipostcard.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SubmitButton(
    enabled: Boolean = true,
    textResId: Int,
    imageResId: Int? = null,
    onClick: () -> Unit,
    backgroundColor: Color = MaterialTheme.colorScheme.primary
) {
    val contentColor = if (enabled) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.35f)
    }
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .padding(top = 24.dp)
            .fillMaxWidth()
            .height(64.dp),
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            imageResId?.let {
                Image(
                    painter = painterResource(id = imageResId),
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .size(24.dp),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(contentColor)
                )
            }
            Text(
                text = stringResource(textResId),
                fontSize = 17.sp,
                fontWeight = FontWeight.Normal,
                color = contentColor
            )
        }
    }
}