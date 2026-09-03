# Laboratorio 03 - Registro de Producto con Jetpack Compose

**Estudiante:** Israel Huaman H.

## DescripciÃ³n
Este es un proyecto de laboratorio para practicar la creaciÃ³n de interfaces de usuario utilizando Jetpack Compose. Implementa un formulario de registro de producto con los campos Nombre, Precio y Cantidad, manejando el estado de la UI (con `remember` y `mutableStateOf`) y mostrando una tarjeta de resumen al presionar el botÃ³n de agregar.

## Capturas de Pantalla

**Pantalla VacÃ­a:**
*(Reemplazar con la captura de la pantalla inicial)*
![Pantalla Inicial](pantalla_vacia.png)

**Producto Registrado:**
*(Reemplazar con la captura luego de registrar el producto)*
![Producto Registrado](producto_registrado.png)

## Pregunta del Laboratorio
**Â¿QuÃ© pasarÃ­a si declaras las variables de los campos SIN `remember`?**

Si se declaran las variables usando `mutableStateOf` pero sin envolverlas en `remember`, el estado no "sobrevivirÃ­a" a las recomposiciones. Cada vez que el usuario ingresa un carÃ¡cter en el `OutlinedTextField`, Compose vuelve a ejecutar la funciÃ³n (recomposiciÃ³n). Sin `remember`, la variable de estado se reinicializarÃ­a nuevamente a su valor por defecto (como `""`), y por lo tanto, el campo de texto se blanquearÃ­a y no permitirÃ­a escribir correctamente. `remember` le dice a Compose que recuerde el valor en la memoria a travÃ©s de las recomposiciones.

## Mejora con IA
| Prompt que usé | Qué generó Gemini | Qué acepté o corregí (y por qué) |
|---|---|---|
| Agrega validación de campos vacíos y muestra un mensaje de error en rojo en vez del Card. Agrega también un botón Limpiar. | Generó un estado `errorMessage` y dos botones dentro de un `Row`. | Acepté la lógica principal, pero corregí el color del mensaje de error a un rojo de Material Design (0xFFB00020) y le aumenté el espaciado (Spacer a 16.dp) para que respire mejor visualmente. |
