package com.tecsup.murocampus

import androidx.compose.ui.semantics.*
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
                var id by rememberSaveable { mutableStateOf<Int?>(null) }
                var crear by rememberSaveable { mutableStateOf(false) }
                val volver = { vm.cerrarDetalle(); id = null; crear = false }
                BackHandler(enabled = id != null || crear) { volver() }
                Scaffold(topBar = { Cabecera(if (id != null || crear) volver else null) }, floatingActionButton = {
                    if (id == null && !crear) FloatingActionButton(onClick = { vm.nuevaPublicacion(); crear = true },
                        modifier = Modifier.semantics { contentDescription = "Nueva publicación" }) {
                        Text("+", style = MaterialTheme.typography.headlineMedium)
                    }
                }) { padding ->
                    Box(Modifier.padding(padding)) {
                        val seleccionado = id
                        if (crear) PantallaCrear(vm)
                        else if (seleccionado == null) PantallaLista(vm) { id = it }
                        else {
                            LaunchedEffect(seleccionado) { vm.abrirSiNecesario(seleccionado) }
                            PantallaDetalle(vm, seleccionado)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@androidx.compose.runtime.Composable
fun Cabecera(volver: (() -> Unit)? = null) {
    TopAppBar(navigationIcon = {
        if (volver != null) TextButton(onClick = volver) { Text("Atrás", color = MaterialTheme.colorScheme.onPrimary) }
    }, title = { Text("MuroCampus") }, colors = TopAppBarDefaults.topAppBarColors(
        containerColor = MaterialTheme.colorScheme.primary,
        titleContentColor = MaterialTheme.colorScheme.onPrimary
    ))
}
