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

class PostsViewModel(private val repo: PostRepository) : ViewModel() {
    private val _lista = MutableStateFlow<UiState<List<Post>>>(UiState.Cargando)
    val lista = _lista.asStateFlow()
    private var peticionLista: Job? = null

    init { cargar() }

    fun cargar() {
        peticionLista?.cancel()
        _lista.value = UiState.Cargando
        peticionLista = viewModelScope.launch {
            _lista.value = repo.listar().aEstado()
        }
    }
}
