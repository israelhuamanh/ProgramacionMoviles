package com.example.controltareas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
