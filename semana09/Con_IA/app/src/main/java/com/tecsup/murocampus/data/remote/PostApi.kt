package com.tecsup.murocampus.data.remote

import com.tecsup.murocampus.model.*
import retrofit2.http.*

interface PostApi {
    @GET("posts")
    suspend fun listar(): List<Post>

    @GET("posts/{id}")
    suspend fun detalle(@Path("id") id: Int): Post

    @GET("posts/{id}/comments")
    suspend fun comentarios(@Path("id") id: Int): List<Comentario>

    @GET("posts")
    suspend fun porUsuario(@Query("userId") userId: Int): List<Post>

    @POST("posts")
    suspend fun crear(@Body nuevo: NuevoPost): Post

    @DELETE("posts/{id}")
    suspend fun eliminar(@Path("id") id: Int): retrofit2.Response<Unit>
}
