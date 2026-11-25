package com.tecsup.avisoscampus

import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tecsup.avisoscampus.data.AvisosRepositoryMemoria
import com.tecsup.avisoscampus.ui.PantallaAvisos
import com.tecsup.avisoscampus.ui.theme.TemaCampus
import com.tecsup.avisoscampus.viewmodel.AvisosViewModelFactory
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // El fondo azul de la barra superior es oscuro en el tema claro y claro en el oscuro.
        val temaOscuro = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
        val estiloBarra = if (temaOscuro) SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
            else SystemBarStyle.dark(Color.TRANSPARENT)
        enableEdgeToEdge(statusBarStyle = estiloBarra)
        val factory = AvisosViewModelFactory(AvisosRepositoryMemoria())
        setContent { TemaCampus { PantallaAvisos(viewModel(factory = factory)) } }
    }
}
