package com.tecsup.cafecampus.ui.cliente

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.tecsup.cafecampus.model.Rol
import com.tecsup.cafecampus.ui.components.CafeTextField
import com.tecsup.cafecampus.ui.components.ClienteBottomBar
import com.tecsup.cafecampus.ui.components.ProductoCard
import com.tecsup.cafecampus.ui.theme.CafeTheme
import com.tecsup.cafecampus.viewmodel.MenuViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    vm: MenuViewModel,
    onProducto: (Int) -> Unit,
    onIrAlCarrito: () -> Unit,
    onNavigate: (String) -> Unit
) {
    val productos by vm.productos.collectAsState()
    val carrito by vm.carrito.collectAsState()
    val totalItemsCarrito = carrito.sumOf { it.cantidad }
    val totalPrecioCarrito = vm.calcularTotal()

    var categoriaSeleccionada by remember { mutableStateOf("Todos") }
    var busqueda by remember { mutableStateOf("") }

    val categorias = listOf("Todos", "Bebidas", "Sandwiches", "Platos", "Snacks")

    val productosFiltrados = productos.filter { p ->
        val coincideCategoria = (categoriaSeleccionada == "Todos" || p.categoria.equals(categoriaSeleccionada, ignoreCase = true))
        val coincideBusqueda = p.nombre.contains(busqueda, ignoreCase = true)
        coincideCategoria && coincideBusqueda
    }

    CafeTheme(rol = Rol.CLIENTE) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "CafeCampus ☕",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    },
                    actions = {
                        IconButton(onClick = onIrAlCarrito) {
                            BadgedBox(
                                badge = {
                                    if (totalItemsCarrito > 0) {
                                        Badge { Text(totalItemsCarrito.toString()) }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ShoppingCart,
                                    contentDescription = "Carrito de compras"
                                )
                            }
                        }
                    }
                )
            },
            bottomBar = {
                Column {
                    if (totalItemsCarrito > 0) {
                        Button(
                            onClick = onIrAlCarrito,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 6.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Ver Carrito ($totalItemsCarrito ítems)", fontWeight = FontWeight.Bold)
                                Text("S/ %.2f".format(totalPrecioCarrito), fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                    ClienteBottomBar(rutaActual = "menu", onNavigate = onNavigate)
                }
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 12.dp)
            ) {
                CafeTextField(
                    valor = busqueda,
                    onValueChange = { busqueda = it },
                    etiqueta = "Buscar producto...",
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    categorias.forEach { cat ->
                        FilterChip(
                            selected = categoriaSeleccionada == cat,
                            onClick = { categoriaSeleccionada = cat },
                            label = { Text(cat) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (productosFiltrados.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text("No se encontraron productos")
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(productosFiltrados) { p ->
                            ProductoCard(
                                producto = p,
                                onClick = { onProducto(p.id) },
                                onAgregarRapido = { vm.agregarAlCarrito(p, 1) }
                            )
                        }
                    }
                }
            }
        }
    }
}
