package com.huaman.registrodenotas.model

import kotlin.math.roundToInt

class RegistroAcademico {
    val cursos = listOf(
        Curso("Fundamentos de Programación", 0.20),
        Curso("Programación Orientada a Objetos", 0.25),
        Curso("Programación en Móviles", 0.30),
        Curso("Base de Datos", 0.25)
    )

    fun registrarNota(indice: Int, nota: Double) {
        if (indice in cursos.indices) {
            cursos[indice].nota = nota
        }
    }

    fun calcularPromedioPonderado(): Double {
        return cursos.sumOf { it.calcularAporte() }
    }

    fun calcularPromedioFinal(redondear: Boolean): Double {
        val ponderado = calcularPromedioPonderado()
        return if (redondear) {
            ponderado.roundToInt().toDouble()
        } else {
            ponderado
        }
    }

    fun obtenerObservacion(promedioFinal: Double): ObservacionResultado {
        return when {
            promedioFinal >= 17 -> ObservacionResultado("EXCELENTE", 0xFF2E7D32)
            promedioFinal >= 13 -> ObservacionResultado("APROBADO", 0xFF4CAF50)
            promedioFinal >= 10 -> ObservacionResultado("EN RECUPERACIÓN", 0xFFFF8F00)
            else -> ObservacionResultado("DESAPROBADO", 0xFFD32F2F)
        }
    }
}

data class ObservacionResultado(
    val mensaje: String,
    val colorHex: Long
)
