package com.tecsup.avisoscampus.data

import com.tecsup.avisoscampus.model.*
import kotlinx.coroutines.flow.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
interface AvisosRepository {
    val avisos: StateFlow<List<Aviso>>
    fun registrar(titulo: String, texto: String, origen: Origen): Aviso
}
class AvisosRepositoryMemoria : AvisosRepository {
    private val historial = MutableStateFlow<List<Aviso>>(emptyList())
    override val avisos = historial.asStateFlow()
    private var siguiente = 1
    override fun registrar(titulo: String, texto: String, origen: Origen): Aviso {
        val hora = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
        val aviso = Aviso(siguiente++, titulo, texto, origen, hora)
        historial.value = listOf(aviso) + historial.value
        return aviso
    }
}
