# Documentación de prompts (Fase 2 y 3)

A continuación se detalla el prompt utilizado en la Fase 2 y 3 con la IA para generar la mejora funcional y la refactorización con Programación Orientada a Objetos (POO).

## Objetivo del Proyecto
Desarrollar una aplicación en Android Studio usando Kotlin para "Clínica Salud+", que permita a los usuarios visualizar médicos, agendar citas médicas, consultar su historial y cancelar citas.

## Contexto de la Tarea
El proyecto inicial (Fase 1) consistió en construir la UI de la aplicación de Clínica usando Jetpack Compose y estado local (`remember/mutableStateOf`). En la Fase 2 y Fase 3, se requiere refactorizar el código para usar principios de Programación Orientada a Objetos (POO) e incorporar una mejora funcional relevante asistida por IA.

## Prompt Utilizado

```text
Actúa como un desarrollador experto en Kotlin y Android Studio (Jetpack Compose).
Tengo una aplicación de reserva de citas médicas llamada "Clínica Salud+".
Actualmente las clases de dominio son simplemente `data class`.
Requiero que:
1. Refactorices el modelo de datos para aplicar Programación Orientada a Objetos (POO).
   - Crea clases base (ej. `Persona`, `ServicioMedico`) y usa herencia para `PersonalMedico` y `Medico`.
   - Implementa polimorfismo (ej. un método `obtenerResumen()` en `ServicioMedico` sobreescrito en `Cita`).
   - Usa encapsulamiento para proteger propiedades mutables como el `_estado` de la cita y la `_descripcion` del médico.
   - Utiliza abstracción donde corresponda.
2. Agrega una nueva funcionalidad (Fase 2):
   - En la pantalla de "Mis citas", si la cita tiene estado "Confirmada", muestra un botón de "Cancelar".
   - Al hacer clic en el botón de cancelar, muestra un `AlertDialog` pidiendo confirmación al usuario ("¿Estás seguro de que deseas cancelar la cita?").
   - Si el usuario confirma, cambia el estado de la cita a "Cancelada" y actualiza la UI.

Considera las restricciones del proyecto:
- Todo debe ser en Kotlin con Jetpack Compose.
- No uses MVVM ni ViewModel, maneja el estado localmente con `remember`/`mutableStateOf`.
- Mantén la estructura de navegación actual.
```

## Correcciones Realizadas al Código Generado
La IA generó correctamente las clases base abstractas y la lógica del `AlertDialog`. Sin embargo, fue necesario ajustar el estado de la clase `Cita` para que la propiedad `_estado` utilizara `mutableStateOf(estadoInicial)` en lugar de ser un simple `String`. De esta forma, Jetpack Compose puede observar el cambio de estado de "Confirmada" a "Cancelada" y recomponer la interfaz automáticamente (en la tarjeta de `PantallaMisCitas`) sin necesidad de recargar toda la lista. También se ajustaron los modificadores para dar un color rojo al botón y al texto de estado cancelado.
