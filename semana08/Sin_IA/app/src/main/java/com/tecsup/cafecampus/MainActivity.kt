package com.tecsup.cafecampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.rememberNavController
import com.tecsup.cafecampus.model.Rol
import com.tecsup.cafecampus.ui.navigation.NavCafe
import com.tecsup.cafecampus.ui.theme.CafeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val app = application as CafeApp
        setContent {
            val modoOscuro by app.prefsStore.modoOscuro.collectAsState(initial = false)
            val perfilGuardado by app.prefsStore.perfilActivo.collectAsState(initial = "")

            val rol = if (perfilGuardado == Rol.ADMIN.name) Rol.ADMIN else Rol.CLIENTE

            CafeTheme(
                rol = rol,
                modoOscuro = modoOscuro
            ) {
                val navController = rememberNavController()
                NavCafe(navController = navController, factory = app.factory)
            }
        }
    }
}
