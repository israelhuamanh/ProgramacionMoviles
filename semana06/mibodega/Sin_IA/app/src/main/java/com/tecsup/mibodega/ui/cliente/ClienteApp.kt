package com.tecsup.mibodega.ui.cliente

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tecsup.mibodega.ui.cliente.modelo.ItemCarrito
import com.tecsup.mibodega.ui.cliente.modelo.Pedido
import com.tecsup.mibodega.ui.cliente.modelo.Producto
import com.tecsup.mibodega.ui.cliente.modelo.listaProductosFake
import com.tecsup.mibodega.ui.cliente.screens.bienvenida.BienvenidaScreen
import com.tecsup.mibodega.ui.cliente.screens.carrito.CarritoScreen
import com.tecsup.mibodega.ui.cliente.screens.confirmacion.ConfirmacionScreen
import com.tecsup.mibodega.ui.cliente.screens.detalle.DetalleProductoScreen
import com.tecsup.mibodega.ui.cliente.screens.entrega.DatosEntregaScreen
import com.tecsup.mibodega.ui.cliente.screens.inicio.InicioScreen
import com.tecsup.mibodega.ui.cliente.screens.login.LoginScreen
import com.tecsup.mibodega.ui.cliente.screens.registro.RegistroScreen

@Composable
fun ClienteApp(
    esModoOscuro: Boolean = false,
    onToggleModoOscuro: (Boolean) -> Unit = {}
) {
    val navController = rememberNavController()

    var carrito by remember { mutableStateOf<List<ItemCarrito>>(emptyList()) }
    var favoritos by remember { mutableStateOf<Set<Int>>(emptySet()) }
    val pedidos = remember { mutableStateListOf<Pedido>() }
    var esRecojoEnTienda by remember { mutableStateOf(false) }

    NavHost(
        navController = navController,
        startDestination = Rutas.BIENVENIDA,
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(300)
            ) + fadeIn(animationSpec = tween(300))
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(300)
            ) + fadeOut(animationSpec = tween(300))
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(300)
            ) + fadeIn(animationSpec = tween(300))
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(300)
            ) + fadeOut(animationSpec = tween(300))
        }
    ) {
        composable(Rutas.BIENVENIDA) {
            BienvenidaScreen(
                onRegistrarse = { navController.navigate(Rutas.REGISTRO) },
                onIniciarSesion = { navController.navigate(Rutas.LOGIN) },
                onTerminos = { /* Enlace de terminos */ }
            )
        }

        composable(Rutas.LOGIN) {
            LoginScreen(
                onVolver = { navController.popBackStack() },
                onLoginExitoso = {
                    navController.navigate(Rutas.INICIO) {
                        popUpTo(Rutas.BIENVENIDA) { inclusive = true }
                    }
                }
            )
        }

        composable(Rutas.REGISTRO) {
            RegistroScreen(
                onVolver = { navController.popBackStack() },
                onCrearCuenta = { _, _, _, _ ->
                    navController.navigate(Rutas.INICIO) {
                        popUpTo(Rutas.BIENVENIDA) { inclusive = true }
                    }
                }
            )
        }

        composable(Rutas.INICIO) {
            InicioScreen(
                cantidadCarrito = carrito.sumOf { it.cantidad },
                favoritos = favoritos,
                pedidos = pedidos,
                esModoOscuro = esModoOscuro,
                onToggleModoOscuro = onToggleModoOscuro,
                onToggleFavorito = { prodId ->
                    favoritos = if (favoritos.contains(prodId)) favoritos - prodId else favoritos + prodId
                },
                onVerCarrito = { navController.navigate(Rutas.CARRITO) },
                onProductoClick = { producto ->
                    navController.navigate(Rutas.detalle(producto.id))
                },
                onAgregarProducto = { producto ->
                    carrito = agregarOSumarProducto(carrito, producto, 1)
                }
            )
        }

        composable(
            route = Rutas.DETALLE,
            arguments = listOf(navArgument("productoId") { type = NavType.IntType })
        ) { backStackEntry ->
            val productoId = backStackEntry.arguments?.getInt("productoId") ?: 0
            val producto = listaProductosFake.firstOrNull { it.id == productoId } ?: listaProductosFake.first()

            DetalleProductoScreen(
                producto = producto,
                esFavorito = favoritos.contains(producto.id),
                onToggleFavorito = {
                    favoritos = if (favoritos.contains(producto.id)) favoritos - producto.id else favoritos + producto.id
                },
                onVolver = { navController.popBackStack() },
                onAgregarAlCarrito = { prod, cant ->
                    carrito = agregarOSumarProducto(carrito, prod, cant)
                    navController.popBackStack()
                }
            )
        }

        composable(Rutas.CARRITO) {
            CarritoScreen(
                carrito = carrito,
                esRecojoEnTienda = esRecojoEnTienda,
                onCambiarTipoEnvio = { esRecojoEnTienda = it },
                onVolver = { navController.popBackStack() },
                onIncrementar = { producto ->
                    carrito = carrito.map {
                        if (it.producto.id == producto.id) it.copy(cantidad = it.cantidad + 1) else it
                    }
                },
                onDecrementar = { producto ->
                    carrito = carrito.mapNotNull {
                        when {
                            it.producto.id != producto.id -> it
                            it.cantidad > 1 -> it.copy(cantidad = it.cantidad - 1)
                            else -> null
                        }
                    }
                },
                onEliminar = { producto ->
                    carrito = carrito.filterNot { it.producto.id == producto.id }
                },
                onContinuarPedido = {
                    navController.navigate(Rutas.DATOS_ENTREGA)
                }
            )
        }

        composable(Rutas.DATOS_ENTREGA) {
            val subtotal = carrito.sumOf { it.producto.precio * it.cantidad }
            val costoEnvio = if (esRecojoEnTienda) 0.0 else 4.00
            val total = subtotal + costoEnvio

            DatosEntregaScreen(
                esRecojoEnTienda = esRecojoEnTienda,
                onVolver = { navController.popBackStack() },
                onConfirmarPedido = { nombre, telefono, dir, ref, metodoPago ->
                    val nuevoPedido = Pedido(
                        items = carrito,
                        subtotal = subtotal,
                        costoEnvio = costoEnvio,
                        total = total,
                        esRecojoEnTienda = esRecojoEnTienda,
                        nombreCliente = nombre,
                        telefonoCliente = telefono,
                        direccionCliente = dir,
                        referenciaCliente = ref,
                        metodoPago = metodoPago
                    )
                    pedidos.add(0, nuevoPedido)
                    carrito = emptyList()

                    navController.navigate(Rutas.confirmacion(nuevoPedido.id)) {
                        popUpTo(Rutas.INICIO) { inclusive = false }
                    }
                }
            )
        }

        composable(
            route = Rutas.CONFIRMACION,
            arguments = listOf(navArgument("pedidoId") { type = NavType.StringType })
        ) { backStackEntry ->
            val pedidoId = backStackEntry.arguments?.getString("pedidoId")
            val pedido = pedidos.firstOrNull { it.id == pedidoId } ?: pedidos.firstOrNull()

            ConfirmacionScreen(
                pedido = pedido,
                onVolverAlInicio = {
                    navController.navigate(Rutas.INICIO) {
                        popUpTo(Rutas.INICIO) { inclusive = true }
                    }
                }
            )
        }
    }
}

private fun agregarOSumarProducto(
    carrito: List<ItemCarrito>,
    producto: Producto,
    cantidad: Int
): List<ItemCarrito> {
    val itemExistente = carrito.find { it.producto.id == producto.id }
    return if (itemExistente != null) {
        carrito.map {
            if (it.producto.id == producto.id) it.copy(cantidad = it.cantidad + cantidad) else it
        }
    } else {
        carrito + ItemCarrito(producto = producto, cantidad = cantidad)
    }
}