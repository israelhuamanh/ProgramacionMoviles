package com.huaman.registrodenotas.model

data class Curso(
    val nombre: String,
    val peso: Double,
    var nota: Double = 0.0
) {
    fun calcularAporte(): Double {
        return nota * peso
    }
}
