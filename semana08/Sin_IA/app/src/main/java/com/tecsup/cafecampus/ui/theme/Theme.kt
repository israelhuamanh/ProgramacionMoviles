package com.tecsup.cafecampus.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.tecsup.cafecampus.model.Rol

private val ClienteLightScheme = lightColorScheme(
    primary = OrangePrimary,
    primaryContainer = OrangeContainer,
    background = OrangeBackground,
    surface = Color.White
)

private val AdminLightScheme = lightColorScheme(
    primary = IndigoPrimary,
    primaryContainer = IndigoContainer,
    background = IndigoBackground,
    surface = Color.White
)

private val DarkScheme = darkColorScheme(
    primary = OrangePrimary,
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E)
)

@Composable
fun CafeTheme(
    rol: Rol = Rol.CLIENTE,
    modoOscuro: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        modoOscuro -> DarkScheme
        rol == Rol.ADMIN -> AdminLightScheme
        else -> ClienteLightScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
