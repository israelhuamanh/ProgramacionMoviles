package com.tecsup.murocampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.*
import androidx.compose.ui.unit.dp
import com.tecsup.murocampus.data.PostRepositoryRemote
import com.tecsup.murocampus.data.remote.RetrofitClient
import com.tecsup.murocampus.ui.*
import com.tecsup.murocampus.ui.theme.MuroTheme
import com.tecsup.murocampus.viewmodel.*

class MainActivity : ComponentActivity() {
    private val vm by viewModels<PostsViewModel> {
        PostsViewModelFactory(PostRepositoryRemote(RetrofitClient.api))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(statusBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT))
        setContent {
            MuroTheme {
                var id by rememberSaveable { mutableStateOf<Int?>(null) }
                var crear by rememberSaveable { mutableStateOf(false) }
                val volver = { vm.cerrarDetalle(); id = null; crear = false }
                BackHandler(enabled = id != null || crear) { volver() }
                Scaffold(topBar = { Cabecera(if (id != null || crear) volver else null) }, floatingActionButton = {
                    if (id == null && !crear) ExtendedFloatingActionButton(
                        modifier = Modifier.semantics { contentDescription = "Nueva publicación" },
                        onClick = { vm.nuevaPublicacion(); crear = true },
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        shape = RoundedCornerShape(22.dp),
                        icon = { Icon(Icons.Default.Add, contentDescription = null) },
                        text = { Text("Nueva publicación") }
                    )
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

@Composable
fun Cabecera(volver: (() -> Unit)? = null) {
    val colores = MaterialTheme.colorScheme
    Surface(shape = RoundedCornerShape(bottomEnd = 32.dp), color = colores.primary, contentColor = colores.onPrimary) {
        Row(Modifier.fillMaxWidth().statusBarsPadding().padding(start = if (volver == null) 20.dp else 4.dp,
            end = 20.dp, top = 8.dp, bottom = 14.dp), verticalAlignment = Alignment.CenterVertically) {
            if (volver != null) IconButton(onClick = volver) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
            }
            Text("MuroCampus", modifier = Modifier.weight(1f).semantics { heading() },
                style = if (volver == null) MaterialTheme.typography.headlineLarge else MaterialTheme.typography.titleLarge)
            Roseta(colores.secondaryContainer, colores.primary, Modifier.size(if (volver == null) 42.dp else 30.dp))
        }
    }
}
