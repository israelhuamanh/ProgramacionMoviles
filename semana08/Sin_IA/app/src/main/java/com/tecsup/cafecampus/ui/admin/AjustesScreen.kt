package com.tecsup.cafecampus.ui.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.cafecampus.model.Rol
import com.tecsup.cafecampus.ui.components.AdminBottomBar
import com.tecsup.cafecampus.ui.components.CafeButton
import com.tecsup.cafecampus.ui.components.ClienteBottomBar
import com.tecsup.cafecampus.ui.theme.CafeTheme
import com.tecsup.cafecampus.viewmodel.SesionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AjustesScreen(
    sesionVm: SesionViewModel,
    onCambiarPerfil: (Rol) -> Unit,
    onCerrarSesion: () -> Unit,
    onNavigate: (String) -> Unit
) {
    val modoOscuro by sesionVm.modoOscuro.collectAsState()
    val recibirPedidos by sesionVm.recibirPedidos.collectAsState()
    val perfilGuardado by sesionVm.perfilGuardado.collectAsState()

    val rolActual = if (perfilGuardado == Rol.ADMIN.name) Rol.ADMIN else Rol.CLIENTE

    CafeTheme(rol = rolActual, modoOscuro = modoOscuro) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Ajustes del Sistema") }
                )
            },
            bottomBar = {
                if (rolActual == Rol.ADMIN) {
                    AdminBottomBar(rutaActual = "ajustes", onNavigate = onNavigate)
                } else {
                    ClienteBottomBar(rutaActual = "ajustes", onNavigate = onNavigate)
                }
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Preferencias de la App",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text("Modo Oscuro", fontWeight = FontWeight.SemiBold)
                                    Text("Cambia el tema de toda la app", style = MaterialTheme.typography.bodySmall)
                                }
                                Switch(
                                    checked = modoOscuro,
                                    onCheckedChange = { sesionVm.guardarModoOscuro(it) }
                                )
                            }

                            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text("Recibir Pedidos", fontWeight = FontWeight.SemiBold)
                                    Text("Habilita o deshabilita compras de clientes", style = MaterialTheme.typography.bodySmall)
                                }
                                Switch(
                                    checked = recibirPedidos,
                                    onCheckedChange = { sesionVm.guardarRecibirPedidos(it) }
                                )
                            }
                        }
                    }

                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Modo Práctica - Perfil Activo",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Perfil actual: ${if (rolActual == Rol.ADMIN) "Administrador" else "Estudiante"}")

                            Spacer(modifier = Modifier.height(12.dp))

                            val nuevoRol = if (rolActual == Rol.ADMIN) Rol.CLIENTE else Rol.ADMIN
                            val textoBotonCambio = if (rolActual == Rol.ADMIN) "Cambiar a Perfil Estudiante" else "Cambiar a Perfil Administrador"

                            CafeButton(
                                texto = textoBotonCambio,
                                onClick = {
                                    sesionVm.cambiarPerfil(nuevoRol) {
                                        onCambiarPerfil(nuevoRol)
                                    }
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }

                OutlinedButton(
                    onClick = {
                        sesionVm.cerrarSesion {
                            onCerrarSesion()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cerrar Sesión", color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}
