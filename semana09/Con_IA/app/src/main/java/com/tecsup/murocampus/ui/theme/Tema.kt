package com.tecsup.murocampus.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val colores = lightColorScheme(
    primary = Color(0xFF174EA6), onPrimary = Color.White,
    primaryContainer = Color(0xFFDBE8FF), onPrimaryContainer = Color(0xFF123466),
    background = Color(0xFFF5F7FB), surface = Color.White,
    onSurface = Color(0xFF17253C), onSurfaceVariant = Color(0xFF475569)
)

@Composable
fun MuroTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = colores, content = content)
}
