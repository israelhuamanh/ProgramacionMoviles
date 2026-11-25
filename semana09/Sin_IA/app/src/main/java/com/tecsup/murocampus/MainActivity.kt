package com.tecsup.murocampus

import android.os.Bundle
import androidx.activity.viewModels
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import com.tecsup.murocampus.data.PostRepositoryRemote
import com.tecsup.murocampus.data.remote.RetrofitClient
import com.tecsup.murocampus.viewmodel.*
import com.tecsup.murocampus.ui.*
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import com.tecsup.murocampus.ui.theme.MuroTheme

class MainActivity : ComponentActivity() {
    private val vm by viewModels<PostsViewModel> {
        PostsViewModelFactory(PostRepositoryRemote(RetrofitClient.api))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MuroTheme {
                Scaffold(topBar = { Cabecera() }) { padding ->
                    Box(Modifier.padding(padding)) { PantallaLista(vm) }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@androidx.compose.runtime.Composable
fun Cabecera() {
    TopAppBar(title = { Text("MuroCampus") }, colors = TopAppBarDefaults.topAppBarColors(
        containerColor = MaterialTheme.colorScheme.primary,
        titleContentColor = MaterialTheme.colorScheme.onPrimary
    ))
}
