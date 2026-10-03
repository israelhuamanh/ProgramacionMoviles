# Registro de Prompts y Asistencia de Inteligencia Artificial

Proyecto: **Mi Bodega — App Cliente**  
Rama: `mejora-ia`  
Objetivo: Implementación del buscador en tiempo real en la pantalla de Inicio combinado con el filtro de categorías.

---

## 1. Prompt Inicial: Análisis y Requerimientos del Buscador Reactivo

### Prompt Enviado a la IA:
> "Actúa como un desarrollador senior de Android con Jetpack Compose. En la pantalla `InicioScreen.kt` de la app Mi Bodega, actualmente tenemos un campo de búsqueda estático y un filtro de categorías horizontal (`LazyRow`).
> Necesito implementar un buscador en tiempo real tal que:
> 1. A medida que el usuario escribe en el `OutlinedTextField`, la lista de productos se filtre dinámicamente de forma reactiva por nombre o descripción sin requerir presionar ningún botón de búsqueda ni 'Enter'.
> 2. Este filtro de búsqueda debe combinarse y coexistir armónicamente con la categoría seleccionada ('Todos', 'Bebidas', 'Abarrotes', 'Snacks') y con el ordenamiento por precio previamente implementado.
> 3. Incluya un botón para limpiar el texto rápidamente (ícono de 'X' o 'Clear') cuando haya texto escrito.
> 4. Si la búsqueda no arroja ningún resultado coincidente, debe mostrarse una vista amigable de retroalimentación indicando que no se encontraron productos.
> 
> Proporciona la solución en Kotlin y Compose respetando la arquitectura de componentes existente."

### Análisis de la Solución Propuesta por la IA:
* Se define un estado recordado mutable `var queryBusqueda by remember { mutableStateOf("") }`.
* Se aplica una operación de filtrado sobre la colección en memoria:
  ```kotlin
  val productosFiltrados = productos.filter { prod ->
      val coincideCategoria = categoriaSeleccionada == "Todos" || prod.categoria == categoriaSeleccionada
      val coincideTexto = queryBusqueda.isBlank() || 
          prod.nombre.contains(queryBusqueda, ignoreCase = true) ||
          prod.descripcion.contains(queryBusqueda, ignoreCase = true)
      coincideCategoria && coincideTexto
  }
  ```
* Se mantiene el encadenamiento con el orden de precios seleccionado (`menor a mayor`, `mayor a menor`).
* Se agrega el `trailingIcon` condicional en el buscador para limpiar la consulta mediante `IconButton(onClick = { queryBusqueda = "" })`.

---

## 2. Prompt Secundario: Refinamiento de Experiencia de Usuario y Casos Extremos

### Prompt Enviado a la IA:
> "¿Qué consideraciones de UX y manejo de casos extremos recomiendas al combinar el buscador por texto con chips de categoría y orden de precios en una app de bodega?"

### Respuesta de la IA y Mejoras Incorporadas:
1. **Contador dinámico de resultados:** Mostrar el número de productos encontrados en el encabezado (ej. *"Resultados (2)"*) cuando hay un término de búsqueda activo, para orientar al cliente.
2. **Vista de Estado Vacío (Empty State):** Cuando el término buscado no coincide con ningún producto de la categoría actual, mostrar el ícono `Icons.Default.SearchOff` con el mensaje *"No se encontraron productos"* y una sugerencia de acción.
3. **Botón de Limpieza Rápida:** Facilitar el reseteo del buscador con un solo toque mediante `Icons.Default.Clear`.
4. **Respeto a la persistencia del ordenamiento:** El orden seleccionado (por defecto, menor a mayor, mayor a menor) se conserva automáticamente sobre los resultados filtrados.

---

## 3. Verificación de Criterios de Evaluación

- [x] **Filtrado en tiempo real:** Cumplido. La lista reacciona a cada carácter tipeado en el `OutlinedTextField`.
- [x] **Combinación con categorías:** Cumplido. Si se selecciona "Bebidas" y se escribe "coca", solo busca dentro de bebidas.
- [x] **Commits requeridos:** 3 commits descriptivos en la rama `mejora-ia`.
- [x] **Documentación completa:** Archivo `PROMPTS.md` con los prompts detallados, código y análisis.
