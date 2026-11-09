package com.tecsup.publicaciones.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryContainerBlue,
    secondary = SecondaryTeal,
    tertiary = TertiaryAmber
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    primaryContainer = PrimaryContainerBlue,
    secondary = SecondaryTeal,
    tertiary = TertiaryAmber,
    error = ErrorRed
)

@Composable
fun PublicacionesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
