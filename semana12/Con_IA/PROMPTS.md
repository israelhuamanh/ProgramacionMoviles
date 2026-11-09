# Registro de Consultas a la IA - Laboratorio 11-12 (Mejora con IA)

Este archivo documenta las consultas realizadas al asistente de Inteligencia Artificial durante la **Fase 2 (Mejora con IA)**, siguiendo la plantilla oficial de la guía del laboratorio.

---

## Consulta 1
- Fecha: 08/11/2026
- Herramienta: Gemini / Claude (Asistente de IA)
- Prompt:
  > "Tengo un `ListaViewModel(private val repo: ItemRepository, private val notificador: Notificador)` que expone `estado: StateFlow<UiState<List<Item>>>` y `aviso: StateFlow<String?>`. Necesito implementar al menos 3 pruebas unitarias en JUnit 4 usando `kotlinx-coroutines-test` con un `FakeItemRepository` y un `FakeNotificador` sin usar librerías externas de mocking para probar: (1) sincronización exitosa y disparo de notificación, (2) error sin internet cuando Room está vacío, y (3) modo sin conexión mostrando datos guardados y el aviso."
- Qué respondió (resumen):
  La IA propuso configurar `Dispatchers.setMain(StandardTestDispatcher())` en `@Before` y `Dispatchers.resetMain()` en `@After`, además de crear clases falsas `FakeItemRepository` (basada en un `MutableStateFlow<List<Item>>` en memoria) y `FakeNotificador` (que registra el número de llamadas y el último texto enviado). Usó `runTest` y `advanceUntilIdle()` para esperar que las corrutinas de `viewModelScope` terminen antes de evaluar las aserciones.
- Qué usamos / qué cambiamos y por qué:
  Usamos la estructura de `FakeItemRepository` y `FakeNotificador`, y agregamos una cuarta prueba unitaria adicional (`alternarFavorito y sincronizar conserva los favoritos marcados`) para verificar también el requisito R07/PA-10 en las pruebas automatizadas.
- Qué aprendimos:
  Aprendimos por qué declarar `ItemRepository` y `Notificador` como interfaces (`interface`) facilita las pruebas unitarias: el `ListaViewModel` se puede probar en milisegundos en la JVM sin necesitar un emulador Android, base de datos Room real ni conexión a internet.

---

## Consulta 2
- Fecha: 08/11/2026
- Herramienta: Gemini / Claude (Asistente de IA)
- Prompt:
  > "En `ListaViewModel` recibo `repo.items: Flow<List<Item>>` desde Room. Quiero agregar en la Fase 2 un buscador en tiempo real por título o subtítulo de publicación y un contador de favoritos para mostrar un `Badge` en el `TopAppBar` de `ListaScreen` sin romper la regla de que Room es la única fuente de verdad ni llamar a Retrofit al buscar."
- Qué respondió (resumen):
  La IA sugirió declarar un `_busqueda = MutableStateFlow("")` dentro de `ListaViewModel` y usar el operador `combine(repo.items, _busqueda)` de Kotlin Coroutines Flow para filtrar reactivamente la lista emitida por Room antes de asignarla a `UiState.Exito`. Asimismo, recomendó recolectar `repo.favoritos` para exponer `totalFavoritos: StateFlow<Int>` y mostrarlo dentro de un `BadgedBox` de Material 3.
- Qué usamos / qué cambiamos y por qué:
  Incorporamos `combine(repo.items, _busqueda)` en `ListaViewModel`, pero ajustamos la condición `if (listaOriginal.isNotEmpty() || refrescoTerminado)` sobre la lista original de Room antes del filtro para evitar que, durante la primera apertura de la app, se muestre prematuramente una lista vacía antes de que termine `refrescar()`.
- Qué aprendimos:
  Comprendimos cómo combinar flujos reactivos (`StateFlow` + `Flow` de Room) con `combine` respetando el patrón MVVM y manteniendo toda la lógica de presentación en el `ViewModel` mientras la UI (`ListaScreen`) solo observa estados con `collectAsStateWithLifecycle()`.

---

## Consulta 3
- Fecha: 08/11/2026
- Herramienta: Gemini / Claude (Asistente de IA)
- Prompt:
  > "¿Cómo debo implementar `NotificadorAndroid` con `NotificationCompat` y el permiso `POST_NOTIFICATIONS` en Android 13+ (API 33+) para que al tocar la notificación se abra `MainActivity` y que si el usuario niega el permiso (PA-14) la aplicación no falle ni se cierre?"
- Qué respondió (resumen):
  La IA explicó que se debe crear un `NotificationChannel` en Android 8.0+ (API 26+), verificar `ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)` antes de invocar `NotificationManagerCompat.from(context).notify(...)`, configurar un `PendingIntent` con `FLAG_IMMUTABLE` apuntando a `MainActivity`, y solicitar el permiso en tiempo de ejecución con `registerForActivityResult(ActivityResultContracts.RequestPermission())`.
- Qué usamos / qué cambiamos y por qué:
  Usamos la implementación en `NotificadorAndroid.kt` inyectada a través de la interfaz `Notificador` en `AppContainer.kt`, añadiendo además un bloque `try / catch (SecurityException)` y el retorno temprano cuando el permiso está denegado para cumplir estrictamente con la prueba de aceptación PA-14 y la regla MVVM de no pasar `Context` al `ViewModel`.
- Qué aprendimos:
  Aprendimos a desacoplar servicios propios del sistema operativo Android (`Context`, `NotificationManager`, permisos de runtime) detrás de una interfaz de dominio/notificación (`Notificador`), manteniendo el `ViewModel` libre de dependencias de `android.*`.
