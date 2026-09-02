package com.huaman.simuladordecredito

fun main() {
    println("=== SIMULADOR DE CREDITO ===")

    print("Nombre del Producto: ")
    val nombreProducto = readLine() ?: ""

    print("Precio unitario: ")
    val precio = readLine()?.toDoubleOrNull() ?: 0.0

    print("Cantidad: ")
    val cantidad = readLine()?.toIntOrNull() ?: 0

    val montoInicial = precio * cantidad
}
