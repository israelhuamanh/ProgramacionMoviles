package com.tecsup.mibodega.ui.cliente.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.componentes.BotonPrimario
import com.tecsup.mibodega.ui.componentes.CampoTexto
import com.tecsup.mibodega.ui.theme.GrisClaro
import com.tecsup.mibodega.ui.theme.VerdeBodega

@Composable
fun LoginScreen(
    onVolver: () -> Unit,
    onLoginExitoso: () -> Unit
) {
    var usuario by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMensaje by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(horizontal = 24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onVolver) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
            }
            Text(
                text = "Iniciar sesión",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Spacer(Modifier.height(20.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "Login",
                tint = VerdeBodega,
                modifier = Modifier
                    .size(72.dp)
                    .background(GrisClaro, CircleShape)
                    .padding(16.dp)
            )
        }

        Spacer(Modifier.height(24.dp))

        CampoTexto(
            etiqueta = "Usuario / Correo",
            valor = usuario,
            onValorCambia = {
                usuario = it
                errorMensaje = null
            },
            placeholder = "admin@bodega.com",
            esError = errorMensaje != null
        )

        Spacer(Modifier.height(16.dp))

        CampoTexto(
            etiqueta = "Contraseña",
            valor = password,
            onValorCambia = {
                password = it
                errorMensaje = null
            },
            placeholder = "••••••",
            teclado = KeyboardType.Password,
            visualTransformation = PasswordVisualTransformation(),
            esError = errorMensaje != null,
            mensajeError = errorMensaje
        )

        if (errorMensaje != null) {
            Spacer(Modifier.height(12.dp))
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = errorMensaje ?: "",
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }

        Spacer(Modifier.weight(1f))

        BotonPrimario(
            texto = "Ingresar",
            onClick = {
                if ((usuario.trim() == "admin@bodega.com" || usuario.trim() == "cliente") && password == "123456") {
                    errorMensaje = null
                    onLoginExitoso()
                } else {
                    errorMensaje = "Credenciales incorrectas. Usa 'admin@bodega.com' o 'cliente' con clave '123456'."
                }
            }
        )

        Spacer(Modifier.height(24.dp))
    }
}
