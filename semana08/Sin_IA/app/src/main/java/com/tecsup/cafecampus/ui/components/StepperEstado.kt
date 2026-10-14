package com.tecsup.cafecampus.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.tecsup.cafecampus.model.EstadoPedido

@Composable
fun StepperEstado(estadoActual: EstadoPedido) {
    Row {
        Text("Estado actual: ${estadoActual.name}")
    }
}
