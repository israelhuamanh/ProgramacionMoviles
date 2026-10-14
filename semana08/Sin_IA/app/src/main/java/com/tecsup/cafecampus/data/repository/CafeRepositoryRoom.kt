package com.tecsup.cafecampus.data.repository

import com.tecsup.cafecampus.data.local.CafeDatabase
import com.tecsup.cafecampus.data.local.entity.Producto
import com.tecsup.cafecampus.data.local.entity.Usuario
import com.tecsup.cafecampus.data.local.relation.PedidoConDetalle
import com.tecsup.cafecampus.model.EstadoPedido
import com.tecsup.cafecampus.model.ItemCarrito
import kotlinx.coroutines.flow.Flow

class CafeRepositoryRoom(private val db: CafeDatabase) : CafeRepository {

    override suspend fun login(correo: String, clave: String): Usuario? {
        // TODO: Paso 4 - Implementar login con db.usuarioDao()
        TODO("Not yet implemented")
    }

    override fun observarProductos(): Flow<List<Producto>> {
        return db.productoDao().observarTodos()
    }

    override suspend fun guardarProducto(p: Producto) {
        if (p.id == 0) {
            db.productoDao().insertar(p)
        } else {
            db.productoDao().actualizar(p)
        }
    }

    override suspend fun eliminarProducto(p: Producto) {
        db.productoDao().eliminar(p)
    }

    override suspend fun cambiarDisponibilidad(id: Int, disponible: Boolean) {
        db.productoDao().cambiarDisponibilidad(id, disponible)
    }

    override suspend fun crearPedido(
        clienteId: Int,
        items: List<ItemCarrito>,
        horaRecojo: String,
        pago: String,
        nota: String
    ): Long {
        // TODO: Paso 4 - Convertir items a DetallePedido, calcular total y llamar a db.pedidoDao().crearPedido
        TODO("Not yet implemented")
    }

    override fun observarPedidosCliente(clienteId: Int): Flow<List<PedidoConDetalle>> {
        // TODO: Paso 4 - db.pedidoDao().observarPorCliente(clienteId)
        TODO("Not yet implemented")
    }

    override fun observarPedidosPorEstado(estado: EstadoPedido): Flow<List<PedidoConDetalle>> {
        // TODO: Paso 4 - db.pedidoDao().observarPorEstado(estado.name)
        TODO("Not yet implemented")
    }

    override fun observarPedido(id: Int): Flow<PedidoConDetalle?> {
        // TODO: Paso 4 - db.pedidoDao().observarUno(id)
        TODO("Not yet implemented")
    }

    override suspend fun cambiarEstado(pedidoId: Int, estado: EstadoPedido) {
        // TODO: Paso 4 - db.pedidoDao().cambiarEstado(pedidoId, estado.name)
        TODO("Not yet implemented")
    }

    override fun observarVentasHoy(inicioDia: Long): Flow<Double> {
        // TODO: Paso 4 - db.pedidoDao().ventasDesde(inicioDia)
        TODO("Not yet implemented")
    }

    override fun observarCantidadPorEstado(estado: EstadoPedido): Flow<Int> {
        // TODO: Paso 4 - db.pedidoDao().contarPorEstado(estado.name)
        TODO("Not yet implemented")
    }
}
