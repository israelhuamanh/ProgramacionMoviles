package com.tecsup.publicaciones.ui.lista

import com.tecsup.publicaciones.data.repository.ItemRepository
import com.tecsup.publicaciones.domain.model.Item
import com.tecsup.publicaciones.notification.Notificador
import com.tecsup.publicaciones.ui.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

class FakeNotificador : Notificador {
    var ultimoTitulo: String? = null
    var ultimoTexto: String? = null
    var llamadas: Int = 0

    override fun mostrar(titulo: String, texto: String) {
        ultimoTitulo = titulo
        ultimoTexto = texto
        llamadas++
    }
}

class FakeItemRepository : ItemRepository {
    private val _datos = MutableStateFlow<List<Item>>(emptyList())
    var simularErrorRed: Boolean = false
    var nuevosAlRefrescar: List<Item> = listOf(
        Item(id = 1, titulo = "Post 1", subtitulo = "Sub 1", contenido = "Cuerpo 1"),
        Item(id = 2, titulo = "Post 2", subtitulo = "Sub 2", contenido = "Cuerpo 2")
    )

    override val items: Flow<List<Item>> = _datos
    override val favoritos: Flow<List<Item>> = _datos.map { lista -> lista.filter { it.esFavorito } }

    fun precargarLocal(lista: List<Item>) {
        _datos.value = lista
    }

    override suspend fun refrescar(): Result<Int> {
        if (simularErrorRed) {
            return Result.failure(IOException("Fallo de red simulado"))
        }
        val favIds = _datos.value.filter { it.esFavorito }.map { it.id }.toSet()
        val actualizados = nuevosAlRefrescar.map { it.copy(esFavorito = it.id in favIds) }
        _datos.value = actualizados
        return Result.success(actualizados.size)
    }

    override suspend fun obtenerDetalle(id: Int): Result<Item> {
        val encontrado = _datos.value.find { it.id == id }
        return if (encontrado != null) Result.success(encontrado)
        else Result.failure(IOException("No encontrado"))
    }

    override suspend fun alternarFavorito(id: Int) {
        _datos.value = _datos.value.map {
            if (it.id == id) it.copy(esFavorito = !it.esFavorito) else it
        }
    }

    override suspend fun cantidadGuardada(): Int = _datos.value.size

    override suspend fun cantidadFavoritos(): Int = _datos.value.count { it.esFavorito }

    override suspend fun limpiarCache() {
        _datos.value = emptyList()
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class ListaViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var fakeRepo: FakeItemRepository
    private lateinit var fakeNotificador: FakeNotificador

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeRepo = FakeItemRepository()
        fakeNotificador = FakeNotificador()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `refrescar con exito actualiza estado a Exito y dispara notificacion local`() = runTest {
        val viewModel = ListaViewModel(fakeRepo, fakeNotificador)
        advanceUntilIdle()

        val estado = viewModel.estado.value
        assertTrue(estado is UiState.Exito)
        assertEquals(2, (estado as UiState.Exito).datos.size)
        assertNull(viewModel.aviso.value)
        assertEquals(1, fakeNotificador.llamadas)
        assertEquals("Se guardaron 2 elementos", fakeNotificador.ultimoTexto)
    }

    @Test
    fun `sin conexion y sin datos locales cambia estado a Error`() = runTest {
        fakeRepo.simularErrorRed = true
        val viewModel = ListaViewModel(fakeRepo, fakeNotificador)
        advanceUntilIdle()

        val estado = viewModel.estado.value
        assertTrue(estado is UiState.Error)
        assertEquals("No hay conexión a internet", (estado as UiState.Error).mensaje)
        assertEquals(0, fakeNotificador.llamadas)
    }

    @Test
    fun `sin conexion pero con datos guardados en Room muestra lista y aviso offline`() = runTest {
        fakeRepo.precargarLocal(
            listOf(Item(id = 10, titulo = "Guardado", subtitulo = "Offline", contenido = "Texto"))
        )
        fakeRepo.simularErrorRed = true

        val viewModel = ListaViewModel(fakeRepo, fakeNotificador)
        advanceUntilIdle()

        val estado = viewModel.estado.value
        assertTrue(estado is UiState.Exito)
        assertEquals(1, (estado as UiState.Exito).datos.size)
        assertEquals("Sin conexión: mostrando datos guardados", viewModel.aviso.value)
    }

    @Test
    fun `alternarFavorito y sincronizar conserva los favoritos marcados`() = runTest {
        val viewModel = ListaViewModel(fakeRepo, fakeNotificador)
        advanceUntilIdle()

        viewModel.alternarFavorito(1)
        advanceUntilIdle()

        assertEquals(1, viewModel.totalFavoritos.value)

        // Sincronizar nuevamente no debe perder el favorito
        viewModel.refrescar()
        advanceUntilIdle()

        val lista = (viewModel.estado.value as UiState.Exito).datos
        val item1 = lista.first { it.id == 1 }
        assertTrue(item1.esFavorito)
        assertEquals(1, viewModel.totalFavoritos.value)
    }
}
