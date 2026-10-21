package com.tecsup.cafecampus.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.cafecampus.model.EstadoPedido

@Composable
fun StepperEstado(estadoActual: EstadoPedido) {
    val pasos = listOf(
        EstadoPedido.RECIBIDO to "Recibido",
        EstadoPedido.PREPARANDO to "Preparando",
        EstadoPedido.LISTO to "Listo",
        EstadoPedido.ENTREGADO to "Entregado"
    )

    val orden = mapOf(
        EstadoPedido.RECIBIDO to 1,
        EstadoPedido.PREPARANDO to 2,
        EstadoPedido.LISTO to 3,
        EstadoPedido.ENTREGADO to 4,
        EstadoPedido.CANCELADO to 0
    )

    val pasoActualNum = orden[estadoActual] ?: 1

    if (estadoActual == EstadoPedido.CANCELADO) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFEE2E2), shape = MaterialTheme.shapes.medium)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Pedido Cancelado ❌",
                color = Color(0xFF991B1B),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )
        }
    } else {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            pasos.forEachIndexed { index, (_, etiqueta) ->
                val pasoNum = index + 1
                val completado = pasoNum <= pasoActualNum
                val esActual = pasoNum == pasoActualNum

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(
                                color = when {
                                    completado -> MaterialTheme.colorScheme.primary
                                    else -> MaterialTheme.colorScheme.surfaceVariant
                                },
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (completado && !esActual) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        } else {
                            Text(
                                text = pasoNum.toString(),
                                color = if (completado) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = etiqueta,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = if (esActual) FontWeight.Bold else FontWeight.Normal,
                        color = if (completado) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
