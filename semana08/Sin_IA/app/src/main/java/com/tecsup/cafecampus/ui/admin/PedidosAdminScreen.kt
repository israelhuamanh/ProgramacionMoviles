package com.tecsup.cafecampus.ui.admin

import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.cafecampus.model.EstadoPedido
import com.tecsup.cafecampus.model.Rol
import com.tecsup.cafecampus.ui.components.AdminBottomBar
import com.tecsup.cafecampus.ui.components.CafeButton
import com.tecsup.cafecampus.ui.components.EstadoChip
import com.tecsup.cafecampus.ui.theme.CafeTheme
import com.tecsup.cafecampus.viewmodel.PedidosAdminViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PedidosAdminScreen(
    pedidosAdminVm: PedidosAdminViewModel,
    onVerDetallePedido: (Int) -> Unit,
    onNavigate: (String) -> Unit
) {
    val estadoSeleccionado by pedidosAdminVm.estadoSeleccionado.collectAsState()
    val pedidos by pedidosAdminVm.pedidos.collectAsState()

    val estados = EstadoPedido.entries

    CafeTheme(rol = Rol.ADMIN) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Gestión de Pedidos") }
                )
            },
            bottomBar = {
                AdminBottomBar(rutaActual = "pedidos_admin", onNavigate = onNavigate)
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                ) {
                    estados.forEach { e ->
                        FilterChip(
                            selected = estadoSeleccionado == e,
                            onClick = { pedidosAdminVm.cambiarFiltro(e) },
                            label = { Text(e.name) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (pedidos.isEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text("No hay pedidos con estado ${estadoSeleccionado.name}")
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(pedidos) { item ->
                            val p = item.pedido
                            val e = try {
                                EstadoPedido.valueOf(p.estado)
                            } catch (ex: Exception) {
                                EstadoPedido.RECIBIDO
                            }

                            Card(modifier = Modifier.fillMaxWidth()) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Pedido #${p.id}",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold
                                        )
                                        EstadoChip(estado = p.estado)
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("Hora de recojo: ${p.horaRecojo} • Pago: ${p.pago}")
                                    if (p.nota.isNotBlank()) {
                                        Text("Nota: ${p.nota}", style = MaterialTheme.typography.bodySmall)
                                    }
                                    Text(
                                        text = "Total: S/ %.2f".format(p.total),
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        val textoBoton = when (e) {
                                            EstadoPedido.RECIBIDO -> "Pasar a Preparando"
                                            EstadoPedido.PREPARANDO -> "Pasar a Listo"
                                            EstadoPedido.LISTO -> "Pasar a Entregado"
                                            else -> null
                                        }

                                        if (textoBoton != null) {
                                            CafeButton(
                                                texto = textoBoton,
                                                onClick = { pedidosAdminVm.avanzar(p.id, e) },
                                                modifier = Modifier.weight(1f)
                                            )
                                        }

                                        if (e != EstadoPedido.ENTREGADO && e != EstadoPedido.CANCELADO) {
                                            OutlinedButton(
                                                onClick = { pedidosAdminVm.cancelar(p.id) },
                                                modifier = Modifier.weight(1f)
                                            ) {
                                                Text("Cancelar")
                                            }
                                        }

                                        CafeButton(
                                            texto = "Ver Detalle",
                                            onClick = { onVerDetallePedido(p.id) },
                                            modifier = Modifier.weight(1f)
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
}
