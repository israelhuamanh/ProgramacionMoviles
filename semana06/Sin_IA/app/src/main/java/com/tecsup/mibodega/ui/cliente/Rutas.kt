package com.tecsup.mibodega.ui.cliente

object Rutas {
    const val BIENVENIDA = "bienvenida"
    const val LOGIN = "login"
    const val REGISTRO = "registro"
    const val INICIO = "inicio"
    const val DETALLE = "detalle/{productoId}"
    const val CARRITO = "carrito"
    const val DATOS_ENTREGA = "datos_entrega"
    const val CONFIRMACION = "confirmacion/{pedidoId}"

    fun detalle(productoId: Int) = "detalle/$productoId"
    fun confirmacion(pedidoId: String) = "confirmacion/$pedidoId"
}
