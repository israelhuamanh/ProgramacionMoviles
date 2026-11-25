# Registro de asistencia de IA

La herramienta utilizada fue **Codex**. El panel de Gemini en Android Studio solicitó iniciar sesión en Google. La tabla recoge la petición real y las decisiones tomadas durante su implementación.

| Prompt que escribí | Qué respondió la IA | Qué cambié o descarté | Por qué |
| --- | --- | --- | --- |
| «Actua como developer senior de android studio experto en UI/UX, necesito que me ayudes hacer esta tarea de android studio, para ello sigue todas las indicaciones que piden en el documento. Es importante que sigas todas las indicaciones. Mi profesor dice que podemos usar IA pra cualquier fase , asi que hazlo todo tu solo» | Codex implementó la app y eligió tres mejoras del laboratorio: borrado, búsqueda local y encabezado Accept. | El borrado comprueba el estado HTTP antes de retirar la tarjeta. La búsqueda usa los datos ya descargados. El encabezado se incorpora en un interceptor común. | Así se mantiene la separación entre pantalla, estado de presentación y red. Cada mejora se puede comprobar por separado. |

## Decisiones y comprobaciones

- **Eliminar.** La pantalla pide confirmación. Retrofit devuelve `Response<Unit>`; el repositorio comprueba `isSuccessful` y convierte el fallo en `Result.failure`. Solo después del éxito se retira el id de la lista en memoria. En el emulador, cancelar mantuvo la publicación. Confirmar envió `DELETE posts/2` y mostró «Publicación eliminada». JSONPlaceholder vuelve a ofrecer el recurso al recargar.
- **Buscar.** El ViewModel combina la lista del usuario seleccionado con la consulta e ignora mayúsculas al comparar títulos. En la prueba, una consulta sin resultados mostró «Sin coincidencias» y «QUI EST ESSE» encontró «qui est esse». El número de GET se mantuvo al escribir.
- **Solicitar JSON.** El interceptor establece una única cabecera `Accept: application/json` antes de registrar la petición. Logcat mostró el encabezado en GET y DELETE. El registro de cuerpos está limitado a compilaciones de depuración.

Para explicar el borrado en la sustentación, sigue el recorrido desde la confirmación en el detalle hasta la actualización del muro. El botón transmite el id, el ViewModel coordina la corrutina y el repositorio interpreta la respuesta HTTP. Una pérdida de red conserva la publicación y ofrece reintentar.
