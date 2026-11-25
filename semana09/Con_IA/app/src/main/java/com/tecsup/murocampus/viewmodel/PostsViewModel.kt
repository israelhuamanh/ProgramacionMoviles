package com.tecsup.murocampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.murocampus.data.PostRepository
import com.tecsup.murocampus.data.mensajeDe
import com.tecsup.murocampus.model.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

private fun <T> Result<T>.aEstado(): UiState<T> =
    fold({ UiState.Exito(it) }, { UiState.Error(mensajeDe(it)) })

data class DetalleUi(val post: Post, val comentarios: List<Comentario>)

class PostsViewModel(private val repo: PostRepository) : ViewModel() {
    private val _lista = MutableStateFlow<UiState<List<Post>>>(UiState.Cargando)
    val lista = _lista.asStateFlow()
    private var peticionLista: Job? = null

    private val _usuarioFiltro = MutableStateFlow<Int?>(null)
    val usuarioFiltro = _usuarioFiltro.asStateFlow()

    init { cargar() }

    fun filtrar(userId: Int?) {
        if (userId == _usuarioFiltro.value) return
        _usuarioFiltro.value = userId
        cargar()
    }

    fun cargar() {
        peticionLista?.cancel()
        _lista.value = UiState.Cargando
        peticionLista = viewModelScope.launch {
            val userId = _usuarioFiltro.value
            _lista.value = (if (userId == null) repo.listar() else repo.porUsuario(userId)).aEstado()
        }
    }

    private val _detalle = MutableStateFlow<UiState<DetalleUi>>(UiState.Cargando)
    val detalle = _detalle.asStateFlow()
    private var peticionDetalle: Job? = null

    private var seleccionado: Int? = null

    fun abrirSiNecesario(id: Int) {
        if (seleccionado != id) abrir(id)
    }

    fun abrir(id: Int) {
        seleccionado = id
        peticionDetalle?.cancel()
        _detalle.value = UiState.Cargando
        peticionDetalle = viewModelScope.launch {
            _detalle.value = repo.detalle(id).fold(
                onSuccess = { post -> repo.comentarios(id).map { DetalleUi(post, it) }.aEstado() },
                onFailure = { UiState.Error(mensajeDe(it)) }
            )
        }
    }

    fun cerrarDetalle() {
        seleccionado = null
        peticionDetalle?.cancel()
    }
}
