package com.tecsup.avisoscampus.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
private val claro = lightColorScheme(
    primary = Color(0xFF204EB4), onPrimary = Color.White,
    primaryContainer = Color(0xFFDAE4FF), onPrimaryContainer = Color(0xFF0E306E),
    secondaryContainer = Color(0xFFCCEBE7), onSecondaryContainer = Color(0xFF073B38),
    tertiaryContainer = Color(0xFFFFE3AD), onTertiaryContainer = Color(0xFF4F3600),
    surfaceContainer = Color(0xFFE9EEF7), surfaceContainerHigh = Color(0xFFDFE5F0),
    surface = Color(0xFFF4F6FC), surfaceContainerLow = Color.White
)
private val oscuro = darkColorScheme(
    primary = Color(0xFFB1C6FF), onPrimary = Color(0xFF123173),
    primaryContainer = Color(0xFF233F7B), onPrimaryContainer = Color(0xFFDCE5FF),
    secondaryContainer = Color(0xFF224C48), onSecondaryContainer = Color(0xFFBBEBE5),
    tertiaryContainer = Color(0xFF584317), onTertiaryContainer = Color(0xFFFFDEA2),
    surfaceContainer = Color(0xFF252B36), surfaceContainerHigh = Color(0xFF303745),
    surface = Color(0xFF11151D), surfaceContainerLow = Color(0xFF1B202A)
)
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun TemaCampus(content: @Composable () -> Unit) {
    MaterialExpressiveTheme(colorScheme = if (isSystemInDarkTheme()) oscuro else claro, content = content)
}
