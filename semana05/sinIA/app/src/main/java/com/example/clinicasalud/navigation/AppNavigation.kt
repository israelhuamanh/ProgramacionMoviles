package com.example.clinicasalud.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.clinicasalud.model.Cita
import com.example.clinicasalud.model.medicosMuestra
import com.example.clinicasalud.ui.screens.*

@Composable
fun AppNavigation(navController: NavHostController) {
    val citasAgendadas = remember { mutableStateListOf<Cita>() }

    NavHost(navController = navController, startDestination = "inicio") {
        composable("inicio") {
            PantallaInicio(navController = navController)
        }
        composable(
            "perfil_medico/{medicoId}",
            arguments = listOf(navArgument("medicoId") { type = NavType.IntType })
        ) { backStackEntry ->
            val medicoId = backStackEntry.arguments?.getInt("medicoId")
            val medico = medicosMuestra.find { it.id == medicoId }
            if (medico != null) {
                PantallaPerfilMedico(navController = navController, medico = medico)
            }
        }
        composable(
            "agendar_cita/{medicoId}",
            arguments = listOf(navArgument("medicoId") { type = NavType.IntType })
        ) { backStackEntry ->
            val medicoId = backStackEntry.arguments?.getInt("medicoId")
            val medico = medicosMuestra.find { it.id == medicoId }
            if (medico != null) {
                PantallaAgendarCita(
                    navController = navController,
                    medico = medico,
                    onCitaAgendada = { cita ->
                        citasAgendadas.add(cita)
                    }
                )
            }
        }
        composable(
            "confirmacion/{citaId}",
            arguments = listOf(navArgument("citaId") { type = NavType.IntType })
        ) { backStackEntry ->
            val citaId = backStackEntry.arguments?.getInt("citaId")
            val cita = citasAgendadas.find { it.id == citaId }
            if (cita != null) {
                PantallaConfirmacion(navController = navController, cita = cita)
            }
        }
        composable("mis_citas") {
            PantallaMisCitas(navController = navController, citas = citasAgendadas)
        }
        composable("historial") {
            PantallaHistorial(navController = navController)
        }
    }
}
