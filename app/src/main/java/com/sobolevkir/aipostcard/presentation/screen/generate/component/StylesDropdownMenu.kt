package com.sobolevkir.aipostcard.presentation.screen.generate.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.ArrowDropUp
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
        .alpha(if (enabled) 1f else 0.35f)
    val language = LocalContext.current.resources.configuration.locales[0].language

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = Modifier.padding(bottom = 16.dp)
    ) {
        TextField(
            value = (if (language == "ru") selectedStyle?.titleRu else selectedStyle?.titleEn)
                ?: stringResource(R.string.loading),
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(type = MenuAnchorType.PrimaryNotEditable, true),
            readOnly = true,
            shape = RoundedCornerShape(16.dp),
            trailingIcon = {
                AnimatedContent(
                    targetState = expanded,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = ""
                ) { targetExpanded ->
                    Icon(
                        imageVector = if (targetExpanded) Icons.Rounded.ArrowDropUp else Icons.Rounded.ArrowDropDown,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp),
                    )
                }
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
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surfaceContainerHigh),
            shape = RoundedCornerShape(16.dp),
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
                            Text(text = if (language == "ru") item.titleRu else item.titleEn)
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
private fun SimpleDropdownMenuPreview() {
    var selectedItem by remember {
        mutableStateOf(
            Style(
                "https://via.placeholder.com/150",
                "REALISM",
                "Реализм",
                titleEn = "Realism"
            )
        )
    }

    val style = Style(
        styleImageUrl = "",
        name = "123",
        titleRu = "123",
        titleEn = "123"
    )

    val styles = MutableList(5) { style }

    MaterialTheme {
        Scaffold { paddings ->
            Box(modifier = Modifier.padding(paddings)) {
                StylesDropdownMenu(
                    styles = styles,
                    selectedStyle = null,
                    onItemSelected = { selectedItem = it },
                    enabled = true
                )
            }
        }
    }
}