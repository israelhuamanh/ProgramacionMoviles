package com.example.clinicasalud.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun DrawerContent(navController: NavHostController, closeDrawer: () -> Unit) {
    ModalDrawerSheet {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Clínica Salud+",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)
        )
        HorizontalDivider()
        NavigationDrawerItem(
            label = { Text("Inicio") },
            selected = false,
            onClick = {
                navController.navigate("inicio") {
                    popUpTo("inicio") { inclusive = true }
                }
                closeDrawer()
            },
            modifier = Modifier.padding(8.dp)
        )
        NavigationDrawerItem(
            label = { Text("Mis Citas") },
            selected = false,
            onClick = {
                navController.navigate("mis_citas")
                closeDrawer()
            },
            modifier = Modifier.padding(8.dp)
        )
        NavigationDrawerItem(
            label = { Text("Historial médico") },
            selected = false,
            onClick = {
                navController.navigate("historial")
                closeDrawer()
            },
            modifier = Modifier.padding(8.dp)
        )
    }
}
