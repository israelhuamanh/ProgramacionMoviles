package com.tecsup.murocampus.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val campus = lightColorScheme(
    primary = Color(0xFF313BA0), onPrimary = Color.White,
    primaryContainer = Color(0xFFDEE0FF), onPrimaryContainer = Color(0xFF171C56),
    secondary = Color(0xFF48552C), secondaryContainer = Color(0xFFDBF897),
    onSecondaryContainer = Color(0xFF253308),
    tertiaryContainer = Color(0xFFFFDCCA), onTertiaryContainer = Color(0xFF542814),
    background = Color(0xFFF9F8FF), surface = Color(0xFFF9F8FF),
    surfaceContainer = Color(0xFFEFEEF8), surfaceContainerHigh = Color(0xFFE7E6F2),
    onSurface = Color(0xFF191A2D), onSurfaceVariant = Color(0xFF49495E),
    outline = Color(0xFF747487), outlineVariant = Color(0xFFD0CFDF)
)

private val base = Typography()
private val tipos = base.copy(
    headlineLarge = base.headlineLarge.copy(fontWeight = FontWeight.Black, letterSpacing = (-1.4).sp),
    headlineSmall = base.headlineSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = (-0.7).sp),
    titleLarge = base.titleLarge.copy(fontWeight = FontWeight.Bold, letterSpacing = (-0.5).sp),
    titleMedium = base.titleMedium.copy(fontWeight = FontWeight.Bold)
)

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MuroTheme(content: @Composable () -> Unit) {
    MaterialExpressiveTheme(colorScheme = campus, typography = tipos, content = content)
}
