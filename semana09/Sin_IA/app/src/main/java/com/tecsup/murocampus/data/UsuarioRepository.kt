package com.tecsup.murocampus.data

import com.tecsup.murocampus.model.Usuario
import kotlinx.coroutines.CancellationException
import retrofit2.http.GET

interface UsuarioApi {
    @GET("users")
    suspend fun listar(): List<Usuario>
}

interface UsuarioRepository {
    suspend fun listar(): Result<List<Usuario>>
}

class UsuarioRepositoryRemote(private val api: UsuarioApi) : UsuarioRepository {
    override suspend fun listar(): Result<List<Usuario>> = try {
        Result.success(api.listar())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        Result.failure(e)
    }
}
