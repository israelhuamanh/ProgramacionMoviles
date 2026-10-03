package com.tecsup.store.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AppDrawer(
    itemSeleccionado: String,
    onDestinoClick: (String) -> Unit
) {
    ModalDrawerSheet {
        // Encabezado con datos del usuario e iniciales MR
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFEDE7F6))
                .padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF673AB7)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "MR",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = "Maria Rojas",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xFF212121)
                    )
                    Text(
                        text = "maria@tecsup.edu.pe",
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        NavigationDrawerItem(
            label = { Text("Inicio") },
            icon = { Icon(Icons.Default.Home, contentDescription = null) },
            selected = itemSeleccionado == "Inicio",
            colors = NavigationDrawerItemDefaults.colors(
                selectedContainerColor = Color(0xFFE8DEF8),
                selectedIconColor = Color(0xFF673AB7),
                selectedTextColor = Color(0xFF673AB7)
            ),
            onClick = { onDestinoClick("Inicio") },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
        NavigationDrawerItem(
            label = { Text("Mis pedidos") },
            icon = { Icon(Icons.Default.ShoppingCart, contentDescription = null) },
            selected = itemSeleccionado == "Mis pedidos",
            colors = NavigationDrawerItemDefaults.colors(
                selectedContainerColor = Color(0xFFE8DEF8),
                selectedIconColor = Color(0xFF673AB7),
                selectedTextColor = Color(0xFF673AB7)
            ),
            onClick = { onDestinoClick("Mis pedidos") },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
        NavigationDrawerItem(
            label = { Text("Favoritos") },
            icon = { Icon(Icons.Default.Favorite, contentDescription = null) },
            selected = itemSeleccionado == "Favoritos",
            colors = NavigationDrawerItemDefaults.colors(
                selectedContainerColor = Color(0xFFE8DEF8),
                selectedIconColor = Color(0xFF673AB7),
                selectedTextColor = Color(0xFF673AB7)
            ),
            onClick = { onDestinoClick("Favoritos") },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
        NavigationDrawerItem(
            label = { Text("Perfil") },
            icon = { Icon(Icons.Default.Person, contentDescription = null) },
            selected = itemSeleccionado == "Perfil",
            colors = NavigationDrawerItemDefaults.colors(
                selectedContainerColor = Color(0xFFE8DEF8),
                selectedIconColor = Color(0xFF673AB7),
                selectedTextColor = Color(0xFF673AB7)
            ),
            onClick = { onDestinoClick("Perfil") },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )

        Spacer(modifier = Modifier.weight(1f))
        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))

        NavigationDrawerItem(
            label = { Text("Cerrar sesion") },
            icon = { Icon(Icons.Default.ExitToApp, contentDescription = null) },
            selected = itemSeleccionado == "Cerrar sesión",
            onClick = { onDestinoClick("Cerrar sesión") },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}
