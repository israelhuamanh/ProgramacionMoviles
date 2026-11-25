package com.tecsup.avisoscampus.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.tecsup.avisoscampus.R
import com.tecsup.avisoscampus.model.Aviso
import com.tecsup.avisoscampus.model.Origen

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun HistorialAvisos(avisos: List<Aviso>) {
    val iniciales = remember { avisos.take(20).map { it.id }.toSet() }
    val colores = MaterialTheme.colorScheme
    val movimiento = MaterialTheme.motionScheme
    val recientes = avisos.take(20)

    Tarjeta("Historial") {
        if (recientes.isEmpty()) {
            // Estado vacío expresivo con silueta decorativa de la librería de 35 formas (SoftBurst).
            Column(
                modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(colores.surfaceContainerHighest, MaterialShapes.SoftBurst.toShape()),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_campana),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = colores.onSurfaceVariant
                    )
                }
                Text(
                    text = "Sin avisos todavía",
                    color = colores.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        } else {
            // Lista expresiva segmentada M3 Expressive: usa SegmentedListItem y ListItemDefaults.SegmentedGap (2.dp)
            // en lugar de divisores para listas contenidas.
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(ListItemDefaults.SegmentedGap)
            ) {
                recientes.forEachIndexed { indice, aviso ->
                    key(aviso.id) {
                        val entrada = remember {
                            MutableTransitionState(aviso.id in iniciales).apply { targetState = true }
                        }
                        AnimatedVisibility(
                            visibleState = entrada,
                            enter = expandVertically(
                                animationSpec = movimiento.defaultSpatialSpec(),
                                expandFrom = Alignment.Top
                            ) + fadeIn(movimiento.defaultEffectsSpec())
                        ) {
                            val (fondo, tinta, formaAvatar, icono) = when (aviso.origen) {
                                Origen.BOTON -> CuadrupleOrigen(
                                    colores.primaryContainer,
                                    colores.onPrimaryContainer,
                                    MaterialShapes.Cookie9Sided.toShape(),
                                    R.drawable.ic_notificacion
                                )
                                Origen.RECORDATORIO -> CuadrupleOrigen(
                                    colores.tertiaryContainer,
                                    colores.onTertiaryContainer,
                                    MaterialShapes.Sunny.toShape(),
                                    R.drawable.ic_temporizador
                                )
                                Origen.MODO_AVION -> CuadrupleOrigen(
                                    colores.secondaryContainer,
                                    colores.onSecondaryContainer,
                                    MaterialShapes.Clover4Leaf.toShape(),
                                    R.drawable.ic_modo_avion
                                )
                                Origen.CARGADOR -> CuadrupleOrigen(
                                    colores.secondaryContainer,
                                    colores.onSecondaryContainer,
                                    MaterialShapes.Gem.toShape(),
                                    R.drawable.ic_cargador
                                )
                            }
                            SegmentedListItem(
                                shapes = ListItemDefaults.segmentedShapes(
                                    index = indice,
                                    count = recientes.size
                                ),
                                colors = ListItemDefaults.segmentedColors(
                                    containerColor = colores.surfaceContainer
                                ),
                                leadingContent = {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .background(fondo, formaAvatar),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            painter = painterResource(icono),
                                            contentDescription = null,
                                            modifier = Modifier.size(20.dp),
                                            tint = tinta
                                        )
                                    }
                                },
                                overlineContent = {
                                    Surface(
                                        shape = MaterialTheme.shapes.extraSmall,
                                        color = fondo,
                                        contentColor = tinta
                                    ) {
                                        Text(
                                            text = aviso.origen.texto,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                            style = MaterialTheme.typography.labelMediumEmphasized
                                        )
                                    }
                                },
                                trailingContent = {
                                    Text(
                                        text = aviso.hora,
                                        color = colores.onSurfaceVariant,
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontFeatureSettings = "tnum"
                                        )
                                    )
                                },
                                supportingContent = {
                                    Text(
                                        text = aviso.texto,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                },
                                content = {
                                    Text(
                                        text = aviso.titulo,
                                        style = MaterialTheme.typography.titleSmallEmphasized
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

private data class CuadrupleOrigen(
    val fondo: androidx.compose.ui.graphics.Color,
    val tinta: androidx.compose.ui.graphics.Color,
    val forma: androidx.compose.ui.graphics.Shape,
    val icono: Int
)
