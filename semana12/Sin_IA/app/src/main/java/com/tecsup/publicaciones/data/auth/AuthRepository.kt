package com.tecsup.publicaciones.data.auth

import com.tecsup.publicaciones.domain.model.Sesion

interface AuthRepository {
    suspend fun iniciarSesion(correo: String, clave: String): Result<Sesion>
    suspend fun sesionActual(): Sesion? // null si no hay sesión
    fun cerrarSesion()
}
