package com.tecsup.publicaciones.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tecsup.publicaciones.AppContainer
import com.tecsup.publicaciones.domain.model.Rol
import com.tecsup.publicaciones.ui.admin.AdminScreen
import com.tecsup.publicaciones.ui.admin.AdminViewModel
import com.tecsup.publicaciones.ui.detalle.DetalleScreen
import com.tecsup.publicaciones.ui.detalle.DetalleViewModel
import com.tecsup.publicaciones.ui.favoritos.FavoritosScreen
import com.tecsup.publicaciones.ui.favoritos.FavoritosViewModel
import com.tecsup.publicaciones.ui.lista.ListaScreen
import com.tecsup.publicaciones.ui.lista.ListaViewModel
import com.tecsup.publicaciones.ui.login.LoginScreen
import com.tecsup.publicaciones.ui.login.LoginViewModel

object Rutas {
    const val LOGIN = "login"
    const val LISTA = "lista"
    const val DETALLE = "detalle/{id}"
    const val FAVORITOS = "favoritos"
    const val ADMIN = "admin"

    fun detalle(id: Int) = "detalle/$id"
}

@Composable
fun AppNav(
    container: AppContainer
) {
    val navController = rememberNavController()
    val loginViewModel: LoginViewModel = viewModel(factory = container.provideLoginFactory())
    var rolActual by rememberSaveable { mutableStateOf(Rol.USUARIO) }

    NavHost(
        navController = navController,
        startDestination = Rutas.LOGIN
    ) {
        composable(Rutas.LOGIN) {
            LoginScreen(
                viewModel = loginViewModel,
                onLoginExitoso = { sesion ->
                    rolActual = sesion.rol
                    navController.navigate(Rutas.LISTA) {
                        popUpTo(Rutas.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        composable(Rutas.LISTA) {
            val listaViewModel: ListaViewModel = viewModel(factory = container.provideListaFactory())
            ListaScreen(
                viewModel = listaViewModel,
                rol = rolActual,
                onItemClick = { id ->
                    navController.navigate(Rutas.detalle(id))
                },
                onIrAFavoritos = {
                    navController.navigate(Rutas.FAVORITOS)
                },
                onIrAAdmin = {
                    if (rolActual == Rol.ADMIN) {
                        navController.navigate(Rutas.ADMIN)
                    }
                },
                onCerrarSesion = {
                    loginViewModel.cerrarSesion()
                    navController.navigate(Rutas.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Rutas.DETALLE,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: 1
            val detalleViewModel: DetalleViewModel = viewModel(
                key = "detalle_$id",
                factory = container.provideDetalleFactory(id)
            )
            DetalleScreen(
                viewModel = detalleViewModel,
                onVolver = { navController.popBackStack() }
            )
        }

        composable(Rutas.FAVORITOS) {
            val favoritosViewModel: FavoritosViewModel = viewModel(
                factory = container.provideFavoritosFactory()
            )
            FavoritosScreen(
                viewModel = favoritosViewModel,
                onItemClick = { id ->
                    navController.navigate(Rutas.detalle(id))
                },
                onVolver = { navController.popBackStack() }
            )
        }

        composable(Rutas.ADMIN) {
            val adminViewModel: AdminViewModel = viewModel(
                factory = container.provideAdminFactory()
            )
            AdminScreen(
                viewModel = adminViewModel,
                onVolver = { navController.popBackStack() }
            )
        }
    }
}
