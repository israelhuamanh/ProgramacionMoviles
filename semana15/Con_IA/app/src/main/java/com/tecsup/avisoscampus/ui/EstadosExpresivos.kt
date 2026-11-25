package com.tecsup.avisoscampus.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.MotionDurationScale
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.*
import androidx.compose.ui.unit.dp
import com.tecsup.avisoscampus.R
import com.tecsup.avisoscampus.model.PermisoUi
import kotlinx.coroutines.delay

@Composable
internal fun EtiquetaPermiso(permiso: PermisoUi) {
    val colores = MaterialTheme.colorScheme
    val oscuro = isSystemInDarkTheme()
    val (etiqueta, fondo, tinta) = when (permiso) {
        PermisoUi.Concedido -> if (oscuro) Triple("Permitido", Color(0xFF164B34), Color(0xFFB8F3D1))
            else Triple("Permitido", Color(0xFFBCEFD3), Color(0xFF064527))
        PermisoUi.Denegado -> Triple("Denegado", colores.errorContainer, colores.onErrorContainer)
        PermisoUi.SinPedir -> Triple("Sin pedir", colores.surfaceContainerHigh, colores.onSurfaceVariant)
    }
    val movimiento = MaterialTheme.motionScheme
    val color by animateColorAsState(fondo, movimiento.fastEffectsSpec(), label = "Estado del permiso")
    Surface(color = color, contentColor = tinta, shape = MaterialTheme.shapes.small) {
        Crossfade(etiqueta, animationSpec = movimiento.fastEffectsSpec(), label = "Etiqueta de permiso") {
            Text(it, Modifier.padding(horizontal = 12.dp, vertical = 6.dp), style = MaterialTheme.typography.labelLargeEmphasized)
        }
    }
}

@Composable
internal fun MensajePermiso(mensaje: String?, abrirAjustes: () -> Unit) {
    val movimiento = MaterialTheme.motionScheme
    var ultimo by remember { mutableStateOf("") }
    SideEffect { if (mensaje != null) ultimo = mensaje }
    AnimatedVisibility(mensaje != null,
        enter = expandVertically(movimiento.defaultSpatialSpec()) + fadeIn(movimiento.fastEffectsSpec()),
        exit = shrinkVertically(movimiento.fastSpatialSpec()) + fadeOut(movimiento.fastEffectsSpec())) {
        Surface(color = MaterialTheme.colorScheme.errorContainer, shape = MaterialTheme.shapes.medium) {
            Column(Modifier.fillMaxWidth().padding(12.dp)) {
                Text(mensaje ?: ultimo, Modifier.semantics { liveRegion = LiveRegionMode.Polite })
                TextButton(onClick = abrirAjustes) { Text("Abrir ajustes") }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun AccionAviso(mostrar: () -> Unit) {
    var pulsaciones by remember { mutableIntStateOf(0) }
    var confirmado by remember { mutableStateOf(false) }
    LaunchedEffect(pulsaciones) {
        if (pulsaciones > 0) {
            confirmado = true
            delay(1300)
            confirmado = false
        }
    }
    val movimiento = MaterialTheme.motionScheme
    val giro by animateFloatAsState(if (confirmado) 12f else 0f, movimiento.fastSpatialSpec(), label = "Confirmación")
    val alto = ButtonDefaults.MediumContainerHeight
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Box(Modifier.weight(1f)) {
            Button(onClick = { mostrar(); pulsaciones++ }, modifier = Modifier.heightIn(min = alto),
                shapes = ButtonDefaults.shapesFor(alto), contentPadding = ButtonDefaults.contentPaddingFor(alto)) {
                Text("Mostrar aviso", style = MaterialTheme.typography.titleMediumEmphasized)
            }
        }
        Box(Modifier.size(64.dp).rotate(giro).background(MaterialTheme.colorScheme.primary,
            MaterialShapes.Cookie9Sided.toShape()), contentAlignment = Alignment.Center) {
            Crossfade(confirmado, animationSpec = movimiento.fastEffectsSpec(), label = "Sello del aviso") { listo ->
                Icon(painterResource(if (listo) R.drawable.ic_listo else R.drawable.ic_notificacion),
                    contentDescription = null, modifier = Modifier.size(32.dp).rotate(-giro),
                    tint = MaterialTheme.colorScheme.onPrimary)
            }
        }
    }
}

@Composable
internal fun CuentaRegresiva(faltan: Int?) {
    val movimiento = MaterialTheme.motionScheme
    val escala = rememberCoroutineScope().coroutineContext[MotionDurationScale]?.scaleFactor ?: 1f
    // Conserva la cifra durante la transición de salida al cancelar.
    var ultimo by remember { mutableIntStateOf(10) }
    SideEffect { if (faltan != null) ultimo = faltan }
    AnimatedVisibility(faltan != null,
        enter = expandVertically(movimiento.defaultSpatialSpec()) + fadeIn(movimiento.fastEffectsSpec()),
        exit = shrinkVertically(movimiento.fastSpatialSpec()) + fadeOut(movimiento.fastEffectsSpec())) {
        val restante = faltan ?: ultimo
        val progreso by animateFloatAsState(restante / 10f, movimiento.defaultEffectsSpec(), label = "Tiempo restante")
        Column(Modifier.fillMaxWidth().padding(vertical = 4.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            AnimatedContent(restante, transitionSpec = {
                (slideInVertically(movimiento.fastSpatialSpec()) { it / 3 } + fadeIn(movimiento.fastEffectsSpec()))
                    .togetherWith(slideOutVertically(movimiento.fastSpatialSpec()) { -it / 3 } + fadeOut(movimiento.fastEffectsSpec()))
            }, label = "Segundos") { segundos ->
                Text("Faltan $segundos s", color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.headlineSmallEmphasized)
            }
            if (escala == 0f) {
                LinearProgressIndicator(progress = { progreso }, modifier = Modifier.fillMaxWidth())
            } else {
                LinearWavyProgressIndicator(progress = { progreso }, modifier = Modifier.fillMaxWidth())
            }
        }
    }
}
