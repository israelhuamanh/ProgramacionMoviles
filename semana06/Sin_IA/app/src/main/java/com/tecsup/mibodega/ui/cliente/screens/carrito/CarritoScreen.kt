package com.tecsup.mibodega.ui.cliente.screens.carrito

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.RemoveShoppingCart
import androidx.compose.material.icons.filled.ShoppingBasket
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.mibodega.ui.cliente.modelo.ItemCarrito
import com.tecsup.mibodega.ui.cliente.modelo.Producto
import com.tecsup.mibodega.ui.componentes.BotonPrimario
import com.tecsup.mibodega.ui.componentes.SelectorCantidad
import com.tecsup.mibodega.ui.theme.GrisClaro
import com.tecsup.mibodega.ui.theme.RojoPrecio
import com.tecsup.mibodega.ui.theme.VerdeBodega

private const val COSTO_DELIVERY_TARIFA = 4.00

@Composable
fun CarritoScreen(
    carrito: List<ItemCarrito>,
    esRecojoEnTienda: Boolean,
    onCambiarTipoEnvio: (Boolean) -> Unit,
    onVolver: () -> Unit,
    onIncrementar: (Producto) -> Unit,
    onDecrementar: (Producto) -> Unit,
    onEliminar: (Producto) -> Unit,
    onContinuarPedido: () -> Unit
) {
    var productoAEliminar by remember { mutableStateOf<Producto?>(null) }

    val subtotal = carrito.sumOf { it.producto.precio * it.cantidad }
    val costoEnvio = if (esRecojoEnTienda || carrito.isEmpty()) 0.0 else COSTO_DELIVERY_TARIFA
    val total = subtotal + costoEnvio

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
    ) {
        EncabezadoCarrito(onVolver = onVolver)

        if (carrito.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.RemoveShoppingCart,
                        contentDescription = "Carrito vacío",
                        tint = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.size(96.dp)
                    )
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = "Tu carrito está vacío",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "Agrega productos desde la sección de inicio para comenzar tu compra.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 20.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(carrito, key = { it.producto.id }) { item ->
                    FilaCarrito(
                        item = item,
                        onIncrementar = { onIncrementar(item.producto) },
                        onDecrementar = { onDecrementar(item.producto) },
                        onSolicitarEliminar = { productoAEliminar = item.producto }
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                }

                item {
                    Spacer(Modifier.height(12.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = "Modalidad de entrega",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(Modifier.height(6.dp))

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .selectable(
                                        selected = !esRecojoEnTienda,
                                        onClick = { onCambiarTipoEnvio(false) },
                                        role = Role.RadioButton
                                    ),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = !esRecojoEnTienda,
                                    onClick = null
                                )
                                Spacer(Modifier.width(8.dp))
                                Text("Delivery a domicilio (S/ 4.00)", style = MaterialTheme.typography.bodyMedium)
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .selectable(
                                        selected = esRecojoEnTienda,
                                        onClick = { onCambiarTipoEnvio(true) },
                                        role = Role.RadioButton
                                    ),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = esRecojoEnTienda,
                                    onClick = null
                                )
                                Spacer(Modifier.width(8.dp))
                                Text("Recojo en tienda (Gratis - S/ 0.00)", style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                }
            }

            ResumenYBoton(
                subtotal = subtotal,
                delivery = costoEnvio,
                total = total,
                onContinuarPedido = onContinuarPedido
            )
        }
    }

    if (productoAEliminar != null) {
        AlertDialog(
            onDismissRequest = { productoAEliminar = null },
            title = { Text("Confirmar eliminación") },
            text = { Text("¿Deseas quitar '${productoAEliminar?.nombre}' del carrito?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        productoAEliminar?.let { onEliminar(it) }
                        productoAEliminar = null
                    }
                ) {
                    Text("Eliminar", color = RojoPrecio, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { productoAEliminar = null }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun EncabezadoCarrito(onVolver: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onVolver) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
        }
        Text(
            text = "Mi carrito",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Composable
private fun FilaCarrito(
    item: ItemCarrito,
    onIncrementar: () -> Unit,
    onDecrementar: () -> Unit,
    onSolicitarEliminar: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(54.dp)
                .background(GrisClaro, RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.ShoppingBasket,
                contentDescription = null,
                tint = VerdeBodega,
                modifier = Modifier.size(28.dp)
            )
        }

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.producto.nombre,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = "S/ %.2f".format(item.producto.precio),
                style = MaterialTheme.typography.labelMedium,
                color = RojoPrecio,
                fontWeight = FontWeight.Bold
            )
        }

        SelectorCantidad(
            cantidad = item.cantidad,
            onIncrementar = onIncrementar,
            onDecrementar = onDecrementar,
            minimo = 1
        )

        IconButton(
            onClick = onSolicitarEliminar,
            modifier = Modifier.padding(start = 4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Eliminar",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ResumenYBoton(
    subtotal: Double,
    delivery: Double,
    total: Double,
    onContinuarPedido: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Subtotal", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("S/ %.2f".format(subtotal), style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
        }

        Spacer(Modifier.height(6.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Costo de delivery", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(
                text = if (delivery == 0.0) "Gratis" else "S/ %.2f".format(delivery),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Total", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(
                text = "S/ %.2f".format(total),
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 22.sp),
                fontWeight = FontWeight.Bold,
                color = VerdeBodega
            )
        }

        Spacer(Modifier.height(16.dp))

        BotonPrimario(
            texto = "Continuar pedido",
            onClick = onContinuarPedido
        )
    }
}
