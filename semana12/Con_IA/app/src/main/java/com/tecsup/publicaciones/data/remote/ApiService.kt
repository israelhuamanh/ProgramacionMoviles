package com.tecsup.publicaciones.data.remote

import com.tecsup.publicaciones.data.remote.dto.ItemDto
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("posts")
    suspend fun listar(): List<ItemDto>

    @GET("posts/{id}")
    suspend fun obtenerDetalle(@Path("id") id: Int): ItemDto
}
