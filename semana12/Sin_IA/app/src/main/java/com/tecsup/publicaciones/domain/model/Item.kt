package com.tecsup.publicaciones.domain.model

data class Item(
    val id: Int,
    val titulo: String,
    val subtitulo: String,
    val contenido: String,
    val autorId: Int = 1,
    val imagenUrl: String? = null,
    val esFavorito: Boolean = false
)
