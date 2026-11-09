package com.tecsup.publicaciones.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class ItemEntity(
    @PrimaryKey val id: Int,
    val titulo: String,
    val subtitulo: String,
    val contenido: String,
    val autorId: Int,
    val imagenUrl: String?,
    val esFavorito: Boolean = false
)
