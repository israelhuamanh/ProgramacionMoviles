package com.tecsup.avisoscampus.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
private val claro = lightColorScheme(
    primary = Color(0xFF204EB4), onPrimary = Color.White,
    primaryContainer = Color(0xFFDAE4FF), onPrimaryContainer = Color(0xFF0E306E),
    surface = Color(0xFFF4F6FC), surfaceContainerLow = Color.White
)
private val oscuro = darkColorScheme(
    primary = Color(0xFFB1C6FF), onPrimary = Color(0xFF123173),
    primaryContainer = Color(0xFF233F7B), onPrimaryContainer = Color(0xFFDCE5FF),
    surface = Color(0xFF11151D), surfaceContainerLow = Color(0xFF1B202A)
)
@Composable
fun TemaCampus(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = if (isSystemInDarkTheme()) oscuro else claro, content = content)
}
