package com.tecsup.avisoscampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.avisoscampus.data.AvisosRepository
import com.tecsup.avisoscampus.model.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
class AvisosViewModel(private val repo: AvisosRepository) : ViewModel() {
    val avisos = repo.avisos
    private val _eventos = MutableSharedFlow<Aviso>()
    val eventos = _eventos.asSharedFlow()
    private val _permiso = MutableStateFlow<PermisoUi>(PermisoUi.SinPedir)
    val permiso = _permiso.asStateFlow()
    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje = _mensaje.asStateFlow()

    fun permisoRespondido(concedido: Boolean) {
        if (concedido) _mensaje.value = null
        _permiso.value = if (concedido) PermisoUi.Concedido else PermisoUi.Denegado
    }

    private suspend fun emitir(titulo: String, texto: String, origen: Origen) {
        _eventos.emit(repo.registrar(titulo, texto, origen))
    }
    fun avisarAhora() {
        viewModelScope.launch { emitir("Tarea lista", "Tu entrega fue registrada", Origen.BOTON) }
    }

    private var trabajo: Job? = null
    private val _faltan = MutableStateFlow<Int?>(null)
    val faltan = _faltan.asStateFlow()

    fun recordarEn(segundos: Int = 10) {
        if (trabajo?.isCompleted == false) return
        trabajo = viewModelScope.launch {
            try {
                for (restantes in segundos downTo 1) {
                    _faltan.value = restantes
                    delay(1000)
                }
                emitir("Recordatorio", "Revisa tu laboratorio pendiente", Origen.RECORDATORIO)
            } finally {
                _faltan.value = null
            }
        }
    }
    fun cancelarRecordatorio() { trabajo?.cancel() }

    private val _escuchandoAvion = MutableStateFlow(false)
    val escuchandoAvion = _escuchandoAvion.asStateFlow()
    fun cambiarEscucha(valor: Boolean) { _escuchandoAvion.value = valor }
    fun alCambiarModoAvion(activo: Boolean) {
        viewModelScope.launch {
            emitir("Modo avión", if (activo) "Activado" else "Desactivado", Origen.MODO_AVION)
        }
    }

    fun noSePudoMostrar() {
        _mensaje.value = "Sin permiso: el aviso se guardó en el historial, pero no se mostró"
    }

    private val _escuchandoCargador = MutableStateFlow(false)
    val escuchandoCargador = _escuchandoCargador.asStateFlow()
    fun cambiarEscuchaCargador(valor: Boolean) { _escuchandoCargador.value = valor }
    fun alCambiarCargador(conectado: Boolean) {
        viewModelScope.launch {
            emitir("Cargador", if (conectado) "Conectado" else "Desconectado", Origen.CARGADOR)
        }
    }
}
