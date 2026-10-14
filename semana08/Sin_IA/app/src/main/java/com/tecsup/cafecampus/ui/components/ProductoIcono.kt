package com.tecsup.cafecampus.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable

@Composable
fun ProductoIcono(icono: String) {
    val imageVector = when (icono.lowercase()) {
        "cafe" -> Icons.Default.Coffee
        "plato", "pan", "empanada" -> Icons.Default.Fastfood
        else -> Icons.Default.LocalDrink
    }
    Icon(imageVector = imageVector, contentDescription = null)
}
