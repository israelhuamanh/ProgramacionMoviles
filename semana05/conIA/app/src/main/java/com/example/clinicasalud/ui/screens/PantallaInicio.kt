package com.example.clinicasalud.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.clinicasalud.model.Medico
import com.example.clinicasalud.model.especialidades
import com.example.clinicasalud.model.medicosMuestra
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaInicio(navController: NavHostController, drawerState: DrawerState) {
    val scope = rememberCoroutineScope()
    var especialidadSeleccionada by remember { mutableStateOf<String?>(null) }
    
    val medicosFiltrados = if (especialidadSeleccionada == null) {
        medicosMuestra
    } else {
        medicosMuestra.filter { it.especialidad == especialidadSeleccionada }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Clínica Salud+") },
                navigationIcon = {
                    IconButton(onClick = { scope.launch { drawerState.open() } }) {
                        Icon(Icons.Filled.Menu, contentDescription = "Menú")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp)) {
            Text("Especialidades", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(especialidades) { especialidad ->
                    FilterChip(
                        selected = (especialidad == especialidadSeleccionada),
                        onClick = { 
                            if (especialidadSeleccionada == especialidad) {
                                especialidadSeleccionada = null
                            } else {
                                especialidadSeleccionada = especialidad 
                            }
                        },
                        label = { Text(especialidad) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text("Médicos disponibles", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(medicosFiltrados) { medico ->
                    MedicoCard(medico = medico) {
                        navController.navigate("perfil_medico/${medico.id}")
                    }
                }
            }
        }
    }
}

@Composable
fun MedicoCard(medico: Medico, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = medico.nombre, style = MaterialTheme.typography.titleMedium)
                Text(text = medico.especialidad, style = MaterialTheme.typography.bodyMedium)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Star, contentDescription = "Calificación", tint = MaterialTheme.colorScheme.primary)
                Text(text = medico.calificacion.toString(), style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
