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
fun PantallaCrear(vm: PostsViewModel) {
    val estado by vm.creacion.collectAsStateWithLifecycle()
    var titulo by rememberSaveable { mutableStateOf("") }
    var texto by rememberSaveable { mutableStateOf("") }
    val ocupado = estado == UiState.Cargando
    LaunchedEffect(estado) {
        if (estado is UiState.Exito) { titulo = ""; texto = "" }
    }
    Column(Modifier.fillMaxSize().imePadding().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Text("Nueva publicación", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        OutlinedTextField(titulo, { titulo = it; vm.nuevaPublicacion() }, Modifier.fillMaxWidth(),
            label = { Text("Título") }, singleLine = true, enabled = !ocupado)
        OutlinedTextField(texto, { texto = it; vm.nuevaPublicacion() }, Modifier.fillMaxWidth(),
            label = { Text("Contenido") }, minLines = 4, enabled = !ocupado)
        Button(onClick = { vm.publicar(titulo, texto) }, enabled = !ocupado && titulo.isNotBlank() && texto.isNotBlank(),
            modifier = Modifier.fillMaxWidth()) { Text("Publicar") }
        when (val actual = estado) {
            UiState.Cargando -> Cargando("Publicando…")
            is UiState.Error -> ErrorConReintento(actual.mensaje) { vm.publicar(titulo, texto) }
            is UiState.Exito -> Surface(color = MaterialTheme.colorScheme.primaryContainer, shape = MaterialTheme.shapes.medium) {
                Text("Publicado con id ${actual.datos.id}", Modifier.fillMaxWidth().padding(16.dp))
            }
            null -> Unit
        }
    }
}
