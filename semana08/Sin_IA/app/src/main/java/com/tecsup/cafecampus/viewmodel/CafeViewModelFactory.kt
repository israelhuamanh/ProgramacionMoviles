package com.tecsup.cafecampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tecsup.cafecampus.data.prefs.PreferenciasStore
import com.tecsup.cafecampus.data.repository.CafeRepository

class CafeViewModelFactory(
    private val repository: CafeRepository,
    private val prefsStore: PreferenciasStore
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MenuViewModel::class.java)) {
            return MenuViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(SesionViewModel::class.java)) {
            return SesionViewModel(repository, prefsStore) as T
        }
        if (modelClass.isAssignableFrom(PedidosClienteViewModel::class.java)) {
            return PedidosClienteViewModel(repository) as T
        }
        throw IllegalArgumentException("ViewModel no reconocido: ${modelClass.name}")
    }
}
