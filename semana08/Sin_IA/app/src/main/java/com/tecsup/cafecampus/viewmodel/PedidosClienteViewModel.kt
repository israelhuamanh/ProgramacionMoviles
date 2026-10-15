package com.tecsup.cafecampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.cafecampus.data.local.relation.PedidoConDetalle
import com.tecsup.cafecampus.data.repository.CafeRepository
import com.tecsup.cafecampus.model.ItemCarrito
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class PedidosClienteViewModel(private val repo: CafeRepository) : ViewModel() {

    fun confirmarPedido(
        clienteId: Int,
        items: List<ItemCarrito>,
        horaRecojo: String,
        pago: String,
        nota: String,
        onExito: (Long) -> Unit
    ) {
        viewModelScope.launch {
            val pedidoId = repo.crearPedido(
                clienteId = clienteId,
                items = items,
                horaRecojo = horaRecojo,
                pago = pago,
                nota = nota
            )
            onExito(pedidoId)
        }
    }

    fun observarMisPedidos(clienteId: Int): Flow<List<PedidoConDetalle>> {
        return repo.observarPedidosCliente(clienteId)
    }

    fun observarPedido(id: Int): Flow<PedidoConDetalle?> {
        return repo.observarPedido(id)
    }
}
