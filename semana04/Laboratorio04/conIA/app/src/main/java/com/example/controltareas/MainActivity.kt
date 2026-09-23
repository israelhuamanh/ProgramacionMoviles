package com.example.controltareas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.collectAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class Tarea(
    val id: Int,
    val nombre: String,
    val completada: Boolean = false
)

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TareasViewModel : ViewModel() {
    private val _tareas = MutableStateFlow<List<Tarea>>(emptyList())
    val tareas: StateFlow<List<Tarea>> = _tareas.asStateFlow()
    private var contadorId = 1
    
    fun agregarTarea(nombre: String) {
        if (nombre.isNotBlank()) {
            _tareas.value = _tareas.value + Tarea(id = contadorId++, nombre = nombre)
        }
    }
    
    fun eliminarTarea(tarea: Tarea) {
        _tareas.value = _tareas.value.filter { it.id != tarea.id }
    }
    
    fun cambiarEstadoTarea(tarea: Tarea, completada: Boolean) {
        _tareas.value = _tareas.value.map {
            if (it.id == tarea.id) it.copy(completada = completada) else it
        }
    }
}

@Composable
fun ItemTarea(tarea: Tarea, onEliminar: () -> Unit, onCambiarEstado: (Boolean) -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Row(modifier = Modifier.fillMaxWidth().padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Row(modifier = Modifier.weight(1f)) {
                Checkbox(checked = tarea.completada, onCheckedChange = onCambiarEstado)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = tarea.nombre, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(top = 12.dp))
            }
            Button(onClick = onEliminar) { Text("Eliminar") }
        }
    }
}

class MainActivity : ComponentActivity() {
    private val viewModel: TareasViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                PantallaTareas(viewModel)
            }
        }
    }
}

@Composable
fun PantallaTareas(viewModel: TareasViewModel) {
    var textoTarea by remember { mutableStateOf("") }
    val listaTareas by viewModel.tareas.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Lista de tareas", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = textoTarea,
            onValueChange = { textoTarea = it },
            label = { Text("Ingrese una tarea") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                viewModel.agregarTarea(textoTarea)
                textoTarea = ""
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Agregar tarea") }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Total de tareas: ${listaTareas.size}", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn {
            items(listaTareas, key = { it.id }) { tarea ->
                ItemTarea(
                    tarea = tarea,
                    onEliminar = { viewModel.eliminarTarea(tarea) },
                    onCambiarEstado = { viewModel.cambiarEstadoTarea(tarea, it) }
                )
            }
        }
    }
}
