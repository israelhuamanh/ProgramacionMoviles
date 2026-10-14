package com.tecsup.cafecampus.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.tecsup.cafecampus.data.local.entity.DetallePedido
import com.tecsup.cafecampus.data.local.entity.Pedido

data class PedidoConDetalle(
    @Embedded val pedido: Pedido,
    @Relation(
        parentColumn = "id",
        entityColumn = "pedidoId"
    )
    val detalles: List<DetallePedido>
)
