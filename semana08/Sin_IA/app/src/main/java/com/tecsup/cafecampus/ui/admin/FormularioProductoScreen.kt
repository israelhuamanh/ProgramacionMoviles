package com.tecsup.cafecampus.ui.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.cafecampus.data.local.entity.Producto
import com.tecsup.cafecampus.model.Rol
import com.tecsup.cafecampus.ui.components.CafeButton
import com.tecsup.cafecampus.ui.components.CafeTextField
import com.tecsup.cafecampus.ui.theme.CafeTheme
import com.tecsup.cafecampus.viewmodel.MenuAdminViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioProductoScreen(
    productoId: Int? = null,
    menuAdminVm: MenuAdminViewModel,
    onVolver: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("Bebidas") }
    var precioText by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var icono by remember { mutableStateOf("cafe") }
    var disponible by remember { mutableStateOf(true) }

    var errorMensaje by remember { mutableStateOf<String?>(null) }

    val productoExistente by if (productoId != null) {
        menuAdminVm.obtenerProducto(productoId).collectAsState(initial = null)
    } else {
        remember { mutableStateOf<Producto?>(null) }
    }

    LaunchedEffect(productoExistente) {
        val p = productoExistente
        if (p != null) {
            nombre = p.nombre
            categoria = p.categoria
            precioText = p.precio.toString()
            descripcion = p.descripcion
            icono = p.icono
            disponible = p.disponible
        }
    }

    CafeTheme(rol = Rol.ADMIN) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(if (productoId == null) "Nuevo Producto" else "Editar Producto") },
                    navigationIcon = {
                        IconButton(onClick = onVolver) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                        }
                    }
                )
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
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CafeTextField(
                        valor = nombre,
                        onValueChange = { nombre = it; errorMensaje = null },
                        etiqueta = "Nombre del producto",
                        modifier = Modifier.fillMaxWidth()
                    )

                    CafeTextField(
                        valor = categoria,
                        onValueChange = { categoria = it; errorMensaje = null },
                        etiqueta = "Categoría (ej. Bebidas, Sandwiches, Platos, Snacks)",
                        modifier = Modifier.fillMaxWidth()
                    )

                    CafeTextField(
                        valor = precioText,
                        onValueChange = { precioText = it; errorMensaje = null },
                        etiqueta = "Precio (S/)",
                        modifier = Modifier.fillMaxWidth()
                    )

                    CafeTextField(
                        valor = descripcion,
                        onValueChange = { descripcion = it },
                        etiqueta = "Descripción del producto",
                        modifier = Modifier.fillMaxWidth()
                    )

                    CafeTextField(
                        valor = icono,
                        onValueChange = { icono = it },
                        etiqueta = "Ícono (cafe, plato, jugo, pan, botella)",
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Disponible en menú",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                        Switch(
                            checked = disponible,
                            onCheckedChange = { disponible = it }
                        )
                    }

                    if (errorMensaje != null) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = errorMensaje!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                CafeButton(
                    texto = "Guardar Producto",
                    onClick = {
                        val precioNum = precioText.toDoubleOrNull()
                        if (nombre.isBlank()) {
                            errorMensaje = "El nombre del producto es obligatorio"
                        } else if (precioNum == null || precioNum <= 0) {
                            errorMensaje = "Ingrese un precio válido mayor a 0"
                        } else {
                            val p = Producto(
                                id = productoId ?: 0,
                                nombre = nombre.trim(),
                                descripcion = descripcion.trim(),
                                categoria = categoria.trim(),
                                precio = precioNum,
                                disponible = disponible,
                                icono = icono.trim().lowercase().ifBlank { "cafe" }
                            )
                            menuAdminVm.guardarProducto(p)
                            onVolver()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
