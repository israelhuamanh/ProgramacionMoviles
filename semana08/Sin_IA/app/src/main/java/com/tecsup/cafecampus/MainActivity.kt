package com.tecsup.cafecampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.tecsup.cafecampus.ui.navigation.NavCafe
import com.tecsup.cafecampus.ui.theme.CafeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val app = application as CafeApp
        setContent {
            CafeTheme {
                val navController = rememberNavController()
                NavCafe(navController = navController, factory = app.factory)
            }
        }
    }
}
