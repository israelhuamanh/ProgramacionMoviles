package com.tecsup.murocampus.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.murocampus.model.Post
import com.tecsup.murocampus.viewmodel.*

@Composable
fun PantallaLista(vm: PostsViewModel, abrir: (Int) -> Unit = {}) {
    val estado by vm.listaVisible.collectAsStateWithLifecycle()
    val filtro by vm.usuarioFiltro.collectAsStateWithLifecycle()
    val busqueda by vm.busqueda.collectAsStateWithLifecycle()
    Column {
        OutlinedTextField(value = busqueda, onValueChange = vm::buscar, singleLine = true,
            label = { Text("Buscar por título") },
            modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp, top = 8.dp))
        LazyRow(contentPadding = PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(11) { indice ->
                val usuario = indice.takeIf { it > 0 }
                FilterChip(selected = filtro == usuario, onClick = { vm.filtrar(usuario) },
                    label = { Text(if (usuario == null) "Todos" else "Usuario $usuario") })
            }
        }
        when (val actual = estado) {
            UiState.Cargando -> Cargando()
            is UiState.Error -> ErrorConReintento(actual.mensaje, vm::cargar)
            is UiState.Exito -> if (actual.datos.isEmpty()) {
                Text(if (busqueda.isBlank()) "Sin publicaciones" else "Sin coincidencias", Modifier.padding(16.dp))
            } else key(filtro) { LazyColumn(contentPadding = PaddingValues(start = 16.dp, end = 16.dp,
                top = 8.dp, bottom = 88.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(actual.datos, key = { it.id }) { post -> TarjetaPost(post) { abrir(post.id) } }
            } }
        }
    }
}
