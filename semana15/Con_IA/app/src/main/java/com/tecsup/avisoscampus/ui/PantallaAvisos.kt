package com.tecsup.avisoscampus.ui

import android.Manifest
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.tecsup.avisoscampus.R
import com.tecsup.avisoscampus.notificacion.Notificador
import com.tecsup.avisoscampus.receiver.*
import com.tecsup.avisoscampus.viewmodel.AvisosViewModel

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun Tarjeta(
    titulo: String,
    destacada: Boolean = false,
    contenido: @Composable ColumnScope.() -> Unit
) {
    val colores = MaterialTheme.colorScheme
    // Contención M3 Expressive: evita esquinas grandes/completas en contenedores densos de texto.
    // Usa largeIncreased (20.dp) para la acción destacada y large (16.dp) para tarjetas secundarias.
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = if (destacada) MaterialTheme.shapes.largeIncreased else MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = if (destacada) colores.primaryContainer else colores.surfaceContainerLow
        )
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                text = titulo,
                modifier = Modifier.semantics { heading() },
                color = if (destacada) colores.onPrimaryContainer else colores.primary,
                style = MaterialTheme.typography.titleMediumEmphasized
            )
            contenido()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun PantallaAvisos(
    vm: AvisosViewModel,
    temaOscuro: Boolean = isSystemInDarkTheme(),
    alCambiarTema: (Boolean) -> Unit = {}
) {
    val contexto = LocalContext.current
    val permiso by vm.permiso.collectAsState()
    val lanzador = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
        vm.permisoRespondido(it)
    }
    LaunchedEffect(contexto) {
        if (Notificador.tienePermiso(contexto)) vm.permisoRespondido(true)
    }
    LaunchedEffect(vm, contexto) {
        vm.eventos.collect { aviso ->
            if (!Notificador.mostrar(contexto, aviso)) vm.noSePudoMostrar()
        }
    }
    val faltan by vm.faltan.collectAsState()
    val escuchando by vm.escuchandoAvion.collectAsState()
    DisposableEffect(contexto, escuchando) {
        if (escuchando) {
            val receptor = ModoAvionReceiver(vm::alCambiarModoAvion)
            ContextCompat.registerReceiver(
                contexto,
                receptor,
                IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED),
                ContextCompat.RECEIVER_NOT_EXPORTED
            )
            onDispose { contexto.unregisterReceiver(receptor) }
        } else onDispose {}
    }
    val avisos by vm.avisos.collectAsState()
    val mensaje by vm.mensaje.collectAsState()
    val cargador by vm.escuchandoCargador.collectAsState()
    DisposableEffect(contexto, cargador) {
        if (cargador) {
            val receptor = CargadorReceiver(vm::alCambiarCargador)
            val filtro = IntentFilter().apply {
                addAction(Intent.ACTION_POWER_CONNECTED)
                addAction(Intent.ACTION_POWER_DISCONNECTED)
            }
            ContextCompat.registerReceiver(contexto, receptor, filtro, ContextCompat.RECEIVER_NOT_EXPORTED)
            onDispose { contexto.unregisterReceiver(receptor) }
        } else onDispose {}
    }
    val barra = TopAppBarDefaults.pinnedScrollBehavior()
    val colores = MaterialTheme.colorScheme
    Scaffold(
        modifier = Modifier.nestedScroll(barra.nestedScrollConnection),
        containerColor = colores.surface,
        topBar = {
            // App bar M3 Expressive (Small TopAppBar en una sola fila): ícono de marca,
            // título + subtítulo y botón toggle tonal (FilledTonalIconToggleButton) con
            // morph de forma (redondo en tema claro -> cuadrado en tema oscuro) para cambiar el tema.
            TopAppBar(
                title = { Text("AvisosCampus") },
                subtitle = { Text("Panel expresivo de notificaciones locales") },
                navigationIcon = {
                    Box(
                        modifier = Modifier
                            .padding(start = 12.dp, end = 4.dp)
                            .size(40.dp)
                            .background(colores.primaryContainer, MaterialShapes.Cookie9Sided.toShape()),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_notificacion),
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = colores.onPrimaryContainer
                        )
                    }
                },
                actions = {
                    FilledTonalIconToggleButton(
                        checked = temaOscuro,
                        onCheckedChange = alCambiarTema,
                        shapes = IconButtonDefaults.toggleableShapes()
                    ) {
                        Icon(
                            painter = painterResource(
                                if (temaOscuro) R.drawable.ic_tema_oscuro else R.drawable.ic_tema_claro
                            ),
                            contentDescription = if (temaOscuro) {
                                "Cambiar a tema claro"
                            } else {
                                "Cambiar a tema oscuro"
                            },
                            modifier = Modifier.size(IconButtonDefaults.smallIconSize)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colores.surface,
                    scrolledContainerColor = colores.surfaceContainer,
                    titleContentColor = colores.onSurface,
                    subtitleContentColor = colores.onSurfaceVariant,
                    navigationIconContentColor = colores.onPrimaryContainer
                ),
                scrollBehavior = barra
            )
        }
    ) { margen ->
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize().padding(margen),
            contentAlignment = Alignment.TopCenter
        ) {
            val ancho = maxWidth.coerceAtMost(1200.dp)
            // Breakpoints M3: Compact (<600.dp) margen 16.dp; Medium (600-839.dp) y Expanded (>=840.dp)
            // margen 24.dp y espaciador entre paneles de 24.dp.
            val margenHorizontal = if (maxWidth < 600.dp) 16.dp else 24.dp
            val doble = ancho >= 840.dp
            val espaciador = 24.dp
            val columna = if (doble) (ancho - margenHorizontal * 2 - espaciador) / 2 else ancho - margenHorizontal * 2
            FlowRow(
                modifier = Modifier
                    .width(ancho)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = margenHorizontal, vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(espaciador),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Column(Modifier.width(columna), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Tarjeta("Permiso de notificaciones") {
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            itemVerticalAlignment = Alignment.CenterVertically
                        ) {
                            EtiquetaPermiso(permiso)
                            FilledTonalButton(
                                shapes = ButtonDefaults.shapes(),
                                contentPadding = ButtonDefaults.SmallContentPadding,
                                onClick = {
                                    if (Build.VERSION.SDK_INT >= 33 && !Notificador.tienePermiso(contexto))
                                        lanzador.launch(Manifest.permission.POST_NOTIFICATIONS)
                                    else vm.permisoRespondido(true)
                                }
                            ) {
                                Text("Permitir notificaciones")
                            }
                        }
                        MensajePermiso(mensaje) {
                            contexto.startActivity(
                                Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                                    .putExtra(Settings.EXTRA_APP_PACKAGE, contexto.packageName)
                            )
                        }
                    }
                    Tarjeta("Avisos", destacada = true) {
                        AccionAviso(vm::avisarAhora)
                    }
                    Tarjeta("Recordatorio") {
                        AccionesRecordatorio(
                            activo = faltan != null,
                            recordar = { vm.recordarEn() },
                            cancelar = vm::cancelarRecordatorio
                        )
                        CuentaRegresiva(faltan)
                    }
                    Tarjeta("Receptor de anuncios") {
                        // Lista expresiva segmentada M3 Expressive para interruptores con gap de 2.dp.
                        Column(verticalArrangement = Arrangement.spacedBy(ListItemDefaults.SegmentedGap)) {
                            OpcionEscucha(
                                texto = "Escuchar el modo avión",
                                indice = 0,
                                icono = R.drawable.ic_modo_avion,
                                activo = escuchando,
                                cambiar = vm::cambiarEscucha
                            )
                            OpcionEscucha(
                                texto = "Escuchar el cargador",
                                indice = 1,
                                icono = R.drawable.ic_cargador,
                                activo = cargador,
                                cambiar = vm::cambiarEscuchaCargador
                            )
                        }
                    }
                }
                Box(Modifier.width(columna)) { HistorialAvisos(avisos) }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun AccionesRecordatorio(
    activo: Boolean,
    recordar: () -> Unit,
    cancelar: () -> Unit
) {
    // Standard ButtonGroup M3 Expressive: los botones responden entre sí con animateWidth y morph al presionar.
    val interaccionRecordar = remember { MutableInteractionSource() }
    val interaccionCancelar = remember { MutableInteractionSource() }
    ButtonGroup(
        overflowIndicator = { estadoMenu ->
            ButtonGroupDefaults.OverflowIndicator(menuState = estadoMenu)
        }
    ) {
        customItem(
            buttonGroupContent = {
                FilledTonalButton(
                    onClick = recordar,
                    enabled = !activo,
                    modifier = Modifier.animateWidth(interaccionRecordar),
                    shapes = ButtonDefaults.shapes(),
                    contentPadding = ButtonDefaults.contentPaddingFor(
                        ButtonDefaults.MinHeight,
                        hasStartIcon = true
                    ),
                    interactionSource = interaccionRecordar
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_temporizador),
                        contentDescription = null,
                        modifier = Modifier.size(ButtonDefaults.SmallIconSize)
                    )
                    Spacer(Modifier.width(ButtonDefaults.IconSpacing))
                    Text("Recordar en 10 s")
                }
            },
            menuContent = { estadoMenu ->
                DropdownMenuItem(
                    text = { Text("Recordar en 10 s") },
                    enabled = !activo,
                    onClick = { recordar(); estadoMenu.dismiss() }
                )
            }
        )
        customItem(
            buttonGroupContent = {
                OutlinedButton(
                    onClick = cancelar,
                    enabled = activo,
                    modifier = Modifier.animateWidth(interaccionCancelar),
                    shapes = ButtonDefaults.shapes(
                        shape = ButtonDefaults.squareShape,
                        pressedShape = ButtonDefaults.pressedShape
                    ),
                    contentPadding = ButtonDefaults.SmallContentPadding,
                    interactionSource = interaccionCancelar
                ) {
                    Text("Cancelar")
                }
            },
            menuContent = { estadoMenu ->
                DropdownMenuItem(
                    text = { Text("Cancelar") },
                    enabled = activo,
                    onClick = { cancelar(); estadoMenu.dismiss() }
                )
            }
        )
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun OpcionEscucha(
    texto: String,
    indice: Int,
    icono: Int,
    activo: Boolean,
    cambiar: (Boolean) -> Unit
) {
    val colores = MaterialTheme.colorScheme
    SegmentedListItem(
        checked = activo,
        onCheckedChange = cambiar,
        shapes = ListItemDefaults.segmentedShapes(index = indice, count = 2),
        colors = ListItemDefaults.segmentedColors(
            containerColor = colores.surfaceContainer,
            selectedContainerColor = colores.secondaryContainer,
            selectedContentColor = colores.onSecondaryContainer,
            selectedLeadingContentColor = colores.onSecondaryContainer
        ),
        leadingContent = {
            Icon(
                painter = painterResource(icono),
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
        },
        trailingContent = {
            Switch(
                checked = activo,
                onCheckedChange = null,
                thumbContent = {
                    Icon(
                        painter = painterResource(if (activo) R.drawable.ic_listo else R.drawable.ic_cerrar),
                        contentDescription = null,
                        modifier = Modifier.size(SwitchDefaults.IconSize)
                    )
                }
            )
        },
        content = {
            Text(
                text = texto,
                style = if (activo) MaterialTheme.typography.bodyLargeEmphasized
                else MaterialTheme.typography.bodyLarge
            )
        }
    )
}
