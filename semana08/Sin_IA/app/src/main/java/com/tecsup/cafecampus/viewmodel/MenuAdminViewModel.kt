package com.tecsup.cafecampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.cafecampus.data.local.entity.Producto
import com.tecsup.cafecampus.data.repository.CafeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MenuAdminViewModel(private val repo: CafeRepository) : ViewModel() {

    val productos: StateFlow<List<Producto>> = repo.observarProductos()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun obtenerProducto(id: Int): Flow<Producto?> {
        return repo.observarProductos().map { lista ->
            lista.find { it.id == id }
        }
    }

    fun guardarProducto(p: Producto) {
        viewModelScope.launch {
            repo.guardarProducto(p)
        }
    }

    fun eliminarProducto(p: Producto) {
        viewModelScope.launch {
            repo.eliminarProducto(p)
        }
    }

    fun alternarDisponibilidad(id: Int, disponibleActual: Boolean) {
        viewModelScope.launch {
            repo.cambiarDisponibilidad(id, !disponibleActual)
        }
    }
}
