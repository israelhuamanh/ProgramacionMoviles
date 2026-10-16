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
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import com.tecsup.cafecampus.model.Rol
import com.tecsup.cafecampus.ui.components.AdminBottomBar
import com.tecsup.cafecampus.ui.components.CafeButton
import com.tecsup.cafecampus.ui.components.EstadoChip
import com.tecsup.cafecampus.ui.theme.CafeTheme
import com.tecsup.cafecampus.viewmodel.PanelAdminViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PanelScreen(
    panelVm: PanelAdminViewModel,
    onNavigate: (String) -> Unit,
    onVerDetallePedido: (Int) -> Unit
) {
    val nuevos by panelVm.nuevosCount.collectAsState()
    val preparando by panelVm.preparandoCount.collectAsState()
    val listos by panelVm.listosCount.collectAsState()
    val ventasHoy by panelVm.ventasHoy.collectAsState()
    val nuevosPedidos by panelVm.nuevosPedidos.collectAsState()

    CafeTheme(rol = Rol.ADMIN) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Panel de Control - Cafetería") }
                )
            },
            bottomBar = {
                AdminBottomBar(rutaActual = "panel_admin", onNavigate = onNavigate)
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            TarjetaContador("Nuevos", nuevos, Modifier.weight(1f))
                            TarjetaContador("Preparando", preparando, Modifier.weight(1f))
                            TarjetaContador("Listos", listos, Modifier.weight(1f))
                        }
                    }

                    item {
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Ventas del Día",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "S/ %.2f".format(ventasHoy),
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }

                    item {
                        Text(
                            text = "Nuevos pedidos recibidos",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    if (nuevosPedidos.isEmpty()) {
                        item {
                            Text("No hay pedidos nuevos por atender")
                        }
                    } else {
                        items(nuevosPedidos) { p ->
                            Card(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Pedido #${p.pedido.id}",
                                            fontWeight = FontWeight.Bold
                                        )
                                        EstadoChip(p.pedido.estado)
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("Recojo: ${p.pedido.horaRecojo} • Total: S/ %.2f".format(p.pedido.total))
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        CafeButton(
                                            texto = "Pasar a Preparando",
                                            onClick = { panelVm.pasarAPreparando(p.pedido.id) },
                                            modifier = Modifier.weight(1f)
                                        )
                                        CafeButton(
                                            texto = "Ver Detalle",
                                            onClick = { onVerDetallePedido(p.pedido.id) },
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

@Composable
private fun TarjetaContador(etiqueta: String, cantidad: Int, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = cantidad.toString(), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text(text = etiqueta, style = MaterialTheme.typography.bodySmall)
        }
    }
}
