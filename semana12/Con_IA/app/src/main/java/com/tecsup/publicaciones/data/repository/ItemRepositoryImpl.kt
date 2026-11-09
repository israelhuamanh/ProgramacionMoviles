package com.tecsup.publicaciones.data.repository

import com.tecsup.publicaciones.data.local.ItemDao
import com.tecsup.publicaciones.data.mapper.aEntity
import com.tecsup.publicaciones.data.mapper.aModelo
import com.tecsup.publicaciones.data.remote.ApiService
import com.tecsup.publicaciones.domain.model.Item
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException

class ItemRepositoryImpl(
    private val api: ApiService,
    private val dao: ItemDao
) : ItemRepository {

    override val items = dao.todos().map { lista -> lista.map { it.aModelo() } }
    override val favoritos = dao.favoritos().map { lista -> lista.map { it.aModelo() } }

    override suspend fun refrescar(): Result<Int> = try {
        // Si insertas sin cuidado, pierdes los favoritos. Se conservan aquí:
        val favoritos = dao.idsFavoritos().toSet()
        val nuevos = api.listar() // List<ItemDto>
            .map { it.aEntity(esFavorito = it.id in favoritos) }
        dao.guardarTodos(nuevos)
        Result.success(nuevos.size)
    } catch (e: IOException) { // sin internet
        Result.failure(e)
    } catch (e: HttpException) { // 404, 500...
        Result.failure(e)
    }

    override suspend fun obtenerDetalle(id: Int): Result<Item> = try {
        val esFav = id in dao.idsFavoritos()
        val dto = api.obtenerDetalle(id)
        Result.success(dto.aModelo(esFavorito = esFav))
    } catch (e: IOException) {
        val local = dao.obtenerPorId(id)
        if (local != null) {
            Result.success(local.aModelo())
        } else {
            Result.failure(e)
        }
    } catch (e: HttpException) {
        Result.failure(e)
    }

    override suspend fun alternarFavorito(id: Int) = dao.alternarFavorito(id)
    override suspend fun cantidadGuardada() = dao.contar()
    override suspend fun cantidadFavoritos() = dao.contarFavoritos()
    override suspend fun limpiarCache() = dao.borrarTodo()
}
