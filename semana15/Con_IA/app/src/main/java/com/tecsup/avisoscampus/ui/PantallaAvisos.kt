package com.tecsup.avisoscampus.ui

import android.Manifest
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.tecsup.avisoscampus.model.PermisoUi
import com.tecsup.avisoscampus.notificacion.Notificador
import com.tecsup.avisoscampus.receiver.*
import com.tecsup.avisoscampus.viewmodel.AvisosViewModel

@Composable
internal fun Tarjeta(titulo: String, destacada: Boolean = false, contenido: @Composable ColumnScope.() -> Unit) {
    val colores = MaterialTheme.colorScheme
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = if (destacada) MaterialTheme.shapes.extraLarge else MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = if (destacada) colores.primaryContainer else colores.surfaceContainerLow)
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(titulo, Modifier.semantics { heading() },
                color = if (destacada) colores.onPrimaryContainer else colores.primary,
                style = MaterialTheme.typography.titleMediumEmphasized)
            contenido()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun PantallaAvisos(vm: AvisosViewModel) {
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
            ContextCompat.registerReceiver(contexto, receptor, IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED),
                ContextCompat.RECEIVER_NOT_EXPORTED)
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
    val barra = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    Scaffold(modifier = Modifier.nestedScroll(barra.nestedScrollConnection), topBar = {
        MediumFlexibleTopAppBar(
            title = { Text("AvisosCampus") },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                scrolledContainerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary
            ), scrollBehavior = barra
        )
    }) { margen ->
        BoxWithConstraints(Modifier.fillMaxSize().padding(margen), contentAlignment = Alignment.TopCenter) {
            val ancho = maxWidth.coerceAtMost(1200.dp)
            val doble = ancho >= 840.dp
            val columna = if (doble) (ancho - 48.dp) / 2 else ancho - 32.dp
            FlowRow(
                Modifier.width(ancho).verticalScroll(rememberScrollState()).padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Column(Modifier.width(columna), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Tarjeta("Permiso de notificaciones") {
                        FlowRow(horizontalArrangement = Arrangement.spacedBy(12.dp),
                            itemVerticalAlignment = Alignment.CenterVertically) {
                            EtiquetaPermiso(permiso)
                            FilledTonalButton(shapes = ButtonDefaults.shapes(), onClick = {
                                if (Build.VERSION.SDK_INT >= 33 && !Notificador.tienePermiso(contexto))
                                    lanzador.launch(Manifest.permission.POST_NOTIFICATIONS)
                                else vm.permisoRespondido(true)
                            }) { Text("Permitir notificaciones") }
                        }
                        MensajePermiso(mensaje) {
                            contexto.startActivity(Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                                .putExtra(Settings.EXTRA_APP_PACKAGE, contexto.packageName))
                        }
                    }
                    Tarjeta("Avisos", destacada = true) {
                        AccionAviso(vm::avisarAhora)
                    }
                    Tarjeta("Recordatorio") {
                        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            FilledTonalButton(onClick = { vm.recordarEn() }, shapes = ButtonDefaults.shapes(),
                                enabled = faltan == null) { Text("Recordar en 10 s") }
                            OutlinedButton(onClick = vm::cancelarRecordatorio, shapes = ButtonDefaults.shapes(),
                                enabled = faltan != null) { Text("Cancelar") }
                        }
                        CuentaRegresiva(faltan)
                    }
                    Tarjeta("Receptor de anuncios") {
                        OpcionEscucha("Escuchar el modo avión", escuchando, vm::cambiarEscucha)
                        OpcionEscucha("Escuchar el cargador", cargador, vm::cambiarEscuchaCargador)
                    }
                }
                Box(Modifier.width(columna)) { HistorialAvisos(avisos) }
            }
        }
    }
}

@Composable
private fun OpcionEscucha(texto: String, activo: Boolean, cambiar: (Boolean) -> Unit) {
    val colores = MaterialTheme.colorScheme
    val fondo by animateColorAsState(if (activo) colores.secondaryContainer else colores.surfaceContainer,
        animationSpec = MaterialTheme.motionScheme.fastEffectsSpec(), label = "Fondo de escucha")
    Surface(color = fondo, shape = MaterialTheme.shapes.medium) {
        Row(Modifier.fillMaxWidth().toggleable(activo, role = Role.Switch, onValueChange = cambiar)
            .heightIn(min = 56.dp).padding(horizontal = 12.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(texto, Modifier.weight(1f), style = if (activo) MaterialTheme.typography.bodyLargeEmphasized
                else MaterialTheme.typography.bodyLarge)
            Switch(checked = activo, onCheckedChange = null)
        }
    }
}
