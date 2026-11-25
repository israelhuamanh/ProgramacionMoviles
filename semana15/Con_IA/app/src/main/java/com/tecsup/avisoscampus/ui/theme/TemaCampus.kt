package com.tecsup.avisoscampus.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val claro = lightColorScheme(
    primary = Color(0xFF204EB4),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFDAE4FF),
    onPrimaryContainer = Color(0xFF0E306E),
    secondary = Color(0xFF1F6A64),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFCCEBE7),
    onSecondaryContainer = Color(0xFF073B38),
    tertiary = Color(0xFF7B5700),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFFFE3AD),
    onTertiaryContainer = Color(0xFF4F3600),
    error = Color(0xFFBA1A1A),
    onError = Color.White,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF93000A),
    surface = Color(0xFFF7F9FF),
    onSurface = Color(0xFF181C22),
    onSurfaceVariant = Color(0xFF434750),
    surfaceContainerLowest = Color.White,
    surfaceContainerLow = Color(0xFFF1F4FB),
    surfaceContainer = Color(0xFFEBEEF6),
    surfaceContainerHigh = Color(0xFFE5E8F0),
    surfaceContainerHighest = Color(0xFFDFE2EA),
    outline = Color(0xFF737781),
    outlineVariant = Color(0xFFC3C6D2)
)

private val oscuro = darkColorScheme(
    primary = Color(0xFFB1C6FF),
    onPrimary = Color(0xFF123173),
    primaryContainer = Color(0xFF233F7B),
    onPrimaryContainer = Color(0xFFDCE5FF),
    secondary = Color(0xFF8CD5CD),
    onSecondary = Color(0xFF003734),
    secondaryContainer = Color(0xFF224C48),
    onSecondaryContainer = Color(0xFFBBEBE5),
    tertiary = Color(0xFFF3BF48),
    onTertiary = Color(0xFF412D00),
    tertiaryContainer = Color(0xFF584317),
    onTertiaryContainer = Color(0xFFFFDEA2),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    surface = Color(0xFF11141A),
    onSurface = Color(0xFFE1E2EA),
    onSurfaceVariant = Color(0xFFC3C6D2),
    surfaceContainerLowest = Color(0xFF0C0E14),
    surfaceContainerLow = Color(0xFF191C22),
    surfaceContainer = Color(0xFF1D2026),
    surfaceContainerHigh = Color(0xFF272A31),
    surfaceContainerHighest = Color(0xFF32353C),
    outline = Color(0xFF8D909B),
    outlineVariant = Color(0xFF434750)
)

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun TemaCampus(
    temaOscuro: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialExpressiveTheme(
        colorScheme = if (temaOscuro) oscuro else claro,
        motionScheme = MotionScheme.expressive(),
        content = content
    )
}
