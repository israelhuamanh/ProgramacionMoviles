package com.tecsup.murocampus.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.semantics.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.murocampus.viewmodel.*

@Composable
fun PantallaLista(vm: PostsViewModel, abrir: (Int) -> Unit = {}) {
    val estado by vm.listaVisible.collectAsStateWithLifecycle()
    val filtro by vm.usuarioFiltro.collectAsStateWithLifecycle()
    val busqueda by vm.busqueda.collectAsStateWithLifecycle()
    val colores = MaterialTheme.colorScheme
    Column {
        OutlinedTextField(value = busqueda, onValueChange = vm::buscar, singleLine = true,
            shape = RoundedCornerShape(50), placeholder = { Text("Buscar por título") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            colors = OutlinedTextFieldDefaults.colors(unfocusedContainerColor = colores.surfaceContainer,
                unfocusedBorderColor = colores.surfaceContainer),
            modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp, top = 14.dp)
                .semantics { contentDescription = "Buscar por título" })
        LazyRow(contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            items(11) { indice ->
                val usuario = indice.takeIf { it > 0 }
                FilterChip(selected = filtro == usuario, onClick = { vm.filtrar(usuario) },
                    shape = RoundedCornerShape(if (filtro == usuario) 20.dp else 11.dp),
                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = colores.primary,
                        selectedLabelColor = colores.onPrimary),
                    label = { Text(if (usuario == null) "Todos" else "Usuario $usuario") })
            }
        }
        when (val actual = estado) {
            UiState.Cargando -> Cargando()
            is UiState.Error -> ErrorConReintento(actual.mensaje, vm::cargar)
            is UiState.Exito -> if (actual.datos.isEmpty()) {
                Text(if (busqueda.isBlank()) "Sin publicaciones" else "Sin coincidencias", Modifier.padding(20.dp))
            } else key(filtro) {
                LazyColumn(contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 96.dp),
                    verticalArrangement = Arrangement.spacedBy(3.dp)) {
                    item {
                        Row(Modifier.fillMaxWidth().padding(vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("Publicaciones", style = MaterialTheme.typography.headlineSmall)
                            Surface(shape = RoundedCornerShape(50), color = colores.primaryContainer) {
                                Text(actual.datos.size.toString(), Modifier.padding(horizontal = 9.dp, vertical = 3.dp),
                                    style = MaterialTheme.typography.labelLarge)
                            }
                        }
                    }
                    itemsIndexed(actual.datos, key = { _, post -> post.id }) { i, post ->
                        TarjetaPost(post, primera = i == 0, ultima = i == actual.datos.lastIndex) { abrir(post.id) }
                    }
                }
            }
        }
    }
}
