package com.example.clinicasalud.model

data class Medico(
    val id: Int,
    val nombre: String,
    val especialidad: String,
    val calificacion: Double,
    val resenas: Int,
    val descripcion: String
)

data class Cita(
    val id: Int,
    val medico: Medico,
    val fecha: String,
    val hora: String,
    val estado: String // "Confirmada", "Completada"
)

val especialidades = listOf("Cardiología", "Pediatría", "Dermatología", "Medicina General")

val medicosMuestra = listOf(
    Medico(1, "Dra. Ana Torres", "Cardiología", 4.9, 128, "Especialista en arritmias e hipertensión, formación en la Clínica Mayo."),
    Medico(2, "Dr. Luis Vega", "Pediatría", 4.7, 85, "Atención integral para niños y adolescentes con 10 años de experiencia."),
    Medico(3, "Dra. Rosa Díaz", "Dermatología", 4.8, 200, "Experta en el cuidado de la piel y tratamientos láser."),
    Medico(4, "Dr. Jorge Paz", "Medicina General", 4.6, 150, "Atención médica preventiva y diagnóstico general.")
)
