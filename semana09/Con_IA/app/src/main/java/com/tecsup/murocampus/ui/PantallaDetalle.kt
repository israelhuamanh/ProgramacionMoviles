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
fun PantallaDetalle(vm: PostsViewModel, id: Int) {
    val estado by vm.detalle.collectAsStateWithLifecycle()
    val borrado by vm.borrado.collectAsStateWithLifecycle()
    var confirmar by rememberSaveable(id) { mutableStateOf(false) }
    if (confirmar) AlertDialog(
        onDismissRequest = { confirmar = false },
        title = { Text("¿Eliminar publicación?") },
        text = { Text("La publicación se quitará de este muro.") },
        confirmButton = { TextButton(onClick = { confirmar = false; vm.eliminar(id) }) { Text("Eliminar") } },
        dismissButton = { TextButton(onClick = { confirmar = false }) { Text("Cancelar") } }
    )
    if (borrado is UiState.Exito) {
        Text("Publicación eliminada", Modifier.padding(20.dp), style = MaterialTheme.typography.titleLarge)
        return
    }
    when (val actual = estado) {
        UiState.Cargando -> Cargando("Cargando publicación…")
        is UiState.Error -> ErrorConReintento(actual.mensaje) { vm.abrir(id) }
        is UiState.Exito -> {
            val (post, comentarios) = actual.datos
            LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                item {
                    Surface(Modifier.fillMaxWidth(), shape = RoundedCornerShape(30.dp, 30.dp, 30.dp, 8.dp),
                        color = MaterialTheme.colorScheme.primaryContainer, contentColor = MaterialTheme.colorScheme.onPrimaryContainer) {
                        Column(Modifier.padding(22.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            EtiquetaAutor(post.userId)
                            Text(post.title, style = MaterialTheme.typography.headlineSmall)
                            Text(post.body)
                            TextButton(onClick = { confirmar = true }, enabled = borrado != UiState.Cargando) {
                                Text(if (borrado == UiState.Cargando) "Eliminando…" else "Eliminar publicación",
                                    color = MaterialTheme.colorScheme.error)
                            }
                            val error = borrado as? UiState.Error
                            if (error != null) ErrorConReintento(error.mensaje) { confirmar = true }
                        }
                    }
                }
                item { Text("Comentarios (${comentarios.size})", style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold) }
                items(comentarios, key = { it.id }) { comentario ->
                    Surface(Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp, 24.dp, 24.dp, 24.dp),
                        color = MaterialTheme.colorScheme.surfaceContainer) {
                        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(comentario.email, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
                            Text(comentario.body, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}
