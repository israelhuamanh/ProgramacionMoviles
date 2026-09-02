package com.huaman.simuladordecredito.utils

import java.util.Scanner

class ConsoleUtils {
    private val scanner = Scanner(System.in)

    fun leerTexto(mensaje: String): String {
        print(mensaje)
        return scanner.nextLine().trim()
    }

    fun leerDouble(mensaje: String): Double {
        while (true) {
            print(mensaje)
            val entrada = scanner.nextLine()
            try {
                val valor = entrada.toDouble()
                if (valor > 0) return valor
                println("Error: El valor debe ser mayor a 0.")
            } catch (e: NumberFormatException) {
                println("Error: Por favor ingrese un número válido.")
            }
        }
    }

    fun leerEntero(mensaje: String, valoresValidos: List<Int>? = null): Int {
        while (true) {
            print(mensaje)
            val entrada = scanner.nextLine()
            try {
                val valor = entrada.toInt()
                if (valoresValidos != null && !valoresValidos.contains(valor)) {
                    println("Error: Valor no permitido. Valores válidos: $aloresValidos")
                    continue
                }
                if (valor > 0) return valor
                println("Error: El valor debe ser mayor a 0.")
            } catch (e: NumberFormatException) {
                println("Error: Por favor ingrese un número entero válido.")
            }
        }
    }
}
