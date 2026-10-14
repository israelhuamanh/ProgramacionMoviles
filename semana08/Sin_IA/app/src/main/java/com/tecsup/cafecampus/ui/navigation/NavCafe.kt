package com.tecsup.cafecampus.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.tecsup.cafecampus.ui.cliente.MenuScreen
import com.tecsup.cafecampus.ui.cliente.SplashScreen
import com.tecsup.cafecampus.viewmodel.CafeViewModelFactory

@Composable
fun NavCafe(
    navController: NavHostController,
    factory: CafeViewModelFactory
) {
    NavHost(
        navController = navController,
        startDestination = Rutas.Splash.ruta
    ) {
        composable(Rutas.Splash.ruta) {
            SplashScreen(onContinuar = {
                navController.navigate(Rutas.Menu.ruta) {
                    popUpTo(Rutas.Splash.ruta) { inclusive = true }
                }
            })
        }
        composable(Rutas.Menu.ruta) {
            val vm: com.tecsup.cafecampus.viewmodel.MenuViewModel = androidx.lifecycle.viewmodel.compose.viewModel(factory = factory)
            MenuScreen(vm = vm, onProducto = { id ->
                navController.navigate(Rutas.DetalleProducto.crearRuta(id))
            })
        }
    }
}
