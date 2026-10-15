package com.tecsup.cafecampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.cafecampus.data.local.relation.PedidoConDetalle
import com.tecsup.cafecampus.data.repository.CafeRepository
import com.tecsup.cafecampus.model.EstadoPedido
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class PedidosAdminViewModel(private val repo: CafeRepository) : ViewModel() {

    private val _estadoSeleccionado = MutableStateFlow(EstadoPedido.RECIBIDO)
    val estadoSeleccionado: StateFlow<EstadoPedido> = _estadoSeleccionado.asStateFlow()

    val pedidos: StateFlow<List<PedidoConDetalle>> = _estadoSeleccionado
        .flatMapLatest { estado -> repo.observarPedidosPorEstado(estado) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun cambiarFiltro(estado: EstadoPedido) {
        _estadoSeleccionado.value = estado
    }

    fun avanzar(pedidoId: Int, estadoActual: EstadoPedido) {
        val siguiente = when (estadoActual) {
            EstadoPedido.RECIBIDO -> EstadoPedido.PREPARANDO
            EstadoPedido.PREPARANDO -> EstadoPedido.LISTO
            EstadoPedido.LISTO -> EstadoPedido.ENTREGADO
            else -> null
        }
        if (siguiente != null) {
            viewModelScope.launch {
                repo.cambiarEstado(pedidoId, siguiente)
            }
        }
    }

    fun cancelar(pedidoId: Int) {
        viewModelScope.launch {
            repo.cambiarEstado(pedidoId, EstadoPedido.CANCELADO)
        }
    }
}
