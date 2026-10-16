package com.tecsup.cafecampus.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun AdminBottomBar(
    rutaActual: String,
    onNavigate: (String) -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            selected = rutaActual == "panel_admin",
            onClick = { onNavigate("panel_admin") },
            icon = { Icon(Icons.Default.Dashboard, contentDescription = "Panel") },
            label = { Text("Panel") }
        )
        NavigationBarItem(
            selected = rutaActual == "pedidos_admin",
            onClick = { onNavigate("pedidos_admin") },
            icon = { Icon(Icons.AutoMirrored.Filled.ListAlt, contentDescription = "Pedidos") },
            label = { Text("Pedidos") }
        )
        NavigationBarItem(
            selected = rutaActual == "menu_admin",
            onClick = { onNavigate("menu_admin") },
            icon = { Icon(Icons.Default.Restaurant, contentDescription = "Menú") },
            label = { Text("Menú") }
        )
        NavigationBarItem(
            selected = rutaActual == "ajustes",
            onClick = { onNavigate("ajustes") },
            icon = { Icon(Icons.Default.Settings, contentDescription = "Ajustes") },
            label = { Text("Ajustes") }
        )
    }
}
