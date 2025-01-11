package com.sobolevkir.aipostcard.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.sobolevkir.aipostcard.R
import com.sobolevkir.aipostcard.domain.model.Style

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StylesDropdownMenu(
    styles: List<Style>,
    selectedStyle: Style? = null,
    onItemSelected: (Style) -> Unit,
    enabled: Boolean
) {
    var expanded by remember { mutableStateOf(false) }

    val imageModifier = Modifier
        .padding(start = 16.dp, end = 16.dp)
        .size(80.dp, 40.dp)
        .clip(RoundedCornerShape(8.dp))
        .background(MaterialTheme.colorScheme.surfaceVariant)
        .fillMaxSize()

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
    ) {
        TextField(
            value = selectedStyle?.title ?: stringResource(R.string.loading),
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(type = MenuAnchorType.PrimaryNotEditable, true),
            readOnly = true,
            shape = RoundedCornerShape(16.dp),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            leadingIcon = {
                AsyncImage(
                    model = selectedStyle?.styleImageUrl,
                    contentDescription = null,
                    modifier = imageModifier,
                    contentScale = ContentScale.Crop
                )
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            enabled = styles.isNotEmpty() && enabled
        )

        ExposedDropdownMenu(
            expanded = expanded && enabled && styles.isNotEmpty(),
            onDismissRequest = { expanded = false },
        ) {
            styles.forEach { item ->
                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            AsyncImage(
                                model = item.styleImageUrl,
                                contentDescription = null,
                                modifier = imageModifier,
                                contentScale = ContentScale.Crop
                            )
                            Text(text = item.title)
                        }
                    },
                    onClick = {
                        onItemSelected(item)
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SimpleDropdownMenuPreview() {
    var selectedItem by remember {
        mutableStateOf(
            Style(
                "https://via.placeholder.com/150",
                "Style 1",
                "Реализм"
            )
        )
    }

    val items = listOf(
        Style("https://via.placeholder.com/150", "Style 1", "Реализм"),
        Style("https://via.placeholder.com/150", "Style 2", "Импрессионизм"),
        Style("https://via.placeholder.com/150", "Style 3", "Аниме")
    )

    MaterialTheme {
        StylesDropdownMenu(
            styles = items,
            selectedStyle = items.first(),
            onItemSelected = { selectedItem = it },
            enabled = true
        )
    }
}