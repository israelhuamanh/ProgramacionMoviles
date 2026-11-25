package com.tecsup.avisoscampus.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.avisoscampus.model.Aviso
import com.tecsup.avisoscampus.model.Origen

@Composable
fun HistorialAvisos(avisos: List<Aviso>) {
    val iniciales = remember { avisos.take(20).map { it.id }.toSet() }
    val colores = MaterialTheme.colorScheme
    val movimiento = MaterialTheme.motionScheme
    Tarjeta("Historial") {
        if (avisos.isEmpty()) Text("Sin avisos todavía", color = colores.onSurfaceVariant)
        avisos.take(20).forEachIndexed { indice, aviso -> key(aviso.id) {
            val entrada = remember { MutableTransitionState(aviso.id in iniciales).apply { targetState = true } }
            AnimatedVisibility(entrada, enter = expandVertically(movimiento.defaultSpatialSpec(), expandFrom = Alignment.Top)
                + fadeIn(movimiento.defaultEffectsSpec())) {
                Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    if (indice > 0) HorizontalDivider(Modifier.padding(bottom = 6.dp), color = colores.outlineVariant)
                    val (fondo, tinta) = when (aviso.origen) {
                        Origen.BOTON -> colores.primaryContainer to colores.onPrimaryContainer
                        Origen.RECORDATORIO -> colores.tertiaryContainer to colores.onTertiaryContainer
                        Origen.MODO_AVION, Origen.CARGADOR -> colores.secondaryContainer to colores.onSecondaryContainer
                    }
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Surface(shape = MaterialTheme.shapes.small, color = fondo, contentColor = tinta) {
                            Text(aviso.origen.texto, Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelMediumEmphasized)
                        }
                        Spacer(Modifier.weight(1f))
                        Text(aviso.hora, color = colores.onSurfaceVariant, style = MaterialTheme.typography.labelMedium)
                    }
                    Text(aviso.titulo, style = MaterialTheme.typography.titleSmallEmphasized)
                    Text(aviso.texto, style = MaterialTheme.typography.bodyMedium)
                }
            }
        } }
    }
}
