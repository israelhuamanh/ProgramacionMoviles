package com.tecsup.mibodega.ui.cliente.modelo

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Pedido(
    val id: String = "#" + (1000..9999).random(),
    val fecha: String = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date()),
    val items: List<ItemCarrito>,
    val subtotal: Double,
    val costoEnvio: Double,
    val total: Double,
    val esRecojoEnTienda: Boolean,
    val nombreCliente: String,
    val telefonoCliente: String,
    val direccionCliente: String,
    val referenciaCliente: String,
    val metodoPago: String
)
