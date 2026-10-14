package com.tecsup.cafecampus.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.cafecampus.data.local.entity.Producto

@Composable
fun ProductoCard(
    producto: Producto,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            ProductoIcono(producto.icono)
            Text(text = producto.nombre, style = MaterialTheme.typography.titleMedium)
            Text(text = "S/ %.2f".format(producto.precio), style = MaterialTheme.typography.bodyMedium)
        }
    }
}
