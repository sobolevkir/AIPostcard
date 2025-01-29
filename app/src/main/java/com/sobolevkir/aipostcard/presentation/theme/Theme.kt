package com.sobolevkir.aipostcard.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    onBackground = AbsoluteWhite,
    primary = Mint,
    secondary = Grey,
    onPrimary = SoftBlack,
    tertiary = Red,
    onPrimaryContainer = AbsoluteWhite,
    onSurface = AbsoluteWhite,
    error = Red,
    onError = AbsoluteWhite
)

private val LightColorScheme = lightColorScheme(
    onBackground = SoftBlack,
    primary = LightMint,
    secondary = LightGrey,
    onPrimary = AbsoluteWhite,
    tertiary = LightRed,
    onPrimaryContainer = SoftBlack,
    onSurface = SoftBlack,
    error = LightRed,
    onError = AbsoluteWhite
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