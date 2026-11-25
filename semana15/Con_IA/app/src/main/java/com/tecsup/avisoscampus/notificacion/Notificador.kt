package com.tecsup.avisoscampus.notificacion

import com.tecsup.avisoscampus.model.Origen
import android.app.PendingIntent
import android.content.Intent
import com.tecsup.avisoscampus.MainActivity
import android.annotation.SuppressLint
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.tecsup.avisoscampus.R
import com.tecsup.avisoscampus.model.Aviso
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
object Notificador {
    const val CANAL = "avisos"
    const val URGENTES = "urgentes"
    fun crearCanal(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(NotificationChannel(CANAL, "Avisos", NotificationManager.IMPORTANCE_DEFAULT))
            manager.createNotificationChannel(NotificationChannel(URGENTES, "Urgentes", NotificationManager.IMPORTANCE_HIGH))
        }
    }

    fun tienePermiso(context: Context): Boolean =
        Build.VERSION.SDK_INT < 33 || ContextCompat.checkSelfPermission(context,
            Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED

    @SuppressLint("MissingPermission")
    fun mostrar(context: Context, aviso: Aviso): Boolean {
        if (!tienePermiso(context)) return false
        val intent = Intent(context, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendiente = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        val canal = if (aviso.origen == Origen.RECORDATORIO) URGENTES else CANAL
        val notificacion = NotificationCompat.Builder(context, canal)
            .setSmallIcon(R.drawable.ic_notificacion)
            .setContentTitle(aviso.titulo)
            .setContentText(aviso.texto)
            .setStyle(NotificationCompat.BigTextStyle().bigText(detalle(aviso)))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendiente)
            .setAutoCancel(true)
            .build()
        return try {
            NotificationManagerCompat.from(context).notify(aviso.id, notificacion)
            true
        } catch (_: SecurityException) {
            false
        }
    }

    private fun detalle(aviso: Aviso): String = when (aviso.origen) {
        Origen.BOTON -> "${aviso.texto}. Puedes consultar la hora de registro en el historial de AvisosCampus."
        Origen.RECORDATORIO -> "${aviso.texto}. Abre tu práctica, revisa las indicaciones y comprueba que tu entrega esté completa."
        Origen.CARGADOR -> "Cargador: ${aviso.texto}. El cambio se registró a las ${aviso.hora}."
        Origen.MODO_AVION -> "Modo avión: ${aviso.texto}. El cambio se registró a las ${aviso.hora}."
    }
}
