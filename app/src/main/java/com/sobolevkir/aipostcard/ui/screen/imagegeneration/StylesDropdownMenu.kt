package com.sobolevkir.aipostcard.ui.screen.imagegeneration

import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.sobolevkir.aipostcard.domain.model.ImageStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StylesDropdownMenu(
    styles: List<ImageStyle>,
    selectedStyle: ImageStyle? = null,
    onItemSelected: (ImageStyle) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
    ) {
        TextField(
            value = selectedStyle?.title ?: "Loading...",
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(type = MenuAnchorType.PrimaryNotEditable, true),
            readOnly = true,
            onValueChange = {},
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            enabled = styles.isNotEmpty()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            if (styles.isNotEmpty()) {
                styles.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(item.title) },
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
}

@Preview(showBackground = true)
@Composable
fun SimpleDropdownMenuPreview() {
    var selectedItem by remember {
        mutableStateOf(
            ImageStyle(
                "https://via.placeholder.com/150",
                "Style 1",
                "Реализм"
            )
        )
    }

    val items = listOf(
        ImageStyle("https://via.placeholder.com/150", "Style 1", "Реализм"),
        ImageStyle("https://via.placeholder.com/150", "Style 2", "Импрессионизм"),
        ImageStyle("https://via.placeholder.com/150", "Style 3", "Аниме")
    )

    MaterialTheme {
        StylesDropdownMenu(
            styles = items,
            selectedStyle = items.first(),
            onItemSelected = { selectedItem = it }
        )
    }
}