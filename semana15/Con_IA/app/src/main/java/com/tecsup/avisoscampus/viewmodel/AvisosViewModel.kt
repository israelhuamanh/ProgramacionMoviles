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
        _permiso.value = if (concedido) PermisoUi.Concedido else PermisoUi.Denegado
    }

    private suspend fun emitir(titulo: String, texto: String, origen: Origen) {
        _eventos.emit(repo.registrar(titulo, texto, origen))
    }
    fun avisarAhora() {
        viewModelScope.launch { emitir("Tarea lista", "Tu entrega fue registrada", Origen.BOTON) }
    }
}
