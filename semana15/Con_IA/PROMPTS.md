# Registro de uso de IA

Se utilizó Codex durante las dos fases, de acuerdo con la autorización comunicada por el estudiante. La columna de respuesta identifica al asistente que intervino. Los fragmentos de los prompts proceden de la conversación de esta entrega.

| Prompt del estudiante | Respuesta de Codex | Decisión aplicada | Criterio técnico |
| --- | --- | --- | --- |
| «Antes de que comiences examina tanto el pdf y la skill que te di» | Identificó los contratos, las dos ramas, las cinco capturas y el requisito de preservar los archivos entregados. | Revisar la existencia de la base antes del primer commit. | La procedencia del código afecta la trazabilidad del laboratorio. |
| «Tienes que hacerlo desde cero creo, no te aparece ningun link del repositorio?» | Reconstruyó una base compilable con los modelos, el repositorio en memoria y la factoría descritos en la guía. | Mantener los nombres de paquetes y las firmas del documento, y documentar la reconstrucción. | El ejercicio se puede seguir commit a commit desde las tarjetas vacías. |
| «Para mejorar el diseño de la aplicacion usaremos esta skill de material expressive 3» | Aplicó una jerarquía de títulos azules, etiqueta textual del permiso, acciones de distinta importancia y colores para tema oscuro. | Conservar las cinco tarjetas en el orden requerido y emplear un contenedor desplazable. | El estado sigue siendo legible sin depender solo del color y la interfaz se adapta al ancho disponible. |
| «sigue todas las indicaciones que piden en el documento» | Seleccionó canales por prioridad, texto expandible y escucha del cargador para la fase 2. | Elegir el canal por origen en el notificador y emitir el cambio de alimentación desde el ViewModel. Mantener la propiedad y liberación del receptor en la pantalla. | El recordatorio puede tener mayor importancia, el aviso conserva su resumen y el ViewModel se mantiene independiente del sistema Android. |

## Para la sustentación

Al activar **Escuchar el cargador**, la pantalla registra un receptor para conexión y desconexión de alimentación. Su única tarea es convertir la acción de Android en un booleano. El ViewModel traduce ese dato en un aviso y pide al repositorio que lo registre. Luego emite el aviso en el mismo flujo que usan el botón y el recordatorio. Al desactivar el interruptor o destruir la composición, la pantalla libera el receptor. Por eso rotar el dispositivo conserva la preferencia sin multiplicar las llamadas.

