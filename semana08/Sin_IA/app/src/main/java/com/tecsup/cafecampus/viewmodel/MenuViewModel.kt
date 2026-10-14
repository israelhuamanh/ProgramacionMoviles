package com.tecsup.cafecampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.cafecampus.data.local.entity.Producto
import com.tecsup.cafecampus.data.repository.CafeRepository
import com.tecsup.cafecampus.model.ItemCarrito
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class MenuViewModel(private val repo: CafeRepository) : ViewModel() {

    val productos: StateFlow<List<Producto>> = repo.observarProductos()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _carrito = MutableStateFlow<List<ItemCarrito>>(emptyList())
    val carrito: StateFlow<List<ItemCarrito>> = _carrito.asStateFlow()

    fun obtenerProducto(id: Int): Flow<Producto?> {
        return repo.observarProductos().map { lista ->
            lista.find { it.id == id }
        }
    }

    fun agregarAlCarrito(producto: Producto, cantidad: Int = 1, nota: String = "") {
        val actual = _carrito.value.toMutableList()
        val index = actual.indexOfFirst { it.producto.id == producto.id }
        if (index >= 0) {
            val itemExistente = actual[index]
            actual[index] = itemExistente.copy(
                cantidad = itemExistente.cantidad + cantidad,
                nota = if (nota.isNotBlank()) nota else itemExistente.nota
            )
        } else {
            actual.add(ItemCarrito(producto, cantidad, nota))
        }
        _carrito.value = actual
    }

    fun quitarDelCarrito(productoId: Int) {
        _carrito.value = _carrito.value.filter { it.producto.id != productoId }
    }

    fun disminuirCantidad(productoId: Int) {
        val actual = _carrito.value.toMutableList()
        val index = actual.indexOfFirst { it.producto.id == productoId }
        if (index >= 0) {
            val item = actual[index]
            if (item.cantidad > 1) {
                actual[index] = item.copy(cantidad = item.cantidad - 1)
            } else {
                actual.removeAt(index)
            }
            _carrito.value = actual
        }
    }

    fun vaciarCarrito() {
        _carrito.value = emptyList()
    }

    fun calcularTotal(): Double {
        return _carrito.value.sumOf { it.producto.precio * it.cantidad }
    }
}
