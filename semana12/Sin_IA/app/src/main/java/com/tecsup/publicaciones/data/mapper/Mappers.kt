package com.tecsup.publicaciones.data.mapper

import com.tecsup.publicaciones.data.local.ItemEntity
import com.tecsup.publicaciones.data.remote.dto.ItemDto
import com.tecsup.publicaciones.domain.model.Item

fun ItemDto.aEntity(esFavorito: Boolean = false): ItemEntity {
    val resumen = body.replace("\n", " ").let {
        if (it.length > 90) it.take(90) + "..." else it
    }
    return ItemEntity(
        id = id,
        titulo = title.replaceFirstChar { it.uppercase() },
        subtitulo = resumen,
        contenido = body,
        autorId = userId,
        imagenUrl = null,
        esFavorito = esFavorito
    )
}

fun ItemEntity.aModelo(): Item {
    return Item(
        id = id,
        titulo = titulo,
        subtitulo = subtitulo,
        contenido = contenido,
        autorId = autorId,
        imagenUrl = imagenUrl,
        esFavorito = esFavorito
    )
}

fun ItemDto.aModelo(esFavorito: Boolean = false): Item {
    val resumen = body.replace("\n", " ").let {
        if (it.length > 90) it.take(90) + "..." else it
    }
    return Item(
        id = id,
        titulo = title.replaceFirstChar { it.uppercase() },
        subtitulo = resumen,
        contenido = body,
        autorId = userId,
        imagenUrl = null,
        esFavorito = esFavorito
    )
}
