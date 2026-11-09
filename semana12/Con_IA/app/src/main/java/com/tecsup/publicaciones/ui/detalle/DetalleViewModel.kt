package com.tecsup.publicaciones.ui.detalle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.publicaciones.data.repository.ItemRepository
import com.tecsup.publicaciones.domain.model.Item
import com.tecsup.publicaciones.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class DetalleViewModel(
    private val repo: ItemRepository,
    private val itemId: Int
) : ViewModel() {

    private val _estado = MutableStateFlow<UiState<Item>>(UiState.Cargando)
    val estado: StateFlow<UiState<Item>> = _estado.asStateFlow()

    init {
        cargarDetalle()
    }

    fun cargarDetalle() {
        viewModelScope.launch {
            _estado.value = UiState.Cargando
            repo.obtenerDetalle(itemId)
                .onSuccess { item ->
                    _estado.value = UiState.Exito(item)
                }
                .onFailure { e ->
                    _estado.value = UiState.Error(mensajeDe(e))
                }
        }
    }

    fun alternarFavorito() {
        viewModelScope.launch {
            repo.alternarFavorito(itemId)
            val estadoActual = _estado.value
            if (estadoActual is UiState.Exito) {
                val itemActual = estadoActual.datos
                _estado.value = UiState.Exito(itemActual.copy(esFavorito = !itemActual.esFavorito))
            }
        }
    }

    private fun mensajeDe(e: Throwable) = when (e) {
        is IOException -> "No hay conexión a internet para cargar el detalle"
        is HttpException -> "El servidor respondió con error ${e.code()}"
        else -> "Ocurrió un error inesperado"
    }
}
