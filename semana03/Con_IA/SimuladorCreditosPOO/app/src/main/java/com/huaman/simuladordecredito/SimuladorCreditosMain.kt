package com.huaman.simuladordecredito

import com.huaman.simuladordecredito.model.Producto
import com.huaman.simuladordecredito.service.SimuladorCreditoService
import com.huaman.simuladordecredito.utils.ConsoleUtils
import java.time.format.DateTimeFormatter

fun main() {
    val console = ConsoleUtils()
    val simuladorService = SimuladorCreditoService()

    println("=== SIMULADOR DE CREDITO (POO & IA) ===")

    val nombre = console.leerTexto("Nombre del Producto: ")
    val precio = console.leerDouble("Precio unitario: ")
    val cantidad = console.leerEntero("Cantidad: ")

    val producto = Producto(nombre, precio, cantidad)

    val cuotas = console.leerEntero(
        mensaje = "Ingrese N de Cuotas (Solo se permite 6, 12 o 24): ",
        valoresValidos = listOf(6, 12, 24)
    )

    try {
        val credito = simuladorService.simularCredito(producto, cuotas)

        println("\n=== RESUMEN DE LA OPERACION ===")
        println("Producto       : ${credito.producto.nombre}")
        println("Monto Inicial  : $`")
        println("Interes        : $`")
        println("MONTO A PAGAR  : $`")
        println("PAGO MENSUAL   : $`")

        println("\n=== CRONOGRAMA DE PAGOS ===")
        println("Nro  | FECHA        | MONTO      | P.MENSUAL  | RESTA PAGO")
        println("------------------------------------------------------------")

        val formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy")

        for (cuota in credito.cronograma) {
            println(
                String.format(
                    "%-4d | %-12s | $%-9.2f | $%-9.2f | $%-9.2f",
                    cuota.numero,
                    cuota.fechaPago.format(formatoFecha),
                    cuota.montoInicialMensual,
                    cuota.montoAPagar,
                    cuota.deudaRestante
                )
            )
        }
    } catch (e: Exception) {
        println("Ocurrió un error inesperado al procesar el crédito: ${e.message}")
    }
}
