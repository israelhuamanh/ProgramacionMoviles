package com.tecsup.cafecampus.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PedidoCard(
    idPedido: Int,
    estado: String,
    total: Double
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Pedido #$idPedido")
            EstadoChip(estado)
            Text("Total: S/ %.2f".format(total))
        }
    }
}
