package com.tecsup.cafecampus.ui.navigation

sealed class Rutas(val ruta: String) {
    object Splash : Rutas("splash")
    object Login : Rutas("login")
    object Menu : Rutas("menu")
    object DetalleProducto : Rutas("detalle_producto/{productoId}") {
        fun crearRuta(id: Int) = "detalle_producto/$id"
    }
    object ConfirmarPedido : Rutas("confirmar_pedido")
    object Seguimiento : Rutas("seguimiento/{pedidoId}") {
        fun crearRuta(id: Int) = "seguimiento/$id"
    }
    object MisPedidos : Rutas("mis_pedidos")
    object PanelAdmin : Rutas("panel_admin")
    object PedidosAdmin : Rutas("pedidos_admin")
    object DetallePedidoAdmin : Rutas("detalle_pedido_admin/{pedidoId}") {
        fun crearRuta(id: Int) = "detalle_pedido_admin/$id"
    }
    object MenuAdmin : Rutas("menu_admin")
    object FormularioProducto : Rutas("formulario_producto?productoId={productoId}") {
        fun crearRuta(id: Int? = null) = if (id != null) "formulario_producto?productoId=$id" else "formulario_producto"
    }
    object Ajustes : Rutas("ajustes")
}
