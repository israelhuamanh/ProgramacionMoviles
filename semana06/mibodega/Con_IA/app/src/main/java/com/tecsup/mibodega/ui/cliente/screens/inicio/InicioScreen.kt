package com.tecsup.mibodega.ui.cliente.screens.inicio

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.cliente.modelo.Pedido
import com.tecsup.mibodega.ui.cliente.modelo.Producto
import com.tecsup.mibodega.ui.cliente.modelo.listaCategorias
import com.tecsup.mibodega.ui.cliente.modelo.listaProductosFake
import com.tecsup.mibodega.ui.componentes.ProductoCard
import com.tecsup.mibodega.ui.theme.GrisClaro
import com.tecsup.mibodega.ui.theme.VerdeBodega

enum class DestinoNav { INICIO, CATEGORIAS, PEDIDOS, PERFIL }
enum class OrdenPrecio { NINGUNO, MENOR_A_MAYOR, MAYOR_A_MENOR }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InicioScreen(
    productos: List<Producto> = listaProductosFake,
    cantidadCarrito: Int,
    favoritos: Set<Int>,
    pedidos: List<Pedido>,
    esModoOscuro: Boolean,
    onToggleModoOscuro: (Boolean) -> Unit,
    onToggleFavorito: (Int) -> Unit,
    onVerCarrito: () -> Unit,
    onProductoClick: (Producto) -> Unit,
    onAgregarProducto: (Producto) -> Unit
) {
    var seccionActual by remember { mutableStateOf(DestinoNav.INICIO) }
    var categoriaSeleccionada by remember { mutableStateOf(listaCategorias.first()) }
    var queryBusqueda by remember { mutableStateOf("") }
    var ordenPrecio by remember { mutableStateOf(OrdenPrecio.NINGUNO) }
    var menuOrdenExpandido by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        when (seccionActual) {
                            DestinoNav.INICIO -> "Mi Bodega"
                            DestinoNav.CATEGORIAS -> "Categorías y Favoritos"
                            DestinoNav.PEDIDOS -> "Mis Pedidos"
                            DestinoNav.PERFIL -> "Mi Perfil"
                        },
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(onClick = onVerCarrito) {
                        BadgedBox(
                            badge = {
                                if (cantidadCarrito > 0) {
                                    Badge { Text("$cantidadCarrito") }
                                }
                            }
                        ) {
                            Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito")
                        }
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = seccionActual == DestinoNav.INICIO,
                    onClick = { seccionActual = DestinoNav.INICIO },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
                    label = { Text("Inicio") }
                )
                NavigationBarItem(
                    selected = seccionActual == DestinoNav.CATEGORIAS,
                    onClick = { seccionActual = DestinoNav.CATEGORIAS },
                    icon = { Icon(Icons.Default.List, contentDescription = "Categorías") },
                    label = { Text("Categorías") }
                )
                NavigationBarItem(
                    selected = seccionActual == DestinoNav.PEDIDOS,
                    onClick = { seccionActual = DestinoNav.PEDIDOS },
                    icon = { Icon(Icons.Default.Receipt, contentDescription = "Pedidos") },
                    label = { Text("Pedidos") }
                )
                NavigationBarItem(
                    selected = seccionActual == DestinoNav.PERFIL,
                    onClick = { seccionActual = DestinoNav.PERFIL },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
                    label = { Text("Perfil") }
                )
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (seccionActual) {
                DestinoNav.INICIO -> {
                    // Filtrado en tiempo real asistido por IA combinando texto de búsqueda y categoría
                    val productosFiltrados = productos.filter { prod ->
                        val coincideCategoria = categoriaSeleccionada == "Todos" || prod.categoria == categoriaSeleccionada
                        val coincideTexto = queryBusqueda.isBlank() ||
                                prod.nombre.contains(queryBusqueda, ignoreCase = true) ||
                                prod.descripcion.contains(queryBusqueda, ignoreCase = true)
                        coincideCategoria && coincideTexto
                    }

                    val productosOrdenados = when (ordenPrecio) {
                        OrdenPrecio.MENOR_A_MAYOR -> productosFiltrados.sortedBy { it.precio }
                        OrdenPrecio.MAYOR_A_MENOR -> productosFiltrados.sortedByDescending { it.precio }
                        OrdenPrecio.NINGUNO -> productosFiltrados
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp)
                    ) {
                        Spacer(Modifier.height(8.dp))

                        // Campo de búsqueda en tiempo real reactivo con botón de limpiar
                        OutlinedTextField(
                            value = queryBusqueda,
                            onValueChange = { queryBusqueda = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("Buscar productos en tiempo real...") },
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                            trailingIcon = {
                                if (queryBusqueda.isNotEmpty()) {
                                    IconButton(onClick = { queryBusqueda = "" }) {
                                        Icon(Icons.Default.Clear, contentDescription = "Limpiar búsqueda")
                                    }
                                }
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        )

                        Spacer(Modifier.height(14.dp))

                        // LazyRow de categorías con iconos
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(listaCategorias) { categoria ->
                                ItemCategoria(
                                    nombre = categoria,
                                    icono = when (categoria) {
                                        "Todos" -> Icons.Default.Store
                                        "Bebidas" -> Icons.Default.LocalDrink
                                        "Abarrotes" -> Icons.Default.ShoppingBag
                                        "Snacks" -> Icons.Default.Fastfood
                                        else -> Icons.Default.Store
                                    },
                                    seleccionado = categoria == categoriaSeleccionada,
                                    onClick = { categoriaSeleccionada = categoria }
                                )
                            }
                        }

                        Spacer(Modifier.height(16.dp))

                        // Encabezado con Ordenamiento por precio y contador
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (queryBusqueda.isNotBlank()) "Resultados (${productosOrdenados.size})" else "Productos destacados",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )

                            Box {
                                OutlinedButton(
                                    onClick = { menuOrdenExpandido = true },
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Icon(Icons.Default.FilterList, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(Modifier.width(4.dp))
                                    Text(
                                        text = when (ordenPrecio) {
                                            OrdenPrecio.NINGUNO -> "Ordenar"
                                            OrdenPrecio.MENOR_A_MAYOR -> "Menor $"
                                            OrdenPrecio.MAYOR_A_MENOR -> "Mayor $"
                                        },
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                    Icon(Icons.Default.ArrowDropDown, contentDescription = null, modifier = Modifier.size(16.dp))
                                }

                                DropdownMenu(
                                    expanded = menuOrdenExpandido,
                                    onDismissRequest = { menuOrdenExpandido = false }
                                ) {
                                    DropdownMenuItem(
                                        text = { Text("Por defecto") },
                                        onClick = {
                                            ordenPrecio = OrdenPrecio.NINGUNO
                                            menuOrdenExpandido = false
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("Precio: Menor a Mayor") },
                                        onClick = {
                                            ordenPrecio = OrdenPrecio.MENOR_A_MAYOR
                                            menuOrdenExpandido = false
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("Precio: Mayor a Menor") },
                                        onClick = {
                                            ordenPrecio = OrdenPrecio.MAYOR_A_MENOR
                                            menuOrdenExpandido = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.height(10.dp))

                        // Si no hay coincidencias con la búsqueda
                        if (productosOrdenados.isEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                                    .padding(24.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(
                                        imageVector = Icons.Default.SearchOff,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.outline,
                                        modifier = Modifier.size(64.dp)
                                    )
                                    Spacer(Modifier.height(12.dp))
                                    Text(
                                        text = "No se encontraron productos",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(Modifier.height(6.dp))
                                    Text(
                                        text = "Intenta buscar con otro término o selecciona otra categoría.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        } else {
                            // Grid de productos filtrados
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                contentPadding = PaddingValues(bottom = 16.dp),
                                modifier = Modifier.fillMaxSize()
                            ) {
                                items(productosOrdenados, key = { it.id }) { producto ->
                                    ProductoCard(
                                        producto = producto,
                                        onClick = { onProductoClick(producto) },
                                        onAgregar = { onAgregarProducto(producto) },
                                        esFavorito = favoritos.contains(producto.id),
                                        onToggleFavorito = { onToggleFavorito(producto.id) }
                                    )
                                }
                            }
                        }
                    }
                }

                DestinoNav.CATEGORIAS -> {
                    val productosFavoritos = productos.filter { favoritos.contains(it.id) }
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Tus Productos Favoritos (${productosFavoritos.size})",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(12.dp))

                        if (productosFavoritos.isEmpty()) {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(24.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("Aún no tienes productos marcados como favoritos. Toca el ícono de corazón para agregarlos.")
                                }
                            }
                        } else {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                modifier = Modifier.fillMaxSize()
                            ) {
                                items(productosFavoritos, key = { it.id }) { prod ->
                                    ProductoCard(
                                        producto = prod,
                                        onClick = { onProductoClick(prod) },
                                        onAgregar = { onAgregarProducto(prod) },
                                        esFavorito = true,
                                        onToggleFavorito = { onToggleFavorito(prod.id) }
                                    )
                                }
                            }
                        }
                    }
                }

                DestinoNav.PEDIDOS -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Historial de Pedidos",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(12.dp))

                        if (pedidos.isEmpty()) {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(24.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("No tienes pedidos confirmados todavía.")
                                }
                            }
                        } else {
                            LazyColumn(
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                modifier = Modifier.fillMaxSize()
                            ) {
                                items(pedidos) { pedido ->
                                    Card(
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                                    ) {
                                        Column(modifier = Modifier.padding(16.dp)) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Text(
                                                    text = "Pedido ${pedido.id}",
                                                    fontWeight = FontWeight.Bold,
                                                    style = MaterialTheme.typography.titleSmall
                                                )
                                                Text(
                                                    text = pedido.fecha,
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            }
                                            Spacer(Modifier.height(6.dp))
                                            Text(
                                                text = if (pedido.esRecojoEnTienda) "Modalidad: Recojo en tienda" else "Entrega: ${pedido.direccionCliente}",
                                                style = MaterialTheme.typography.bodySmall
                                            )
                                            Text(
                                                text = "Pago: ${pedido.metodoPago}",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                            Spacer(Modifier.height(8.dp))
                                            Text(
                                                text = "Total: S/ %.2f".format(pedido.total),
                                                fontWeight = FontWeight.Bold,
                                                color = VerdeBodega
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                DestinoNav.PERFIL -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp)
                    ) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(72.dp)
                                        .background(VerdeBodega, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = "Usuario",
                                        tint = MaterialTheme.colorScheme.onPrimary,
                                        modifier = Modifier.size(40.dp)
                                    )
                                }
                                Spacer(Modifier.height(10.dp))
                                Text("Usuario Mi Bodega", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                                Text("cliente@bodega.com", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }

                        Spacer(Modifier.height(20.dp))

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text("Modo oscuro", fontWeight = FontWeight.SemiBold)
                                    Text(
                                        text = if (esModoOscuro) "Activado" else "Desactivado",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Switch(
                                    checked = esModoOscuro,
                                    onCheckedChange = onToggleModoOscuro
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ItemCategoria(
    nombre: String,
    icono: ImageVector,
    seleccionado: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(54.dp)
                .background(
                    if (seleccionado) VerdeBodega else GrisClaro,
                    RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icono,
                contentDescription = nombre,
                tint = if (seleccionado) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(28.dp)
            )
        }
        Spacer(Modifier.height(4.dp))
        Text(
            text = nombre,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.Normal,
            color = if (seleccionado) VerdeBodega else MaterialTheme.colorScheme.onSurface
        )
    }
}