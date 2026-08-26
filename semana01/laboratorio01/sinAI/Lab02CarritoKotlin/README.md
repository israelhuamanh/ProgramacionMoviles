# Laboratorio 02 - Carrito de Compras en Kotlin

## Nombre
Israel Huaman

## Descripción
Este proyecto implementa un carrito de compras en Kotlin

El programa permite:
- Crear productos usando una data class.
- Agregar productos a una lista mutable.
- Calcular el subtotal.
- Calcular el IGV del 18%.
- Calcular el total a pagar.
- Mostrar el detalle del carrito con columnas alineadas.
- Identificar el producto más caro.
- Aplicar un descuento usando when:
  - 5% si el total supera S/ 3000.
  - 10% si el total supera S/ 5000.

## Captura de consola
<img width="517" height="552" alt="cap" src="https://github.com/user-attachments/assets/9bc0aff7-7c5c-4443-a505-461db43db7a5" />


## ¿Por qué nombre y precio son val pero cantidad es var?

`nombre` y `precio` se declaran con `val` porque no deberían cambiar después de crear un producto

`cantidad` se declara con `var` porque puede modificarse si el cliente aumenta o disminuye la cantidad del producto en el carrito
