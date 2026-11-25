package com.tecsup.avisoscampus

import android.os.Bundle
import androidx.activity.ComponentActivity
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
        enableEdgeToEdge()
        val factory = AvisosViewModelFactory(AvisosRepositoryMemoria())
        setContent { TemaCampus { PantallaAvisos(viewModel(factory = factory)) } }
    }
}
