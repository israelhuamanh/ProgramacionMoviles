package com.huaman.simuladordecredito.service

import com.huaman.simuladordecredito.model.Credito
import com.huaman.simuladordecredito.model.Cuota
import com.huaman.simuladordecredito.model.Producto
import java.time.LocalDate

class SimuladorCreditoService {

    fun simularCredito(producto: Producto, numeroCuotas: Int): Credito {
        if (!esNumeroCuotasValido(numeroCuotas)) {
            throw IllegalArgumentException("El número de cuotas no es válido. Solo se permite 6, 12 o 24.")
        }

        val montoInicial = producto.obtenerMontoTotal()
        val porcentajeInteres = calcularPorcentajeInteres(numeroCuotas)
        val interesTotal = montoInicial * porcentajeInteres
        val montoAPagar = montoInicial + interesTotal
        val pagoMensual = montoAPagar / numeroCuotas

        val cronograma = generarCronograma(numeroCuotas, montoAPagar, pagoMensual)

        return Credito(
            producto = producto,
            cuotas = numeroCuotas,
            porcentajeInteres = porcentajeInteres,
            montoInicial = montoInicial,
            interesTotal = interesTotal,
            montoAPagar = montoAPagar,
            pagoMensual = pagoMensual,
            cronograma = cronograma
        )
    }

    private fun esNumeroCuotasValido(cuotas: Int): Boolean {
        return cuotas == 6 || cuotas == 12 || cuotas == 24
    }

    private fun calcularPorcentajeInteres(cuotas: Int): Double {
        return when (cuotas) {
            6 -> 0.20
            12 -> 0.40
            24 -> 0.60
            else -> 0.0
        }
    }

    private fun generarCronograma(
        numeroCuotas: Int,
        montoTotalAPagar: Double,
        pagoMensual: Double
    ): List<Cuota> {
        val cronograma = mutableListOf<Cuota>()
        var fechaActual = LocalDate.now()
        var deudaRestante = montoTotalAPagar

        for (i in 1..numeroCuotas) {
            fechaActual = fechaActual.plusMonths(1)
            deudaRestante -= pagoMensual
            
            if (deudaRestante < 0.01) {
                deudaRestante = 0.0
            }

            cronograma.add(
                Cuota(
                    numero = i,
                    fechaPago = fechaActual,
                    montoInicialMensual = deudaRestante + pagoMensual,
                    montoAPagar = pagoMensual,
                    deudaRestante = deudaRestante
                )
            )
        }
        return cronograma
    }
}
