package com.tecsup.mibodega.ui.cliente.screens.entrega

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.LocalAtm
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.componentes.BotonPrimario
import com.tecsup.mibodega.ui.componentes.CampoTexto
import com.tecsup.mibodega.ui.theme.VerdeBodega

@Composable
fun DatosEntregaScreen(
    esRecojoEnTienda: Boolean,
    onVolver: () -> Unit,
    onConfirmarPedido: (nombre: String, telefono: String, direccion: String, referencia: String, metodoPago: String) -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var referencia by remember { mutableStateOf("") }
    var metodoPagoSeleccionado by remember { mutableStateOf("Efectivo al entregar") }

    var intentarAvanzar by remember { mutableStateOf(false) }

    val errorNombre = intentarAvanzar && nombre.isBlank()
    val errorTelefono = intentarAvanzar && (telefono.isBlank() || telefono.length < 9)
    val errorDireccion = intentarAvanzar && !esRecojoEnTienda && direccion.isBlank()
    val errorReferencia = intentarAvanzar && !esRecojoEnTienda && referencia.isBlank()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
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
                text = "Datos de entrega",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Spacer(Modifier.height(16.dp))

        CampoTexto(
            etiqueta = "Nombre",
            valor = nombre,
            onValorCambia = { nombre = it },
            placeholder = "Juan Pérez",
            esError = errorNombre,
            mensajeError = if (errorNombre) "El nombre es obligatorio" else null
        )

        Spacer(Modifier.height(14.dp))

        CampoTexto(
            etiqueta = "Teléfono",
            valor = telefono,
            onValorCambia = { if (it.length <= 9 && it.all { char -> char.isDigit() }) telefono = it },
            placeholder = "987 654 321",
            teclado = KeyboardType.Phone,
            esError = errorTelefono,
            mensajeError = if (errorTelefono) "Ingresa un teléfono válido de 9 dígitos" else null
        )

        if (!esRecojoEnTienda) {
            Spacer(Modifier.height(14.dp))

            CampoTexto(
                etiqueta = "Dirección",
                valor = direccion,
                onValorCambia = { direccion = it },
                placeholder = "Av. Los Olivos 123",
                esError = errorDireccion,
                mensajeError = if (errorDireccion) "La dirección es obligatoria" else null
            )

            Spacer(Modifier.height(14.dp))

            CampoTexto(
                etiqueta = "Referencia",
                valor = referencia,
                onValorCambia = { referencia = it },
                placeholder = "Frente al parque",
                esError = errorReferencia,
                mensajeError = if (errorReferencia) "La referencia es obligatoria" else null
            )
        } else {
            Spacer(Modifier.height(14.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Text(
                    text = "Punto de recojo: Bodega Central (Av. Universitaria 456). Tu pedido estará listo en 20 minutos.",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(14.dp)
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        Text(
            text = "Método de pago",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(12.dp))

        val opcionesPago = listOf(
            Triple("Efectivo al entregar", Icons.Default.LocalAtm, Color(0xFF2E7D32)),
            Triple("Yape", Icons.Default.QrCode, Color(0xFF7B1FA2)),
            Triple("Plin", Icons.Default.CreditCard, Color(0xFF0288D1))
        )

        opcionesPago.forEach { (metodo, icono, colorIcono) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .selectable(
                        selected = metodoPagoSeleccionado == metodo,
                        onClick = { metodoPagoSeleccionado = metodo },
                        role = Role.RadioButton
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = metodoPagoSeleccionado == metodo,
                    onClick = null
                )
                Spacer(Modifier.width(10.dp))
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .background(colorIcono.copy(alpha = 0.15f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icono,
                        contentDescription = null,
                        tint = colorIcono,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(Modifier.width(12.dp))
                Text(
                    text = metodo,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = if (metodoPagoSeleccionado == metodo) FontWeight.SemiBold else FontWeight.Normal
                )
            }
        }

        Spacer(Modifier.height(32.dp))

        BotonPrimario(
            texto = "Confirmar pedido",
            onClick = {
                intentarAvanzar = true
                val direccionValida = esRecojoEnTienda || direccion.isNotBlank()
                val referenciaValida = esRecojoEnTienda || referencia.isNotBlank()
                if (nombre.isNotBlank() && telefono.length == 9 && direccionValida && referenciaValida) {
                    val dirFinal = if (esRecojoEnTienda) "Recojo en Tienda Central" else direccion.trim()
                    val refFinal = if (esRecojoEnTienda) "Mostrador Principal" else referencia.trim()
                    onConfirmarPedido(nombre.trim(), telefono.trim(), dirFinal, refFinal, metodoPagoSeleccionado)
                }
            }
        )

        Spacer(Modifier.height(24.dp))
    }
}
