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
    val estado by vm.lista.collectAsStateWithLifecycle()
    Column {
        when (val actual = estado) {
            UiState.Cargando -> Cargando()
            is UiState.Error -> ErrorConReintento(actual.mensaje, vm::cargar)
            is UiState.Exito -> LazyColumn(contentPadding = PaddingValues(start = 16.dp, end = 16.dp,
                top = 8.dp, bottom = 88.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(actual.datos, key = { it.id }) { post -> TarjetaPost(post) { abrir(post.id) } }
            }
        }
    }
}
