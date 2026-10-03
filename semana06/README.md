# Laboratorio 06: Menú - Navegación (TECSUP Store)

Este repositorio contiene la implementación del **Laboratorio 06**, dividido en dos fases: desarrollo propio sin asistencia de IA y optimización asistida por IA.

---

## 📁 Estructura del Laboratorio

- **`Sin_IA/` (Fase 1 - Desarrollo propio)**:
  - Implementación de `DropdownMenu` contextual de 3 puntos (⋮) en cada tarjeta de producto (`TarjetaProducto.kt`) con opciones *Favoritos*, *Compartir* y *Reportar*, con sus respectivos íconos y separadores.
  - Implementación de `NavigationDrawer` con `ModalDrawerSheet` (`AppDrawer.kt`) como navegación principal, envolviendo el `Scaffold` en `AppNavegacion.kt`.
  - Personalización con encabezado de usuario (Avatar "MR", nombre y correo) e ítem activo resaltado.
  - Navegación real condicional entre Inicio, Mis pedidos, Favoritos y Perfil.

- **`Con_IA/` (Fase 2 - Mejora con IA)**:
  - Archivo `PROMPTS.md` con el registro de los prompts utilizados con el asistente IA.
  - **Mejora obligatoria implementada**: Badge con contador numérico interactivo en el ítem *Favoritos* del NavigationDrawer.
  - Conexión reactiva entre componentes mediante *State Hoisting* (elevación de estado en `AppNavegacion.kt`): al marcar o desmarcar un producto como favorito desde su `DropdownMenu`, el badge del Drawer se actualiza en tiempo real y muestra feedback mediante `Snackbar`.

---

## 📋 Preguntas de Reflexión

1. **¿Por qué el DropdownMenu se declara dentro de un Box junto al ícono que lo activa, y no en cualquier parte de la pantalla?**  
   Porque en Jetpack Compose el componente `DropdownMenu` posiciona su ventana emergente tomando como anclaje (*anchor*) el contenedor padre inmediato. Al situarlo dentro del mismo `Box` que aloja el `IconButton`, el menú aparece adyacente al ícono de 3 puntos de esa tarjeta específica.

2. **¿Qué diferencia de alcance hay entre las opciones del DropdownMenu y las del NavigationDrawer?**  
   El `DropdownMenu` posee un alcance **local y contextual** (sus opciones afectan exclusivamente a un ítem o producto en particular), mientras que el `NavigationDrawer` posee un alcance **global** a nivel de aplicación (gestiona el enrutamiento general entre las distintas pantallas).

3. **¿Cómo tuviste que estructurar tu código para que el contador de favoritos del drawer "se entere" de lo que pasa en el DropdownMenu de cada producto?**  
   Se aplicó la técnica de **State Hoisting** (elevación de estado). La lista o conjunto de IDs de productos favoritos se centralizó en `AppNavegacion`, pasando el conteo `.size` hacia `AppDrawer` y la función mutadora (`onToggleFavorito`) hacia `CatalogoScreen` y `TarjetaProducto`.

4. **¿Qué tuviste que corregir del código que te generó la IA para la mejora del badge de favoritos?**  
   Se corrigió la propuesta inicial de superposición manual con `Box` para emplear el slot nativo `badge = { ... }` que ofrece `NavigationDrawerItem` en Material 3, garantizando el cumplimiento de las guías de diseño y márgenes correctos de Material You.
