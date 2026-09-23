package com.example.clinicasalud.model

// Abstracción y Herencia
abstract class Persona(
    open val id: Int,
    open val nombre: String
)

abstract class PersonalMedico(
    override val id: Int,
    override val nombre: String,
    open val calificacion: Double,
    open val resenas: Int
) : Persona(id, nombre) {
    abstract fun obtenerDescripcionProfesional(): String
}

class Medico(
    id: Int,
    nombre: String,
    val especialidad: String,
    calificacion: Double,
    resenas: Int,
    private var _descripcion: String // Encapsulamiento
) : PersonalMedico(id, nombre, calificacion, resenas) {
    
    val descripcion: String
        get() = _descripcion

    // Polimorfismo
    override fun obtenerDescripcionProfesional(): String {
        return "$especialidad - $_descripcion"
    }
}

// Polimorfismo a través de una clase base abstracta
abstract class ServicioMedico {
    abstract val id: Int
    abstract fun obtenerResumen(): String
}

import androidx.compose.runtime.mutableStateOf

class Cita(
    override val id: Int,
    val medico: Medico,
    val fecha: String,
    val hora: String,
    estadoInicial: String // Encapsulamiento con estado mutable
) : ServicioMedico() {

    private val _estado = mutableStateOf(estadoInicial)

    val estado: String
        get() = _estado.value

    override fun obtenerResumen(): String {
        return "Cita con ${medico.nombre} el $fecha a las $hora"
    }
    
    fun cancelarCita() {
        if (_estado.value == "Confirmada") {
            _estado.value = "Cancelada"
        }
    }
}

val especialidades = listOf("Cardiología", "Pediatría", "Dermatología", "Medicina General")

val medicosMuestra = listOf(
    Medico(1, "Dra. Ana Torres", "Cardiología", 4.9, 128, "Especialista en arritmias e hipertensión, formación en la Clínica Mayo."),
    Medico(2, "Dr. Luis Vega", "Pediatría", 4.7, 85, "Atención integral para niños y adolescentes con 10 años de experiencia."),
    Medico(3, "Dra. Rosa Díaz", "Dermatología", 4.8, 200, "Experta en el cuidado de la piel y tratamientos láser."),
    Medico(4, "Dr. Jorge Paz", "Medicina General", 4.6, 150, "Atención médica preventiva y diagnóstico general.")
)
