package com.huaman.simuladordecredito

import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun main() {
    println("=== SIMULADOR DE CREDITO ===")

    print("Nombre del Producto: ")
    val nombreProducto = readLine() ?: ""

    print("Precio unitario: ")
    val precio = readLine()?.toDoubleOrNull() ?: 0.0

    print("Cantidad: ")
    val cantidad = readLine()?.toIntOrNull() ?: 0

    val montoInicial = precio * cantidad

    var cuotas = 0
    while (cuotas != 6 && cuotas != 12 && cuotas != 24) {
        print("Ingrese N de Cuotas (Solo se permite 6, 12 o 24): ")
        val entradaCuotas = readLine()?.toIntOrNull() ?: 0

        if (entradaCuotas == 6 || entradaCuotas == 12 || entradaCuotas == 24) {
            cuotas = entradaCuotas
        } else {
            println("Error: Numero de cuotas no valido. Intente de nuevo.\n")
        }
    }

    val porcentajeInteres = when (cuotas) {
        6 -> 0.20
        12 -> 0.40
        24 -> 0.60
        else -> 0.0
    }

    val interes = montoInicial * porcentajeInteres
    val montoAPagar = montoInicial + interes
    val pagoMensual = montoAPagar / cuotas

    println("\n=== RESUMEN DE LA OPERACION ===")
    println("Producto       : $nombreProducto")
    println("Monto Inicial  : $${String.format("%.2f", montoInicial)}")
    println("Interes        : $${String.format("%.2f", interes)}")
    println("MONTO A PAGAR  : $${String.format("%.2f", montoAPagar)}")
    println("PAGO MENSUAL   : $${String.format("%.2f", pagoMensual)}")

    println("\n=== CRONOGRAMA DE PAGOS ===")
    println("N°   | FECHA        | MONTO      | P.MENSUAL  | RESTA PAGO")
    println("------------------------------------------------------------")

    var fechaPago = LocalDate.now()
    val formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    var deudaActual = montoAPagar

    for (i in 1..cuotas) {
        fechaPago = fechaPago.plusMonths(1)
        var restaPago = deudaActual - pagoMensual

        if (restaPago < 0.01) restaPago = 0.0

        println(
            String.format(
                "%-4d | %-12s | $%-9.2f | $%-9.2f | $%-9.2f",
                i,
                fechaPago.format(formatoFecha),
                deudaActual,
                pagoMensual,
                restaPago
            )
        )
        deudaActual = restaPago
    }
}