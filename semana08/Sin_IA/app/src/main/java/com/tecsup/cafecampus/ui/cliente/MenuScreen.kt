package com.tecsup.cafecampus.ui.cliente

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.tecsup.cafecampus.ui.components.ProductoCard
import com.tecsup.cafecampus.viewmodel.MenuViewModel

@Composable
fun MenuScreen(
    vm: MenuViewModel,
    onProducto: (Int) -> Unit
) {
    val productos by vm.productos.collectAsState()

    LazyVerticalGrid(columns = GridCells.Fixed(2)) {
        items(productos) { p ->
            ProductoCard(producto = p) {
                onProducto(p.id)
            }
        }
    }
}
