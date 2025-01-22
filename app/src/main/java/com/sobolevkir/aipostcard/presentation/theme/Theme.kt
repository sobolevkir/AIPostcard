package com.sobolevkir.aipostcard.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = Mint,
    secondary = Grey,
    onPrimary = SoftBlack,
    tertiary = Red,
    onPrimaryContainer = AbsoluteWhite,
)

private val LightColorScheme = lightColorScheme(
    primary = LightMint,
    secondary = LightGrey,
    onPrimary = AbsoluteWhite,
    tertiary = LightRed,
    onPrimaryContainer = SoftBlack
)

@Composable
fun AIPostcardTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}