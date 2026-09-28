# Lab 04

**Estudiante:** Israel Huamán Huamán
**Curso:** Programación en Móviles

## Descripción
Aplicación de carrito de compras construida con Jetpack Compose que integra un formulario de captura de datos y una lista dinámica. Calcula el subtotal, IGV (18%) y total en tiempo real.

## Capturas de Pantalla


**a) ¿Por qué usar mutableStateListOf y no una MutableList normal?**
Porque `mutableStateListOf` crea una lista observable, si usamos una lista normal al agregar un producto los datos cambian en memoria pero la interfaz no se entera.

**b) ¿Por qué la lista se declara con val y aún así podemos agregarle elementos?**
Usamos `val` para que la referencia a la lista quede fija en la memoria.
**c) ¿Qué hace weight(1f) en la LazyColumn?**
Le indica a la lista que se expanda para ocupar todo el espacio vertical sobrante disponible