package com.tecsup.cafecampus.ui.navigation

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.tecsup.cafecampus.model.Rol
import com.tecsup.cafecampus.ui.admin.AjustesScreen
import com.tecsup.cafecampus.ui.admin.DetallePedidoAdminScreen
import com.tecsup.cafecampus.ui.admin.FormularioProductoScreen
import com.tecsup.cafecampus.ui.admin.MenuAdminScreen
import com.tecsup.cafecampus.ui.admin.PanelScreen
import com.tecsup.cafecampus.ui.admin.PedidosAdminScreen
import com.tecsup.cafecampus.ui.cliente.ConfirmarPedidoScreen
import com.tecsup.cafecampus.ui.cliente.DetalleProductoScreen
import com.tecsup.cafecampus.ui.cliente.LoginScreen
import com.tecsup.cafecampus.ui.cliente.MenuScreen
import com.tecsup.cafecampus.ui.cliente.MisPedidosScreen
import com.tecsup.cafecampus.ui.cliente.SeguimientoScreen
import com.tecsup.cafecampus.ui.cliente.SplashScreen
import com.tecsup.cafecampus.viewmodel.CafeViewModelFactory
import com.tecsup.cafecampus.viewmodel.MenuAdminViewModel
import com.tecsup.cafecampus.viewmodel.MenuViewModel
import com.tecsup.cafecampus.viewmodel.PanelAdminViewModel
import com.tecsup.cafecampus.viewmodel.PedidosAdminViewModel
import com.tecsup.cafecampus.viewmodel.PedidosClienteViewModel
import com.tecsup.cafecampus.viewmodel.SesionViewModel

@Composable
fun NavCafe(
    navController: NavHostController,
    factory: CafeViewModelFactory
) {
    val activity = LocalContext.current as ComponentActivity

    val sesionVm: SesionViewModel = viewModel(viewModelStoreOwner = activity, factory = factory)
    val menuVm: MenuViewModel = viewModel(viewModelStoreOwner = activity, factory = factory)
    val pedidosVm: PedidosClienteViewModel = viewModel(viewModelStoreOwner = activity, factory = factory)
    val panelVm: PanelAdminViewModel = viewModel(viewModelStoreOwner = activity, factory = factory)
    val pedidosAdminVm: PedidosAdminViewModel = viewModel(viewModelStoreOwner = activity, factory = factory)
    val menuAdminVm: MenuAdminViewModel = viewModel(viewModelStoreOwner = activity, factory = factory)

    val usuarioActual by sesionVm.usuarioActual.collectAsState()

    NavHost(
        navController = navController,
        startDestination = Rutas.Splash.ruta
    ) {
        composable(Rutas.Splash.ruta) {
            SplashScreen(
                sesionVm = sesionVm,
                onContinuar = { perfil ->
                    val destino = when (perfil) {
                        Rol.CLIENTE.name -> Rutas.Menu.ruta
                        Rol.ADMIN.name -> Rutas.PanelAdmin.ruta
                        else -> Rutas.Login.ruta
                    }
                    navController.navigate(destino) {
                        popUpTo(Rutas.Splash.ruta) { inclusive = true }
                    }
                }
            )
        }

        composable(Rutas.Login.ruta) {
            LoginScreen(
                vm = sesionVm,
                onLoginExito = { rol ->
                    val destino = if (rol == Rol.CLIENTE) Rutas.Menu.ruta else Rutas.PanelAdmin.ruta
                    navController.navigate(destino) {
                        popUpTo(Rutas.Login.ruta) { inclusive = true }
                    }
                }
            )
        }

        composable(Rutas.Menu.ruta) {
            MenuScreen(
                vm = menuVm,
                onProducto = { id ->
                    navController.navigate(Rutas.DetalleProducto.crearRuta(id))
                },
                onIrAlCarrito = {
                    navController.navigate(Rutas.ConfirmarPedido.ruta)
                },
                onNavigate = { ruta ->
                    navController.navigate(ruta) {
                        popUpTo(Rutas.Menu.ruta) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Rutas.DetalleProducto.ruta,
            arguments = listOf(navArgument("productoId") { type = NavType.IntType })
        ) { backStackEntry ->
            val productoId = backStackEntry.arguments?.getInt("productoId") ?: 0
            DetalleProductoScreen(
                productoId = productoId,
                menuVm = menuVm,
                onVolver = { navController.popBackStack() }
            )
        }

        composable(Rutas.ConfirmarPedido.ruta) {
            val clienteId = usuarioActual?.id ?: 1

            ConfirmarPedidoScreen(
                clienteId = clienteId,
                menuVm = menuVm,
                pedidosVm = pedidosVm,
                sesionVm = sesionVm,
                onPedidoConfirmado = { pedidoId ->
                    navController.navigate(Rutas.Seguimiento.crearRuta(pedidoId)) {
                        popUpTo(Rutas.Menu.ruta) { inclusive = false }
                    }
                },
                onVolver = { navController.popBackStack() }
            )
        }

        composable(
            route = Rutas.Seguimiento.ruta,
            arguments = listOf(navArgument("pedidoId") { type = NavType.IntType })
        ) { backStackEntry ->
            val pedidoId = backStackEntry.arguments?.getInt("pedidoId") ?: 0
            SeguimientoScreen(
                pedidoId = pedidoId,
                pedidosVm = pedidosVm,
                onVolverAlMenu = {
                    navController.navigate(Rutas.Menu.ruta) {
                        popUpTo(Rutas.Menu.ruta) { inclusive = true }
                    }
                },
                onNavigate = { ruta ->
                    navController.navigate(ruta) {
                        popUpTo(Rutas.Menu.ruta) { inclusive = true }
                    }
                }
            )
        }

        composable(Rutas.MisPedidos.ruta) {
            val clienteId = usuarioActual?.id ?: 1
            MisPedidosScreen(
                clienteId = clienteId,
                pedidosVm = pedidosVm,
                onVerPedido = { pedidoId ->
                    navController.navigate(Rutas.Seguimiento.crearRuta(pedidoId))
                },
                onNavigate = { ruta ->
                    navController.navigate(ruta) {
                        popUpTo(Rutas.Menu.ruta) { inclusive = true }
                    }
                }
            )
        }

        composable(Rutas.PanelAdmin.ruta) {
            PanelScreen(
                panelVm = panelVm,
                onNavigate = { ruta ->
                    navController.navigate(ruta) {
                        popUpTo(Rutas.PanelAdmin.ruta) { inclusive = true }
                    }
                },
                onVerDetallePedido = { id ->
                    navController.navigate(Rutas.DetallePedidoAdmin.crearRuta(id))
                }
            )
        }

        composable(Rutas.PedidosAdmin.ruta) {
            PedidosAdminScreen(
                pedidosAdminVm = pedidosAdminVm,
                onVerDetallePedido = { id ->
                    navController.navigate(Rutas.DetallePedidoAdmin.crearRuta(id))
                },
                onNavigate = { ruta ->
                    navController.navigate(ruta) {
                        popUpTo(Rutas.PanelAdmin.ruta) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Rutas.DetallePedidoAdmin.ruta,
            arguments = listOf(navArgument("pedidoId") { type = NavType.IntType })
        ) { backStackEntry ->
            val pedidoId = backStackEntry.arguments?.getInt("pedidoId") ?: 0
            DetallePedidoAdminScreen(
                pedidoId = pedidoId,
                pedidosAdminVm = pedidosAdminVm,
                onVolver = { navController.popBackStack() }
            )
        }

        composable(Rutas.MenuAdmin.ruta) {
            MenuAdminScreen(
                menuAdminVm = menuAdminVm,
                onEditarProducto = { id ->
                    navController.navigate(Rutas.FormularioProducto.crearRuta(id))
                },
                onNavigate = { ruta ->
                    navController.navigate(ruta) {
                        popUpTo(Rutas.PanelAdmin.ruta) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Rutas.FormularioProducto.ruta,
            arguments = listOf(navArgument("productoId") {
                type = NavType.IntType
                defaultValue = -1
            })
        ) { backStackEntry ->
            val rawId = backStackEntry.arguments?.getInt("productoId") ?: -1
            val productoId = if (rawId == -1) null else rawId

            FormularioProductoScreen(
                productoId = productoId,
                menuAdminVm = menuAdminVm,
                onVolver = { navController.popBackStack() }
            )
        }

        composable(Rutas.Ajustes.ruta) {
            AjustesScreen(
                sesionVm = sesionVm,
                onCambiarPerfil = { nuevoRol ->
                    val destino = if (nuevoRol == Rol.CLIENTE) Rutas.Menu.ruta else Rutas.PanelAdmin.ruta
                    navController.navigate(destino) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onCerrarSesion = {
                    navController.navigate(Rutas.Login.ruta) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onNavigate = { ruta ->
                    navController.navigate(ruta) {
                        popUpTo(0) { inclusive = false }
                    }
                }
            )
        }
    }
}
