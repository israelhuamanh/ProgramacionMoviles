package com.tecsup.store.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.store.model.Producto
import com.tecsup.store.ui.components.TarjetaProducto

@Composable
fun CatalogoScreen(
    paddingValues: PaddingValues,
    onAccionProducto: (String, Producto) -> Unit = { _, _ -> }
) {
    val categorias = listOf("Todos", "Audífonos", "Smartwatch", "Fundas", "Laptops")
    val productos = listOf(
        Producto(1, "Audífonos Bluetooth", 89.00, "Audífonos"),
        Producto(2, "Smartwatch Pro", 199.00, "Smartwatch"),
        Producto(3, "Funda celular reforzada", 25.00, "Fundas"),
        Producto(4, "Cargador Carga Rápida 65W", 45.00, "Accesorios"),
        Producto(5, "Teclado Mecánico RGB", 150.00, "Accesorios")
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Categorías",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categorias) { cat ->
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFFECEFF1)
                    ) {
                        Text(
                            text = cat,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }

        item {
            Text(
                text = "Más vendidos",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        items(productos) { prod ->
            TarjetaProducto(
                producto = prod,
                onAccion = { accion ->
                    onAccionProducto(accion, prod)
                }
            )
        }
    }
}
