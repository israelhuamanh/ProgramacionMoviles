package com.example.clinicasalud.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.clinicasalud.model.Cita
import com.example.clinicasalud.model.Medico

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaAgendarCita(
    navController: NavHostController,
    medico: Medico,
    drawerState: DrawerState,
    onCitaAgendada: (Cita) -> Unit
) {
    var fechaSeleccionada by remember { mutableStateOf<String?>(null) }
    var horaSeleccionada by remember { mutableStateOf<String?>(null) }

    val fechas = listOf("Jue 26", "Vie 27", "Sáb 28")
    val horas = listOf("9:00", "10:30", "15:00")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Agendar cita") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text("Selecciona fecha", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.selectableGroup()) {
                fechas.forEach { fecha ->
                    Row(
                        Modifier
                            .selectable(
                                selected = (fecha == fechaSeleccionada),
                                onClick = { fechaSeleccionada = fecha },
                                role = Role.RadioButton
                            )
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (fecha == fechaSeleccionada),
                            onClick = null
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(fecha)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Selecciona hora", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.selectableGroup()) {
                horas.forEach { hora ->
                    Row(
                        Modifier
                            .selectable(
                                selected = (hora == horaSeleccionada),
                                onClick = { horaSeleccionada = hora },
                                role = Role.RadioButton
                            )
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (hora == horaSeleccionada),
                            onClick = null
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(hora)
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    if (fechaSeleccionada != null && horaSeleccionada != null) {
                        val nuevaCita = Cita(
                            id = (1..1000).random(),
                            medico = medico,
                            fecha = fechaSeleccionada!!,
                            hora = horaSeleccionada!!,
                            estado = "Confirmada"
                        )
                        onCitaAgendada(nuevaCita)
                        navController.navigate("confirmacion/${nuevaCita.id}") {
                            popUpTo("inicio") { inclusive = false }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = fechaSeleccionada != null && horaSeleccionada != null
            ) {
                Text("Confirmar cita")
            }
        }
    }
}
