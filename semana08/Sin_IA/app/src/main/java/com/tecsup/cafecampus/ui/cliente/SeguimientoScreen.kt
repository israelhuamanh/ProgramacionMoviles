package com.tecsup.cafecampus.ui.cliente

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.cafecampus.model.EstadoPedido
import com.tecsup.cafecampus.model.Rol
import com.tecsup.cafecampus.ui.components.ClienteBottomBar
import com.tecsup.cafecampus.ui.theme.CafeTheme
import com.tecsup.cafecampus.viewmodel.PedidosClienteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeguimientoScreen(
    pedidoId: Int,
    pedidosVm: PedidosClienteViewModel,
    onVolverAlMenu: () -> Unit,
    onNavigate: (String) -> Unit
) {
    val pedidoConDetalle by pedidosVm.observarPedido(pedidoId).collectAsState(initial = null)

    CafeTheme(rol = Rol.CLIENTE) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Seguimiento", fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = onVolverAlMenu) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                        }
                    }
                )
            },
            bottomBar = {
                ClienteBottomBar(rutaActual = "mis_pedidos", onNavigate = onNavigate)
            }
        ) { padding ->
            val data = pedidoConDetalle
            if (data == null) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("Cargando pedido...")
                }
            } else {
                val pedido = data.pedido
                val estadoActual = try {
                    EstadoPedido.valueOf(pedido.estado)
                } catch (e: Exception) {
                    EstadoPedido.RECIBIDO
                }

                val ordenPasos = mapOf(
                    EstadoPedido.RECIBIDO to 1,
                    EstadoPedido.PREPARANDO to 2,
                    EstadoPedido.LISTO to 3,
                    EstadoPedido.ENTREGADO to 4,
                    EstadoPedido.CANCELADO to 0
                )
                val pasoActual = ordenPasos[estadoActual] ?: 1

                val itemsResumen = data.detalles.joinToString(" · ") { "${it.cantidad} x ${it.nombreProducto}" }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        item {
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = "Pedido #%04d".format(pedido.id),
                                            style = MaterialTheme.typography.titleLarge,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text("Recojo a las ${pedido.horaRecojo}", color = Color.White.copy(alpha = 0.9f))
                                        Text("Tiempo estimado: 10 min", color = Color.White.copy(alpha = 0.9f), fontSize = 12.sp)
                                    }

                                    Box(
                                        modifier = Modifier
                                            .size(44.dp)
                                            .background(Color.White.copy(alpha = 0.2f), CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(Icons.Default.AccessTime, contentDescription = null, tint = Color.White)
                                    }
                                }
                            }
                        }

                        item {
                            if (estadoActual == EstadoPedido.CANCELADO) {
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFEE2E2))
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Text("Pedido Cancelado ❌", fontWeight = FontWeight.Bold, color = Color(0xFF991B1B))
                                        Text("El restaurante ha cancelado tu pedido.", color = Color(0xFF7F1D1D))
                                    }
                                }
                            } else {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 8.dp),
                                    verticalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    val pasos = listOf(
                                        1 to ("Recibido" to "Tu pedido llegó a la cocina"),
                                        2 to ("Preparando" to "Están cocinando tu pedido"),
                                        3 to ("Listo" to "Puedes pasar a recogerlo"),
                                        4 to ("Entregado" to "¡Buen provecho!")
                                    )

                                    pasos.forEach { (numPaso, info) ->
                                        val (titulo, desc) = info
                                        val completado = numPaso <= pasoActual
                                        val esActual = numPaso == pasoActual

                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(36.dp)
                                                    .background(
                                                        color = when {
                                                            completado && !esActual -> Color(0xFF16A34A)
                                                            esActual -> MaterialTheme.colorScheme.primary
                                                            else -> MaterialTheme.colorScheme.surfaceVariant
                                                        },
                                                        shape = CircleShape
                                                    ),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                if (completado && !esActual) {
                                                    Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                                                } else {
                                                    Box(
                                                        modifier = Modifier
                                                            .size(12.dp)
                                                            .background(if (esActual) Color.White else Color.Transparent, CircleShape)
                                                    )
                                                }
                                            }

                                            Spacer(modifier = Modifier.width(16.dp))

                                            Column {
                                                Text(
                                                    text = titulo,
                                                    fontWeight = if (esActual) FontWeight.Bold else FontWeight.SemiBold,
                                                    style = MaterialTheme.typography.bodyLarge,
                                                    color = if (completado) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                                Text(
                                                    text = desc,
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        item {
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(text = itemsResumen, style = MaterialTheme.typography.bodyMedium)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Total S/ %.2f".format(pedido.total),
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
