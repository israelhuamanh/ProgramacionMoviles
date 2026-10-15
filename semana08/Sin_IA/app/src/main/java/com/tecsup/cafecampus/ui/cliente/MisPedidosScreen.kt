package com.tecsup.cafecampus.ui.cliente

import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.cafecampus.model.EstadoPedido
import com.tecsup.cafecampus.ui.components.EstadoChip
import com.tecsup.cafecampus.viewmodel.PedidosClienteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MisPedidosScreen(
    clienteId: Int,
    pedidosVm: PedidosClienteViewModel,
    onVerPedido: (Int) -> Unit
) {
    val pedidos by pedidosVm.observarMisPedidos(clienteId).collectAsState(initial = emptyList())
    var tabActivos by remember { mutableStateOf(true) }

    val pedidosFiltrados = pedidos.filter { p ->
        val e = p.pedido.estado
        if (tabActivos) {
            e == EstadoPedido.RECIBIDO.name || e == EstadoPedido.PREPARANDO.name || e == EstadoPedido.LISTO.name
        } else {
            e == EstadoPedido.ENTREGADO.name || e == EstadoPedido.CANCELADO.name
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Pedidos") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FilterChip(
                    selected = tabActivos,
                    onClick = { tabActivos = true },
                    label = { Text("Activos") }
                )
                FilterChip(
                    selected = !tabActivos,
                    onClick = { tabActivos = false },
                    label = { Text("Historial") }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (pedidosFiltrados.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("No hay pedidos en esta sección")
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(pedidosFiltrados) { item ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onVerPedido(item.pedido.id) }
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Pedido #${item.pedido.id}",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    EstadoChip(estado = item.pedido.estado)
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Hora recojo: ${item.pedido.horaRecojo}")
                                Text(
                                    text = "Total: S/ %.2f".format(item.pedido.total),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
