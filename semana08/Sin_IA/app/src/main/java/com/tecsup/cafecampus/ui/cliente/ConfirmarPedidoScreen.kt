package com.tecsup.cafecampus.ui.cliente

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
import androidx.compose.material3.FilterChip
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.cafecampus.ui.components.CafeButton
import com.tecsup.cafecampus.ui.components.CafeTextField
import com.tecsup.cafecampus.viewmodel.MenuViewModel
import com.tecsup.cafecampus.viewmodel.PedidosClienteViewModel
import com.tecsup.cafecampus.viewmodel.SesionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmarPedidoScreen(
    clienteId: Int,
    menuVm: MenuViewModel,
    pedidosVm: PedidosClienteViewModel,
    sesionVm: SesionViewModel,
    onPedidoConfirmado: (Int) -> Unit,
    onVolver: () -> Unit
) {
    val items by menuVm.carrito.collectAsState()
    val total = menuVm.calcularTotal()
    val recibirPedidos by sesionVm.recibirPedidos.collectAsState()

    var horaRecojo by remember { mutableStateOf("10:30 AM") }
    var pago by remember { mutableStateOf("YAPE") }
    var nota by remember { mutableStateOf("") }

    val horasDisponibles = listOf("10:15 AM", "10:30 AM", "10:45 AM", "11:00 AM")
    val metodosPago = listOf("YAPE", "CAJA")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Confirmar Pedido") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
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
                if (!recibirPedidos) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFFEE2E2))
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "Cafetería Cerrada",
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF991B1B)
                                )
                                Text(
                                    text = "En este momento no se están recibiendo nuevos pedidos.",
                                    color = Color(0xFF7F1D1D)
                                )
                            }
                        }
                    }
                }

                item {
                    Text(
                        text = "Resumen del pedido",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                items(items) { item ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "${item.cantidad}x ${item.producto.nombre}",
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.SemiBold
                                )
                                if (item.nota.isNotBlank()) {
                                    Text(
                                        text = "Nota: ${item.nota}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                            Text(
                                text = "S/ %.2f".format(item.producto.precio * item.cantidad),
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                item {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                    Text(
                        text = "Hora aproximada de recojo",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        horasDisponibles.forEach { h ->
                            FilterChip(
                                selected = horaRecojo == h,
                                onClick = { horaRecojo = h },
                                label = { Text(h) }
                            )
                        }
                    }
                }

                item {
                    Text(
                        text = "Método de pago",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        metodosPago.forEach { m ->
                            FilterChip(
                                selected = pago == m,
                                onClick = { pago = m },
                                label = { Text(if (m == "YAPE") "Yape / Plin" else "Caja") }
                            )
                        }
                    }
                }

                item {
                    CafeTextField(
                        valor = nota,
                        onValueChange = { nota = it },
                        etiqueta = "Instrucciones de entrega / nota",
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Total a pagar:",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "S/ %.2f".format(total),
                            style = MaterialTheme.typography.headlineSmall,
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
                CafeButton(
                    texto = if (recibirPedidos) "Confirmar pedido" else "Cafetería Cerrada",
                    onClick = {
                        pedidosVm.confirmarPedido(
                            clienteId = clienteId,
                            items = items,
                            horaRecojo = horaRecojo,
                            pago = pago,
                            nota = nota
                        ) { pedidoId ->
                            menuVm.vaciarCarrito()
                            onPedidoConfirmado(pedidoId.toInt())
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = items.isNotEmpty() && recibirPedidos
                )

                OutlinedButton(
                    onClick = onVolver,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Seguir pidiendo")
                }
            }
        }
    }
}
