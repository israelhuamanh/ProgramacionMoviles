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
    var mostrarDialogo by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }

    if (mostrarDialogo) {
        AlertDialog(
            onDismissRequest = { mostrarDialogo = false },
            title = { Text("Cancelar cita") },
            text = { Text("¿Estás seguro de que deseas cancelar la cita con ${cita.medico.nombre}?") },
            confirmButton = {
                TextButton(onClick = {
                    cita.cancelarCita()
                    mostrarDialogo = false
                }) {
                    Text("Sí, cancelar")
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogo = false }) {
                    Text("No")
                }
            }
        )
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = cita.medico.nombre, style = MaterialTheme.typography.titleMedium)
                Text(text = "${cita.fecha}, ${cita.hora}", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))
                val colorEstado = when (cita.estado) {
                    "Confirmada" -> Color(0xFF4CAF50)
                    "Cancelada" -> Color.Red
                    else -> Color.Gray
                }
                Text(
                    text = cita.estado,
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = colorEstado)
                )
                Text(
                    text = cita.obtenerResumen(),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
            if (cita.estado == "Confirmada") {
                Button(
                    onClick = { mostrarDialogo = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Cancelar")
                }
            }
        }
    }
}
