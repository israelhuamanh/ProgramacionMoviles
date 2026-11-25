package com.tecsup.avisoscampus.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.MotionDurationScale
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.*
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.graphics.shapes.Morph
import com.tecsup.avisoscampus.R
import com.tecsup.avisoscampus.model.PermisoUi
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun EtiquetaPermiso(permiso: PermisoUi) {
    val colores = MaterialTheme.colorScheme
    // Usa pares de roles accesibles (garantizan mínimo 3:1 en tema claro y oscuro según M3 Color Roles).
    val (etiqueta, fondo, tinta) = when (permiso) {
        PermisoUi.Concedido -> Triple("Permitido", colores.tertiaryContainer, colores.onTertiaryContainer)
        PermisoUi.Denegado -> Triple("Denegado", colores.errorContainer, colores.onErrorContainer)
        PermisoUi.SinPedir -> Triple("Sin pedir", colores.surfaceContainerHighest, colores.onSurfaceVariant)
    }
    val movimiento = MaterialTheme.motionScheme
    val colorFondo by animateColorAsState(fondo, movimiento.fastEffectsSpec(), label = "Fondo del permiso")
    val colorTexto by animateColorAsState(tinta, movimiento.fastEffectsSpec(), label = "Tinta del permiso")
    Surface(color = colorFondo, contentColor = colorTexto, shape = MaterialTheme.shapes.small) {
        Crossfade(etiqueta, animationSpec = movimiento.fastEffectsSpec(), label = "Etiqueta de permiso") {
            Text(
                text = it,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                style = MaterialTheme.typography.labelLargeEmphasized
            )
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun MensajePermiso(mensaje: String?, abrirAjustes: () -> Unit) {
    val movimiento = MaterialTheme.motionScheme
    val colores = MaterialTheme.colorScheme
    var ultimo by remember { mutableStateOf("") }
    SideEffect { if (mensaje != null) ultimo = mensaje }
    AnimatedVisibility(
        visible = mensaje != null,
        enter = expandVertically(movimiento.defaultSpatialSpec()) + fadeIn(movimiento.fastEffectsSpec()),
        exit = shrinkVertically(movimiento.fastSpatialSpec()) + fadeOut(movimiento.fastEffectsSpec())
    ) {
        // Redondez óptica M3 Expressive: tarjeta exterior 20.dp (largeIncreased) - 16.dp padding = 4.dp (extraSmall).
        Surface(
            color = colores.errorContainer,
            contentColor = colores.onErrorContainer,
            shape = MaterialTheme.shapes.extraSmall
        ) {
            Column(Modifier.fillMaxWidth().padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = mensaje ?: ultimo,
                    modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite },
                    style = MaterialTheme.typography.bodyMedium
                )
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(
                        onClick = abrirAjustes,
                        colors = ButtonDefaults.textButtonColors(contentColor = colores.onErrorContainer)
                    ) {
                        Text("Abrir ajustes", style = MaterialTheme.typography.labelLargeEmphasized)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun rememberMorphShape(morph: Morph, progress: Float): Shape =
    remember(morph, progress) {
        object : Shape {
            private val path = Path()
            override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
                morph.toPath(progress = progress, path = path)
                val matrix = Matrix().apply { scale(size.width, size.height) }
                path.transform(matrix)
                path.translate(size.center - path.getBounds().center)
                return Outline.Generic(path)
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
    val colores = MaterialTheme.colorScheme
    // Hero moment M3 Expressive: shape morphing (Cookie9Sided -> Sunny) + spatial spring + effects spring.
    val progresoMorph by animateFloatAsState(
        targetValue = if (confirmado) 1f else 0f,
        animationSpec = movimiento.defaultSpatialSpec(),
        label = "Morph de sello"
    )
    val giro by animateFloatAsState(
        targetValue = if (confirmado) 12f else 0f,
        animationSpec = movimiento.fastSpatialSpec(),
        label = "Rotación de confirmación"
    )
    val fondoSello by animateColorAsState(
        targetValue = if (confirmado) colores.tertiary else colores.primary,
        animationSpec = movimiento.fastEffectsSpec(),
        label = "Color del sello"
    )
    val tintaSello by animateColorAsState(
        targetValue = if (confirmado) colores.onTertiary else colores.onPrimary,
        animationSpec = movimiento.fastEffectsSpec(),
        label = "Tinta del sello"
    )
    val morph = remember { Morph(MaterialShapes.Cookie9Sided, MaterialShapes.Sunny) }
    val formaSello = rememberMorphShape(morph, progresoMorph)
    val alto = ButtonDefaults.MediumContainerHeight
    val tamanoIcono = ButtonDefaults.iconSizeFor(alto)
    val espacioIcono = ButtonDefaults.iconSpacingFor(alto)

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(Modifier.weight(1f)) {
            Button(
                onClick = { mostrar(); pulsaciones++ },
                modifier = Modifier.fillMaxWidth().heightIn(min = alto),
                shapes = ButtonDefaults.shapesFor(alto),
                contentPadding = ButtonDefaults.contentPaddingFor(alto, hasStartIcon = true)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_campana),
                    contentDescription = null,
                    modifier = Modifier.size(tamanoIcono)
                )
                Spacer(Modifier.width(espacioIcono))
                Text("Mostrar aviso", style = MaterialTheme.typography.titleMediumEmphasized)
            }
        }
        Box(
            modifier = Modifier
                .size(64.dp)
                .rotate(giro)
                .background(fondoSello, formaSello),
            contentAlignment = Alignment.Center
        ) {
            Crossfade(confirmado, animationSpec = movimiento.fastEffectsSpec(), label = "Sello del aviso") { listo ->
                Icon(
                    painter = painterResource(if (listo) R.drawable.ic_listo else R.drawable.ic_notificacion),
                    contentDescription = null,
                    modifier = Modifier.size(32.dp).rotate(-giro),
                    tint = tintaSello
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun CuentaRegresiva(faltan: Int?) {
    val movimiento = MaterialTheme.motionScheme
    val colores = MaterialTheme.colorScheme
    val escala = rememberCoroutineScope().coroutineContext[MotionDurationScale]?.scaleFactor ?: 1f
    // Conserva la cifra durante la transición de salida al cancelar.
    var ultimo by remember { mutableIntStateOf(10) }
    SideEffect { if (faltan != null) ultimo = faltan }
    AnimatedVisibility(
        visible = faltan != null,
        enter = expandVertically(movimiento.defaultSpatialSpec()) + fadeIn(movimiento.fastEffectsSpec()),
        exit = shrinkVertically(movimiento.fastSpatialSpec()) + fadeOut(movimiento.fastEffectsSpec())
    ) {
        val restante = faltan ?: ultimo
        val progreso by animateFloatAsState(
            targetValue = restante / 10f,
            animationSpec = movimiento.defaultEffectsSpec(),
            label = "Tiempo restante"
        )
        // Redondez óptica: 20.dp (largeIncreased) - 16.dp padding = 4.dp (extraSmall).
        // Tratamiento editorial M3 Expressive con cifras tabulares (tnum) para evitar desplazamientos.
        Surface(
            color = colores.tertiaryContainer,
            contentColor = colores.onTertiaryContainer,
            shape = MaterialTheme.shapes.extraSmall
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Entrega programada",
                        style = MaterialTheme.typography.labelLargeEmphasized
                    )
                    AnimatedContent(
                        targetState = restante,
                        transitionSpec = {
                            (slideInVertically(movimiento.fastSpatialSpec()) { it / 3 } +
                                fadeIn(movimiento.fastEffectsSpec()))
                                .togetherWith(
                                    slideOutVertically(movimiento.fastSpatialSpec()) { -it / 3 } +
                                        fadeOut(movimiento.fastEffectsSpec())
                                )
                        },
                        label = "Segundos"
                    ) { segundos ->
                        Text(
                            text = "Faltan $segundos s",
                            color = colores.onTertiaryContainer,
                            style = MaterialTheme.typography.headlineSmallEmphasized.copy(
                                fontFeatureSettings = "tnum"
                            )
                        )
                    }
                }
                if (escala == 0f) {
                    LinearProgressIndicator(
                        progress = { progreso },
                        modifier = Modifier.fillMaxWidth(),
                        color = colores.tertiary,
                        trackColor = colores.surfaceContainerHighest
                    )
                } else {
                    LinearWavyProgressIndicator(
                        progress = { progreso },
                        modifier = Modifier.fillMaxWidth(),
                        color = colores.tertiary,
                        trackColor = colores.surfaceContainerHighest
                    )
                }
            }
        }
    }
}
