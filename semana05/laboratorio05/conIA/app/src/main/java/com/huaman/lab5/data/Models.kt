package com.huaman.lab5.data

// Base class to demonstrate OOP
abstract class Person(
    open val id: Int,
    open val name: String,
    open val email: String,
    open val phone: String
) {
    abstract fun getRole(): String
    abstract fun getDetails(): String
}

data class Student(
    override val id: Int,
    override val name: String,
    override val email: String,
    override val phone: String,
    val career: String,
    val cycle: String,
    val bio: String
) : Person(id, name, email, phone) {
    override fun getRole() = "Estudiante"
    override fun getDetails() = "Carrera: $career, Ciclo: $cycle"
}

object DataProvider {
    val students = listOf(
        Student(1, "Israel Huaman Huaman", "israel.huaman@tecsup.edu.pe", "93418696", "Ingeniería de Software", "IV ciclo", "Estudiante destacado con interés en desarrollo Android."),
        Student(2, "Maria Garcia", "maria.garcia@tecsup.edu.pe", "987654321", "Arquitectura", "V ciclo", "Apasionada por el diseño de interiores."),
        Student(3, "Carlos Perez", "carlos.perez@tecsup.edu.pe", "987654322", "Medicina", "VI ciclo", "Dedicado al estudio de la anatomía."),
        Student(4, "Ana Lopez", "ana.lopez@tecsup.edu.pe", "987654323", "Derecho", "VII ciclo", "Interesada en el derecho penal."),
        Student(5, "Luis Ramirez", "luis.ramirez@tecsup.edu.pe", "987654324", "Administración", "VIII ciclo", "Enfocado en finanzas corporativas.")
    )

    fun getStudentById(id: Int): Student? {
        return students.find { it.id == id }
    }
}
