package com.example.clinicasalud.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.clinicasalud.model.Cita
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMisCitas(navController: NavHostController, citas: List<Cita>, drawerState: DrawerState) {
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis citas") },
                navigationIcon = {
                    IconButton(onClick = { scope.launch { drawerState.open() } }) {
                        Icon(Icons.Filled.Menu, contentDescription = "Menú")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp)) {
            if (citas.isEmpty()) {
                Text("No tienes citas agendadas.")
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(citas) { cita ->
                        CitaCard(cita = cita)
                    }
                }
            }
        }
    }
}

@Composable
fun CitaCard(cita: Cita) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = cita.medico.nombre, style = MaterialTheme.typography.titleMedium)
            Text(text = "${cita.fecha}, ${cita.hora}", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            val colorEstado = if (cita.estado == "Confirmada") Color(0xFF4CAF50) else Color.Gray
            Text(
                text = cita.estado,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = colorEstado)
            )
        }
    }
}
