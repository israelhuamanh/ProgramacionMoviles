package com.tecsup.cafecampus.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun ClienteBottomBar(
    rutaActual: String,
    onNavigate: (String) -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            selected = rutaActual == "menu",
            onClick = { onNavigate("menu") },
            icon = { Icon(Icons.Default.Coffee, contentDescription = "Menú") },
            label = { Text("Menú") }
        )
        NavigationBarItem(
            selected = rutaActual == "mis_pedidos",
            onClick = { onNavigate("mis_pedidos") },
            icon = { Icon(Icons.AutoMirrored.Filled.ReceiptLong, contentDescription = "Mis Pedidos") },
            label = { Text("Mis Pedidos") }
        )
        NavigationBarItem(
            selected = rutaActual == "ajustes",
            onClick = { onNavigate("ajustes") },
            icon = { Icon(Icons.Default.Settings, contentDescription = "Ajustes") },
            label = { Text("Ajustes") }
        )
    }
}
