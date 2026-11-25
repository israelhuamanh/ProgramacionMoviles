package com.tecsup.avisoscampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tecsup.avisoscampus.data.AvisosRepositoryMemoria
import com.tecsup.avisoscampus.ui.PantallaAvisos
import com.tecsup.avisoscampus.ui.theme.TemaCampus
import com.tecsup.avisoscampus.viewmodel.AvisosViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val factory = AvisosViewModelFactory(AvisosRepositoryMemoria)
        setContent {
            val oscuroSistema = isSystemInDarkTheme()
            var preferenciaOscuro by rememberSaveable { mutableStateOf<Boolean?>(null) }
            val temaOscuro = preferenciaOscuro ?: oscuroSistema
            SideEffect {
                val estiloBarra = if (temaOscuro) {
                    SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
                } else {
                    SystemBarStyle.light(
                        android.graphics.Color.TRANSPARENT,
                        android.graphics.Color.TRANSPARENT
                    )
                }
                enableEdgeToEdge(statusBarStyle = estiloBarra, navigationBarStyle = estiloBarra)
            }
            TemaCampus(temaOscuro = temaOscuro) {
                PantallaAvisos(
                    vm = viewModel(factory = factory),
                    temaOscuro = temaOscuro,
                    alCambiarTema = { preferenciaOscuro = it }
                )
            }
        }
    }
}
