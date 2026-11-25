package com.tecsup.murocampus.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.tecsup.murocampus.model.Post
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

// La roseta identifica el muro y, en pequeño, acompaña la etiqueta de autor.
@Composable
fun Roseta(color: Color, centro: Color, modifier: Modifier) {
    Canvas(modifier) {
        val radio = size.minDimension / 2
        repeat(8) { i ->
            val angulo = i * PI / 4
            drawCircle(color, radio * .45f, center + Offset(
                cos(angulo).toFloat() * radio * .54f, sin(angulo).toFloat() * radio * .54f))
        }
        drawCircle(color, radio * .57f)
        drawCircle(centro, radio * .24f)
    }
}

@Composable
fun EtiquetaAutor(id: Int) {
    val esquema = MaterialTheme.colorScheme
    val (fondo, tinta) = when (id % 3) {
        1 -> esquema.primaryContainer to esquema.onPrimaryContainer
        2 -> esquema.tertiaryContainer to esquema.onTertiaryContainer
        else -> esquema.secondaryContainer to esquema.onSecondaryContainer
    }
    Surface(shape = RoundedCornerShape(50), color = fondo, contentColor = tinta) {
        Row(Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Roseta(tinta, fondo, Modifier.size(14.dp))
            Text("Usuario $id", style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Composable
fun TarjetaPost(post: Post, primera: Boolean, ultima: Boolean, abrir: () -> Unit) {
    val arriba = if (primera) 28.dp else 8.dp
    val abajo = if (ultima) 28.dp else 8.dp
    Surface(onClick = abrir, modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(arriba, arriba, abajo, abajo),
        color = MaterialTheme.colorScheme.surfaceContainer) {
        Column(Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)) {
            EtiquetaAutor(post.userId)
            Text(post.title, style = MaterialTheme.typography.titleLarge)
            Text(post.body, maxLines = 2, overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun Cargando(mensaje: String = "Cargando publicaciones…") {
    Column(Modifier.fillMaxWidth().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(shape = RoundedCornerShape(22.dp), color = MaterialTheme.colorScheme.primaryContainer) {
            CircularProgressIndicator(Modifier.padding(16.dp).size(36.dp))
        }
        Spacer(Modifier.height(12.dp))
        Text(mensaje, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun ErrorConReintento(mensaje: String, reintentar: () -> Unit) {
    Surface(Modifier.fillMaxWidth().padding(16.dp), color = MaterialTheme.colorScheme.errorContainer,
        shape = RoundedCornerShape(28.dp)) {
        Column(Modifier.padding(22.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text(mensaje, style = MaterialTheme.typography.titleMedium)
            Button(onClick = reintentar, modifier = Modifier.heightIn(min = 48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)) { Text("Reintentar") }
        }
    }
}
