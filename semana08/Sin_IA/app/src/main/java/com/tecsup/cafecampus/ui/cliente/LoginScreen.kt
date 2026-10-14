package com.tecsup.cafecampus.ui.cliente

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.tecsup.cafecampus.model.Rol
import com.tecsup.cafecampus.ui.components.CafeButton
import com.tecsup.cafecampus.ui.components.CafeTextField
import com.tecsup.cafecampus.ui.theme.CafeTheme
import com.tecsup.cafecampus.viewmodel.SesionViewModel

@Composable
fun LoginScreen(
    vm: SesionViewModel,
    onLoginExito: (Rol) -> Unit
) {
    var rolSeleccionado by remember { mutableStateOf(Rol.CLIENTE) }
    var correo by remember { mutableStateOf("juan@campus.test") }
    var clave by remember { mutableStateOf("1234") }

    val error by vm.error.collectAsState()

    CafeTheme(rol = rolSeleccionado) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "CafeCampus",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Inicia sesión para continuar",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FilterChip(
                    selected = rolSeleccionado == Rol.CLIENTE,
                    onClick = {
                        rolSeleccionado = Rol.CLIENTE
                        correo = "juan@campus.test"
                        clave = "1234"
                        vm.limpiarError()
                    },
                    label = { Text("Estudiante") }
                )
                FilterChip(
                    selected = rolSeleccionado == Rol.ADMIN,
                    onClick = {
                        rolSeleccionado = Rol.ADMIN
                        correo = "admin@cafe.test"
                        clave = "admin"
                        vm.limpiarError()
                    },
                    label = { Text("Administrador") }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            CafeTextField(
                valor = correo,
                onValueChange = { correo = it; vm.limpiarError() },
                etiqueta = "Correo institucional",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            CafeTextField(
                valor = clave,
                onValueChange = { clave = it; vm.limpiarError() },
                etiqueta = "Contraseña",
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )

            if (error != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = error!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            CafeButton(
                texto = "Ingresar",
                onClick = {
                    vm.login(correo, clave, rolSeleccionado, onLoginExito)
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = {
                if (rolSeleccionado == Rol.CLIENTE) {
                    correo = "juan@campus.test"
                    clave = "1234"
                } else {
                    correo = "admin@cafe.test"
                    clave = "admin"
                }
            }) {
                Text("Cargar credenciales de prueba")
            }
        }
    }
}
