package com.sobolevkir.aipostcard.presentation.component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.sobolevkir.aipostcard.R

@Composable
fun QueryTextField(
    value: String,
    onQueryChange: (String) -> Unit,
    enabled: Boolean,
    isError: Boolean? = null,
    @StringRes labelTextResId: Int,
) {
    TextField(
        value = value,
        onValueChange = { if (it.length <= REQUEST_MAX_CHAR) onQueryChange(it) },
        enabled = enabled,
        singleLine = true,
        isError = isError ?: false,
        label = {
            Text(
                text = stringResource(labelTextResId),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
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
                text = "${value.length} / $REQUEST_MAX_CHAR",
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.secondary,
                textAlign = TextAlign.End,
            )
        },
        trailingIcon = {
            if (value.isNotEmpty()) {
                IconButton(onClick = { onQueryChange(EMPTY_STRING) }, enabled = enabled) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = stringResource(R.string.action_clear_input)
                    )
                }
            }
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
    )
}

const val REQUEST_MAX_CHAR = 500
private const val EMPTY_STRING = ""