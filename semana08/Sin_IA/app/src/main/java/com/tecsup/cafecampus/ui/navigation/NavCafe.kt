package com.tecsup.cafecampus.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.tecsup.cafecampus.model.Rol
import com.tecsup.cafecampus.ui.cliente.DetalleProductoScreen
import com.tecsup.cafecampus.ui.cliente.LoginScreen
import com.tecsup.cafecampus.ui.cliente.MenuScreen
import com.tecsup.cafecampus.ui.cliente.SplashScreen
import com.tecsup.cafecampus.viewmodel.CafeViewModelFactory
import com.tecsup.cafecampus.viewmodel.MenuViewModel
import com.tecsup.cafecampus.viewmodel.SesionViewModel

@Composable
fun NavCafe(
    navController: NavHostController,
    factory: CafeViewModelFactory
) {
    val sesionVm: SesionViewModel = viewModel(factory = factory)

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
            val vm: MenuViewModel = viewModel(factory = factory)
            MenuScreen(
                vm = vm,
                onProducto = { id ->
                    navController.navigate(Rutas.DetalleProducto.crearRuta(id))
                }
            )
        }

        composable(
            route = Rutas.DetalleProducto.ruta,
            arguments = listOf(navArgument("productoId") { type = NavType.IntType })
        ) { backStackEntry ->
            val productoId = backStackEntry.arguments?.getInt("productoId") ?: 0
            val menuVm: MenuViewModel = viewModel(factory = factory)
            DetalleProductoScreen(
                productoId = productoId,
                menuVm = menuVm,
                onVolver = { navController.popBackStack() }
            )
        }
    }
}
