package com.tecsup.cafecampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.cafecampus.data.local.entity.Producto
import com.tecsup.cafecampus.data.repository.CafeRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class MenuViewModel(private val repo: CafeRepository) : ViewModel() {
    val productos: StateFlow<List<Producto>> = repo.observarProductos()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // TODO(Paso 7): carrito: agregar, quitar, total, vaciar
}
