package com.tecsup.cafecampus.data.repository

import com.tecsup.cafecampus.data.local.CafeDatabase
import com.tecsup.cafecampus.data.local.entity.DetallePedido
import com.tecsup.cafecampus.data.local.entity.Pedido
import com.tecsup.cafecampus.data.local.entity.Producto
import com.tecsup.cafecampus.data.local.entity.Usuario
import com.tecsup.cafecampus.data.local.relation.PedidoConDetalle
import com.tecsup.cafecampus.model.EstadoPedido
import com.tecsup.cafecampus.model.ItemCarrito
import kotlinx.coroutines.flow.Flow

class CafeRepositoryRoom(private val db: CafeDatabase) : CafeRepository {

    override suspend fun login(correo: String, clave: String): Usuario? {
        return db.usuarioDao().login(correo, clave)
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
        val total = items.sumOf { it.producto.precio * it.cantidad }
        val pedido = Pedido(
            clienteId = clienteId,
            estado = EstadoPedido.RECIBIDO.name,
            horaRecojo = horaRecojo,
            pago = pago,
            nota = nota,
            total = total
        )
        val detalles = items.map { item ->
            DetallePedido(
                pedidoId = 0,
                productoId = item.producto.id,
                nombreProducto = item.producto.nombre,
                cantidad = item.cantidad,
                precioUnitario = item.producto.precio
            )
        }
        return db.pedidoDao().crearPedido(pedido, detalles)
    }

    override fun observarPedidosCliente(clienteId: Int): Flow<List<PedidoConDetalle>> {
        return db.pedidoDao().observarPorCliente(clienteId)
    }

    override fun observarPedidosPorEstado(estado: EstadoPedido): Flow<List<PedidoConDetalle>> {
        return db.pedidoDao().observarPorEstado(estado.name)
    }

    override fun observarPedido(id: Int): Flow<PedidoConDetalle?> {
        return db.pedidoDao().observarUno(id)
    }

    override suspend fun cambiarEstado(pedidoId: Int, estado: EstadoPedido) {
        db.pedidoDao().cambiarEstado(pedidoId, estado.name)
    }

    override fun observarVentasHoy(inicioDia: Long): Flow<Double> {
        return db.pedidoDao().ventasDesde(inicioDia)
    }

    override fun observarCantidadPorEstado(estado: EstadoPedido): Flow<Int> {
        return db.pedidoDao().contarPorEstado(estado.name)
    }
}
