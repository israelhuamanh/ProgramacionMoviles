package com.huaman.simuladordecredito.model

import java.time.LocalDate

data class Producto(
    val nombre: String,
    val precioUnitario: Double,
    val cantidad: Int
) {
    fun obtenerMontoTotal(): Double {
        return precioUnitario * cantidad
    }
}

data class Cuota(
    val numero: Int,
    val fechaPago: LocalDate,
    val montoInicialMensual: Double,
    val montoAPagar: Double,
    val deudaRestante: Double
)

data class Credito(
    val producto: Producto,
    val cuotas: Int,
    val porcentajeInteres: Double,
    val montoInicial: Double,
    val interesTotal: Double,
    val montoAPagar: Double,
    val pagoMensual: Double,
    val cronograma: List<Cuota>
)
