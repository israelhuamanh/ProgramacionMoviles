package com.tecsup.cafecampus.ui.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import com.tecsup.cafecampus.model.EstadoPedido
import com.tecsup.cafecampus.model.Rol
import com.tecsup.cafecampus.ui.components.CafeButton
import com.tecsup.cafecampus.ui.components.EstadoChip
import com.tecsup.cafecampus.ui.theme.CafeTheme
import com.tecsup.cafecampus.viewmodel.PedidosAdminViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetallePedidoAdminScreen(
    pedidoId: Int,
    pedidosAdminVm: PedidosAdminViewModel,
    onVolver: () -> Unit
) {
    val pedidos by pedidosAdminVm.pedidos.collectAsState()
    val item = pedidos.find { it.pedido.id == pedidoId }

    CafeTheme(rol = Rol.ADMIN) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Detalle del Pedido #$pedidoId") },
                    navigationIcon = {
                        IconButton(onClick = onVolver) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                        }
                    }
                )
            }
        ) { padding ->
            if (item == null) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("Cargando información del pedido...")
                }
            } else {
                val pedido = item.pedido
                val estadoActual = try {
                    EstadoPedido.valueOf(pedido.estado)
                } catch (e: Exception) {
                    EstadoPedido.RECIBIDO
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            Card(modifier = Modifier.fillMaxWidth()) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Pedido #${pedido.id}",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold
                                        )
                                        EstadoChip(estado = pedido.estado)
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text("Hora de recojo: ${pedido.horaRecojo}")
                                    Text("Método de pago: ${pedido.pago}")
                                }
                            }
                        }

                        if (pedido.nota.isNotBlank()) {
                            item {
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF3C7))
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text(
                                            text = "Nota del cliente:",
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF92400E)
                                        )
                                        Text(
                                            text = pedido.nota,
                                            color = Color(0xFF78350F)
                                        )
                                    }
                                }
                            }
                        }

                        item {
                            HorizontalDivider()
                            Text(
                                text = "Productos solicitados",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        items(item.detalles) { d ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("${d.cantidad}x ${d.nombreProducto}")
                                Text("S/ %.2f".format(d.precioUnitario * d.cantidad))
                            }
                        }

                        item {
                            HorizontalDivider()
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Total:", fontWeight = FontWeight.Bold)
                                Text(
                                    "S/ %.2f".format(pedido.total),
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val textoBoton = when (estadoActual) {
                            EstadoPedido.RECIBIDO -> "Pasar a Preparando"
                            EstadoPedido.PREPARANDO -> "Pasar a Listo"
                            EstadoPedido.LISTO -> "Pasar a Entregado"
                            else -> null
                        }

                        if (textoBoton != null) {
                            CafeButton(
                                texto = textoBoton,
                                onClick = {
                                    pedidosAdminVm.avanzar(pedido.id, estadoActual)
                                    onVolver()
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        if (estadoActual != EstadoPedido.ENTREGADO && estadoActual != EstadoPedido.CANCELADO) {
                            OutlinedButton(
                                onClick = {
                                    pedidosAdminVm.cancelar(pedido.id)
                                    onVolver()
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Cancelar pedido")
                            }
                        }
                    }
                }
            }
        }
    }
}
