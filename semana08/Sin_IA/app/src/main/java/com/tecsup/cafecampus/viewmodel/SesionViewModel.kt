package com.tecsup.cafecampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.cafecampus.data.local.entity.Usuario
import com.tecsup.cafecampus.data.prefs.PreferenciasStore
import com.tecsup.cafecampus.data.repository.CafeRepository
import com.tecsup.cafecampus.model.Rol
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SesionViewModel(
    private val repo: CafeRepository,
    private val prefsStore: PreferenciasStore
) : ViewModel() {

    private val _usuarioActual = MutableStateFlow<Usuario?>(null)
    val usuarioActual: StateFlow<Usuario?> = _usuarioActual.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    val perfilGuardado: StateFlow<String> = prefsStore.perfilActivo
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

    val modoOscuro: StateFlow<Boolean> = prefsStore.modoOscuro
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val recibirPedidos: StateFlow<Boolean> = prefsStore.recibirPedidos
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    fun login(correo: String, clave: String, rolSeleccionado: Rol, onExito: (Rol) -> Unit) {
        if (correo.isBlank() || clave.isBlank()) {
            _error.value = "Por favor ingrese correo y contraseña"
            return
        }

        viewModelScope.launch {
            val usuario = repo.login(correo.trim(), clave.trim())
            if (usuario == null) {
                _error.value = "Correo o contraseña incorrectos"
            } else if (usuario.rol != rolSeleccionado.name) {
                val rolTexto = if (rolSeleccionado == Rol.CLIENTE) "Estudiante" else "Administrador"
                _error.value = "El usuario no corresponde al rol de $rolTexto"
            } else {
                _error.value = null
                _usuarioActual.value = usuario
                prefsStore.guardarPerfil(usuario.rol)
                onExito(rolSeleccionado)
            }
        }
    }

    fun cerrarSesion(onExito: () -> Unit) {
        _usuarioActual.value = null
        viewModelScope.launch {
            prefsStore.borrarPerfil()
            onExito()
        }
    }

    fun cambiarPerfil(nuevoRol: Rol, onExito: () -> Unit) {
        viewModelScope.launch {
            prefsStore.guardarPerfil(nuevoRol.name)
            onExito()
        }
    }

    fun guardarModoOscuro(activo: Boolean) {
        viewModelScope.launch {
            prefsStore.guardarModoOscuro(activo)
        }
    }

    fun guardarRecibirPedidos(activo: Boolean) {
        viewModelScope.launch {
            prefsStore.guardarRecibirPedidos(activo)
        }
    }

    fun limpiarError() {
        _error.value = null
    }
}
