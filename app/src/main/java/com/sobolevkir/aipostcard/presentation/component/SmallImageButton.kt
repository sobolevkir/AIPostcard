package com.sobolevkir.aipostcard.presentation.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sobolevkir.aipostcard.R

@Composable
fun SmallImageButton(
    @DrawableRes imageResId: Int,
    text: Int,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .widthIn(max = 128.dp)
            .clickable { onClick.invoke() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            painter = painterResource(id = imageResId),
            modifier = Modifier.size(56.dp),
            contentDescription = null,
            tint = Color.White
        )
        Text(
            text = stringResource(text),
            fontSize = 13.sp,
            lineHeight = 14.sp,
            softWrap = true,
            textAlign = TextAlign.Center,
            color = Color.White,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Preview
@Composable
private fun SmallImageButtonPreview() {
    SmallImageButton(
        imageResId = R.drawable.ic_image_placeholder,
        text = R.string.action_save_to_gallery,
        onClick = {})
}