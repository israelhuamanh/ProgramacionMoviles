package com.tecsup.publicaciones.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.publicaciones.data.auth.AuthRepository
import com.tecsup.publicaciones.domain.model.Sesion
import com.tecsup.publicaciones.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _estado = MutableStateFlow<UiState<Sesion?>>(UiState.Cargando)
    val estado: StateFlow<UiState<Sesion?>> = _estado.asStateFlow()

    init {
        verificarSesionActual()
    }

    fun verificarSesionActual() {
        viewModelScope.launch {
            _estado.value = UiState.Cargando
            try {
                val sesion = authRepository.sesionActual()
                _estado.value = UiState.Exito(sesion)
            } catch (e: Exception) {
                _estado.value = UiState.Exito(null)
            }
        }
    }

    fun iniciarSesion(correo: String, clave: String) {
        val correoLimpio = correo.trim()
        val claveLimpia = clave.trim()

        if (correoLimpio.isEmpty() || claveLimpia.isEmpty()) {
            _estado.value = UiState.Error("Por favor, ingresa tu correo y contraseña")
            return
        }

        viewModelScope.launch {
            _estado.value = UiState.Cargando
            authRepository.iniciarSesion(correoLimpio, claveLimpia)
                .onSuccess { sesion ->
                    _estado.value = UiState.Exito(sesion)
                }
                .onFailure {
                    _estado.value = UiState.Error("Credenciales incorrectas o usuario no registrado")
                }
        }
    }

    fun cerrarSesion() {
        authRepository.cerrarSesion()
        _estado.value = UiState.Exito(null)
    }

    fun limpiarError() {
        if (_estado.value is UiState.Error) {
            _estado.value = UiState.Exito(null)
        }
    }
}
