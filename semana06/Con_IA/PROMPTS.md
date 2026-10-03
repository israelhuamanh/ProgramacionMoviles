# Registro de Prompts - Laboratorio 06 (Mejora con IA)

Este documento registra los prompts utilizados con el asistente de Inteligencia Artificial para implementar la mejora obligatoria: conexión de estado de favoritos entre el `DropdownMenu` de cada producto y un badge con contador en el `NavigationDrawer`.

---

## Prompt 1: Elevación de estado y contador de favoritos
**Herramienta / Asistente:** Asistente IA (Gemini / Claude)  
**Objetivo:** Diseñar la arquitectura de estado compartido para conectar los favoritos del menú contextual con el Drawer.

### Prompt:
> *"Tengo una aplicación en Jetpack Compose con una pantalla `CatalogoScreen` que tiene tarjetas de producto `TarjetaProducto`. Cada tarjeta tiene un `DropdownMenu` con una opción 'Favoritos'. Además, tengo un `AppDrawer` envuelto en `ModalNavigationDrawer` en `AppNavegacion`. Necesito conectar ambas partes: cuando el usuario presione 'Favoritos' en el menú de un producto, se debe alternar o sumar a una lista de favoritos y contar cuántos productos favoritos hay en total. ¿Cómo elevo el estado (state hoisting) en `AppNavegacion` para compartir los productos favoritos tanto con el catálogo como con el drawer?"*

### Resultado obtenido:
La IA propuso mantener un `remember { mutableStateListOf<Int>() }` (o `Set<Int>`) en `AppNavegacion` que almacena los IDs de los productos marcados como favoritos. Este conjunto de favoritos se pasa como parámetro a `CatalogoScreen` y a `AppDrawer` para calcular el conteo `.size`.

---

## Prompt 2: Creación del Badge interactivo en el NavigationDrawer
**Herramienta / Asistente:** Asistente IA (Gemini / Claude)  
**Objetivo:** Agregar el componente `Badge` y `BadgedBox` en el ítem 'Favoritos' del NavigationDrawer.

### Prompt:
> *"¿Cómo agrego un Badge con contador al ítem 'Favoritos' dentro de un `ModalDrawerSheet` usando `NavigationDrawerItem` en Jetpack Compose Material 3? Quiero que solo se muestre si la cantidad de favoritos es mayor a 0, con fondo de color distintivo."*

### Resultado obtenido:
La IA recomendó usar el parámetro `badge = { ... }` que ofrece nativamente `NavigationDrawerItem`, implementando `Badge` de Material 3 con el texto del contador (`${cantidadFavoritos}`) cuando la cantidad sea mayor a 0.

---

## Prompt 3: Refactorización y sincronización visual en TarjetaProducto
**Herramienta / Asistente:** Asistente IA (Gemini / Claude)  
**Objetivo:** Actualizar el estado visual del DropdownMenu y feedback con Snackbar.

### Prompt:
> *"En `TarjetaProducto`, quiero que el DropdownMenu cambie el texto a 'Quitar de favoritos' o muestre un icono de corazón relleno si ya es favorito, y que al hacer click se muestre un Snackbar informando al usuario la acción realizada."*

### Resultado obtenido:
Se agregó el parámetro `esFavorito: Boolean` en `TarjetaProducto`, permitiendo modificar dinámicamente el texto del item en el menú ("Agregar a favoritos" / "Quitar de favoritos") y activar un `SnackbarHostState` con feedback inmediato.
