package com.huaman.lab02carritokotlin

data class Producto(
    val nombre: String,
    val precio: Double,
    var cantidad: Int
)

fun calcularSubtotal(productos: List<Producto>): Double {
    var subtotal = 0.0

    for (p in productos) {
        subtotal += p.precio * p.cantidad
    }

    return subtotal
}

fun calcularIGV(subtotal: Double): Double {
    return subtotal * 0.18
}

fun calcularTotal(subtotal: Double, igv: Double): Double {
    return subtotal + igv
}

fun mostrarDetalle(productos: List<Producto>) {
    println("--------- DETALLE DEL CARRITO -------------------------")

    var i = 1

    for (p in productos) {
        val importe = p.precio * p.cantidad

        println(
            String.format(
                "%d. %-20s x%d S/ %8.2f",
                i,
                p.nombre,
                p.cantidad,
                importe
            )
        )

        i++
    }
}

fun calcularDescuento(total: Double): Double {
    return when {
        total > 5000 -> total * 0.10
        total > 3000 -> total * 0.05
        else -> 0.0
    }
}

fun buscarProducto(
    productos: List<Producto>,
    nombre: String
): Producto? {
    return productos.find {
        it.nombre.equals(nombre, ignoreCase = true)
    }
}

fun main() {

    println("=========================================")
    println("    CARRITO DE COMPRAS - TIENDA TECSUP")
    println("=========================================")

    val nombreCliente = "Huaman"
    val carrito = mutableListOf<Producto>()

    println("Cliente: $nombreCliente")
    println()

    carrito.add(Producto("Laptop HP", 2500.0, 1))
    carrito.add(Producto("Mouse Logitech", 45.5, 2))
    carrito.add(Producto("Audifonos Sony", 120.0, 1))
    carrito.add(Producto("USB Kingston 64GB", 25.0, 3))

    for (producto in carrito) {
        println("Producto agregado: ${producto.nombre}")
    }

    println()

    mostrarDetalle(carrito)

    println("Cantidad de productos: ${carrito.size}")
    println()

    val subtotal = calcularSubtotal(carrito)
    val igv = calcularIGV(subtotal)
    val total = calcularTotal(subtotal, igv)

    println(String.format("Subtotal       : S/ %.2f", subtotal))
    println(String.format("IGV (18%%)      : S/ %.2f", igv))
    println(String.format("TOTAL A PAGAR  : S/ %.2f", total))

    println()

    val masCaro = carrito.maxByOrNull { it.precio }

    if (masCaro != null) {
        println(
            "Producto mas caro: ${masCaro.nombre} " +
                    String.format("(S/ %.2f)", masCaro.precio)
        )
    }

    val descuento = calcularDescuento(total)

    if (descuento > 0) {
        println(String.format("Descuento aplicado: S/ %.2f", descuento))
        println(
            String.format(
                "TOTAL CON DESCUENTO: S/ %.2f",
                total - descuento
            )
        )
    } else {
        println("No se aplica descuento")
    }

    println()

    val productoBuscado = buscarProducto(
        carrito,
        "Mouse Logitech"
    )

    if (productoBuscado != null) {
        println("Producto encontrado: ${productoBuscado.nombre}")
    } else {
        println("Producto no encontrado")
    }

    println()

    val eliminado = carrito.removeIf {
        it.nombre == "USB Kingston 64GB"
    }

    if (eliminado) {
        println("Producto eliminado: USB Kingston 64GB")
    } else {
        println("Producto no encontrado para eliminar")
    }

    println()

    mostrarDetalle(carrito)

    val nuevoSubtotal = calcularSubtotal(carrito)
    val nuevoIgv = calcularIGV(nuevoSubtotal)
    val nuevoTotal = calcularTotal(nuevoSubtotal, nuevoIgv)

    println("Cantidad de productos: ${carrito.size}")
    println(String.format("Subtotal       : S/ %.2f", nuevoSubtotal))
    println(String.format("IGV (18%%)      : S/ %.2f", nuevoIgv))
    println(String.format("TOTAL A PAGAR  : S/ %.2f", nuevoTotal))
}