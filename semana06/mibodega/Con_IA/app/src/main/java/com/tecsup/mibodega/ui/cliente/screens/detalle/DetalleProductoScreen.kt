package com.tecsup.mibodega.ui.cliente.screens.detalle

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ShoppingBasket
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.mibodega.ui.cliente.modelo.Producto
import com.tecsup.mibodega.ui.componentes.BotonPrimario
import com.tecsup.mibodega.ui.componentes.SelectorCantidad
import com.tecsup.mibodega.ui.theme.GrisClaro
import com.tecsup.mibodega.ui.theme.RojoPrecio
import com.tecsup.mibodega.ui.theme.VerdeBodega

@Composable
fun DetalleProductoScreen(
    producto: Producto,
    esFavorito: Boolean,
    onToggleFavorito: () -> Unit,
    onVolver: () -> Unit,
    onAgregarAlCarrito: (Producto, Int) -> Unit
) {
    var cantidad by remember { mutableStateOf(1) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
    ) {
        EncabezadoDetalle(
            onVolver = onVolver,
            esFavorito = esFavorito,
            onToggleFavorito = onToggleFavorito
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .aspectRatio(1.2f)
                .background(GrisClaro, RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.ShoppingBasket,
                contentDescription = producto.nombre,
                tint = VerdeBodega,
                modifier = Modifier.size(96.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            Spacer(Modifier.height(20.dp))

            Text(
                text = producto.nombre,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = "Categoría: ${producto.categoria}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "S/ %.2f".format(producto.precio),
                style = MaterialTheme.typography.displaySmall.copy(fontSize = 28.sp),
                color = RojoPrecio,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(14.dp))

            Text(
                text = producto.descripcion,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Cantidad",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                SelectorCantidad(
                    cantidad = cantidad,
                    onIncrementar = { cantidad++ },
                    onDecrementar = { if (cantidad > 1) cantidad-- }
                )
            }

            Spacer(Modifier.weight(1f))

            BotonPrimario(
                texto = "Agregar al carrito",
                subtexto = "Total: S/ %.2f".format(producto.precio * cantidad),
                icono = rememberVectorPainter(Icons.Default.ShoppingCart),
                onClick = { onAgregarAlCarrito(producto, cantidad) }
            )

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun EncabezadoDetalle(
    onVolver: () -> Unit,
    esFavorito: Boolean,
    onToggleFavorito: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onVolver) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
        }
        IconButton(onClick = onToggleFavorito) {
            Icon(
                imageVector = if (esFavorito) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = "Favorito",
                tint = if (esFavorito) RojoPrecio else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
