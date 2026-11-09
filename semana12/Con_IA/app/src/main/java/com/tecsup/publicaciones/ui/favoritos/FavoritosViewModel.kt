package com.tecsup.publicaciones.ui.favoritos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.publicaciones.data.repository.ItemRepository
import com.tecsup.publicaciones.domain.model.Item
import com.tecsup.publicaciones.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoritosViewModel(
    private val repo: ItemRepository
) : ViewModel() {

    private val _estado = MutableStateFlow<UiState<List<Item>>>(UiState.Cargando)
    val estado: StateFlow<UiState<List<Item>>> = _estado.asStateFlow()

    init {
        viewModelScope.launch {
            repo.favoritos.collect { lista ->
                _estado.value = UiState.Exito(lista)
            }
        }
    }

    fun alternarFavorito(id: Int) {
        viewModelScope.launch {
            repo.alternarFavorito(id)
        }
    }
}
