package com.tecsup.publicaciones.ui.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.publicaciones.data.repository.ItemRepository
import com.tecsup.publicaciones.notification.Notificador
import com.tecsup.publicaciones.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ResumenAdmin(
    val totalGuardado: Int = 0,
    val totalFavoritos: Int = 0,
    val mensajeAccion: String? = null
)

class AdminViewModel(
    private val repo: ItemRepository,
    private val notificador: Notificador
) : ViewModel() {

    private val _estado = MutableStateFlow<UiState<ResumenAdmin>>(UiState.Cargando)
    val estado: StateFlow<UiState<ResumenAdmin>> = _estado.asStateFlow()

    init {
        cargarTotales()
    }

    fun cargarTotales(mensaje: String? = null) {
        viewModelScope.launch {
            val totalGuardado = repo.cantidadGuardada()
            val totalFavoritos = repo.cantidadFavoritos()
            _estado.value = UiState.Exito(
                ResumenAdmin(
                    totalGuardado = totalGuardado,
                    totalFavoritos = totalFavoritos,
                    mensajeAccion = mensaje
                )
            )
        }
    }

    fun sincronizar() {
        viewModelScope.launch {
            _estado.value = UiState.Cargando
            repo.refrescar()
                .onSuccess { n ->
                    notificador.mostrar("Datos actualizados", "Se guardaron $n elementos")
                    cargarTotales("Sincronización exitosa: se guardaron $n elementos")
                }
                .onFailure {
                    cargarTotales("No se pudo sincronizar: verifica tu conexión a internet")
                }
        }
    }

    fun limpiarCache() {
        viewModelScope.launch {
            repo.limpiarCache()
            cargarTotales("Caché local limpiada correctamente")
        }
    }
}
