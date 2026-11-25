package com.tecsup.avisoscampus.ui

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color
import com.tecsup.avisoscampus.model.PermisoUi
import com.tecsup.avisoscampus.notificacion.Notificador
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.avisoscampus.viewmodel.AvisosViewModel

@Composable
internal fun Tarjeta(titulo: String, contenido: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)
    ) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(titulo, color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            contenido()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
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
        vm.eventos.collect { aviso -> Notificador.mostrar(contexto, aviso) }
    }
    val faltan by vm.faltan.collectAsState()
    Scaffold(topBar = {
        TopAppBar(title = { Text("AvisosCampus", fontWeight = FontWeight.Bold) },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary))
    }) { margen ->
        Box(Modifier.fillMaxSize().padding(margen), contentAlignment = Alignment.TopCenter) {
            Column(Modifier.widthIn(max = 640.dp).fillMaxWidth().verticalScroll(rememberScrollState()).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Tarjeta("Permiso de notificaciones") {
                    val (etiqueta, fondo, texto) = when (permiso) {
                        PermisoUi.Concedido -> Triple("Permitido", Color(0xFFBCEFD3), Color(0xFF064527))
                        PermisoUi.Denegado -> Triple("Denegado", MaterialTheme.colorScheme.errorContainer, MaterialTheme.colorScheme.onErrorContainer)
                        PermisoUi.SinPedir -> Triple("Sin pedir", MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Surface(shape = RoundedCornerShape(50), color = fondo, contentColor = texto) {
                        Text(etiqueta, Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelLarge)
                    }
                    FilledTonalButton(onClick = {
                        if (Build.VERSION.SDK_INT >= 33 && !Notificador.tienePermiso(contexto))
                            lanzador.launch(Manifest.permission.POST_NOTIFICATIONS)
                        else vm.permisoRespondido(true)
                    }) { Text("Permitir notificaciones") }
                }
                Tarjeta("Avisos") {
                    Button(onClick = vm::avisarAhora) { Text("Mostrar aviso") }
                }
                Tarjeta("Recordatorio") {
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FilledTonalButton(onClick = { vm.recordarEn() }, enabled = faltan == null) {
                            Text("Recordar en 10 s")
                        }
                        OutlinedButton(onClick = vm::cancelarRecordatorio, enabled = faltan != null) {
                            Text("Cancelar")
                        }
                    }
                    faltan?.let { Text("Faltan $it s", style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary) }
                }
                Tarjeta("Receptor de anuncios") {}
                Tarjeta("Historial") {}
            }
        }
    }
}
