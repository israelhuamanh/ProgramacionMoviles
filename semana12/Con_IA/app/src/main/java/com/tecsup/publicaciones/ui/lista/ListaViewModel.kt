package com.tecsup.publicaciones.ui.lista

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.publicaciones.data.repository.ItemRepository
import com.tecsup.publicaciones.domain.model.Item
import com.tecsup.publicaciones.notification.Notificador
import com.tecsup.publicaciones.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class ListaViewModel(
    private val repo: ItemRepository,
    private val notificador: Notificador
) : ViewModel() {

    private val _estado = MutableStateFlow<UiState<List<Item>>>(UiState.Cargando)
    val estado: StateFlow<UiState<List<Item>>> = _estado.asStateFlow()

    private val _aviso = MutableStateFlow<String?>(null)
    val aviso: StateFlow<String?> = _aviso.asStateFlow()

    private val _busqueda = MutableStateFlow("")
    val busqueda: StateFlow<String> = _busqueda.asStateFlow()

    private val _totalFavoritos = MutableStateFlow(0)
    val totalFavoritos: StateFlow<Int> = _totalFavoritos.asStateFlow()

    private var refrescoTerminado = false

    init {
        viewModelScope.launch {
            combine(repo.items, _busqueda) { lista, query ->
                val filtro = query.trim().lowercase()
                val filtrada = if (filtro.isEmpty()) {
                    lista
                } else {
                    lista.filter {
                        it.titulo.lowercase().contains(filtro) ||
                            it.subtitulo.lowercase().contains(filtro)
                    }
                }
                Pair(lista, filtrada)
            }.collect { (listaOriginal, listaFiltrada) ->
                if (listaOriginal.isNotEmpty() || refrescoTerminado) {
                    _estado.value = UiState.Exito(listaFiltrada)
                }
            }
        }
        viewModelScope.launch {
            repo.favoritos.collect { favs ->
                _totalFavoritos.value = favs.size
            }
        }
        refrescar()
    }

    fun actualizarBusqueda(nuevaBusqueda: String) {
        _busqueda.value = nuevaBusqueda
    }

    fun refrescar() {
        viewModelScope.launch {
            val resultado = repo.refrescar()
            refrescoTerminado = true
            resultado
                .onSuccess { n ->
                    if (_estado.value is UiState.Cargando) _estado.value = UiState.Exito(emptyList())
                    _aviso.value = null
                    notificador.mostrar("Datos actualizados", "Se guardaron $n elementos")
                }
                .onFailure { e ->
                    if (repo.cantidadGuardada() == 0) _estado.value = UiState.Error(mensajeDe(e))
                    else _aviso.value = "Sin conexión: mostrando datos guardados"
                }
        }
    }

    fun alternarFavorito(id: Int) = viewModelScope.launch { repo.alternarFavorito(id) }

    private fun mensajeDe(e: Throwable) = when (e) {
        is IOException -> "No hay conexión a internet"
        is HttpException -> "El servidor respondió con error ${e.code()}"
        else -> "Ocurrió un error inesperado"
    }
}
