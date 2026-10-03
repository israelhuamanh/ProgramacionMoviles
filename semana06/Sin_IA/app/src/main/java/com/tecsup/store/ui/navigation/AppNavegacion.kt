package com.tecsup.store.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.tecsup.store.ui.screens.CatalogoScreen
import com.tecsup.store.ui.screens.GenericaScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavegacion() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var destinoActual by remember { mutableStateOf("Inicio") }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawer(
                itemSeleccionado = destinoActual,
                onDestinoClick = { destino ->
                    destinoActual = destino
                    scope.launch { drawerState.close() }
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            if (destinoActual == "Inicio") "TECSUP Store" else destinoActual
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Abrir menú de navegación")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        ) { padding ->
            when (destinoActual) {
                "Inicio" -> CatalogoScreen(paddingValues = padding)
                "Mis pedidos" -> GenericaScreen(titulo = "Mis Pedidos", paddingValues = padding)
                "Favoritos" -> GenericaScreen(titulo = "Favoritos", paddingValues = padding)
                "Perfil" -> GenericaScreen(titulo = "Mi Perfil", paddingValues = padding)
                "Cerrar sesión" -> GenericaScreen(titulo = "Sesión cerrada", paddingValues = padding)
                else -> CatalogoScreen(paddingValues = padding)
            }
        }
    }
}
