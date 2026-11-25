package com.tecsup.avisoscampus.glance

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.glance.GlanceId
import androidx.glance.GlanceTheme
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionParametersOf
import androidx.glance.action.actionStartActivity
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.provideContent
import com.tecsup.avisoscampus.MainActivity
import com.tecsup.avisoscampus.R
import com.tecsup.avisoscampus.data.AvisosRepositoryMemoria
import com.tecsup.avisoscampus.glance.layout.ExpressiveToolBarButton
import com.tecsup.avisoscampus.glance.layout.ExpressiveToolbarLayout
import com.tecsup.avisoscampus.model.Origen
import com.tecsup.avisoscampus.notificacion.Notificador

/**
 * Widget expresivo con forma de galleta (Expressive Toolbar Canonical Layout) para AvisosCampus:
 * - Botón central (hero): Mostrar aviso inmediato ("Tarea lista").
 * - Esquina superior izquierda: Lanzar recordatorio urgente ("Recordatorio").
 * - Esquina superior derecha: Registrar aviso de Modo avión.
 * - Esquina inferior izquierda: Registrar aviso de Cargador.
 * - Esquina inferior derecha: Abrir el panel completo de AvisosCampus (MainActivity).
 */
class ExpressiveToolbarAppWidget : GlanceAppWidget() {
    override val sizeMode: SizeMode = SizeMode.Exact

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            GlanceTheme {
                WidgetContent()
            }
        }
    }

    @Composable
    fun WidgetContent() {
        ExpressiveToolbarLayout(
            centerButton = ExpressiveToolBarButton(
                iconRes = R.drawable.ic_campana,
                contentDescription = "Mostrar aviso inmediato",
                onClick = actionRunCallback<DispararAvisoAction>(
                    actionParametersOf(DispararAvisoAction.KEY_ORIGEN to Origen.BOTON.name)
                )
            ),
            cornerButtons = listOf(
                ExpressiveToolBarButton(
                    iconRes = R.drawable.ic_temporizador,
                    contentDescription = "Enviar recordatorio urgente",
                    onClick = actionRunCallback<DispararAvisoAction>(
                        actionParametersOf(DispararAvisoAction.KEY_ORIGEN to Origen.RECORDATORIO.name)
                    )
                ),
                ExpressiveToolBarButton(
                    iconRes = R.drawable.ic_modo_avion,
                    contentDescription = "Simular aviso de modo avión",
                    onClick = actionRunCallback<DispararAvisoAction>(
                        actionParametersOf(DispararAvisoAction.KEY_ORIGEN to Origen.MODO_AVION.name)
                    )
                ),
                ExpressiveToolBarButton(
                    iconRes = R.drawable.ic_cargador,
                    contentDescription = "Simular aviso de cargador",
                    onClick = actionRunCallback<DispararAvisoAction>(
                        actionParametersOf(DispararAvisoAction.KEY_ORIGEN to Origen.CARGADOR.name)
                    )
                ),
                ExpressiveToolBarButton(
                    iconRes = R.drawable.ic_notificacion,
                    contentDescription = "Abrir AvisosCampus",
                    onClick = actionStartActivity<MainActivity>()
                )
            )
        )
    }
}

class ExpressiveToolbarAppWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = ExpressiveToolbarAppWidget()
}

/**
 * Callback sin parámetros en el constructor (requerido por Glance/R8) que registra el aviso
 * en el repositorio compartido y muestra la notificación local desde el widget.
 */
class DispararAvisoAction : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        Notificador.crearCanal(context)
        val nombreOrigen = parameters[KEY_ORIGEN] ?: Origen.BOTON.name
        val origen = runCatching { Origen.valueOf(nombreOrigen) }.getOrDefault(Origen.BOTON)
        val (titulo, texto) = when (origen) {
            Origen.BOTON -> "Tarea lista" to "Tu entrega fue registrada desde el widget"
            Origen.RECORDATORIO -> "Recordatorio" to "Revisa tu laboratorio pendiente"
            Origen.MODO_AVION -> "Modo avión" to "Evento consultado desde el widget"
            Origen.CARGADOR -> "Cargador" to "Estado registrado desde el widget"
        }
        val aviso = AvisosRepositoryMemoria.registrar(titulo, texto, origen)
        Notificador.mostrar(context, aviso)
    }

    companion object {
        val KEY_ORIGEN = ActionParameters.Key<String>("origen")
    }
}
