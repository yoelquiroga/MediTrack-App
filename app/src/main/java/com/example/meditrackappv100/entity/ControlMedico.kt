package com.example.meditrackappv100.entity

data class ControlMedico(
    val id_con_med: Int,
    val nombres_apellidos: String,
    val edad: Int,
    val peso: Double,
    val altura: Double,
    val presion_arterial: String,
    val comentario: String,
    val imc: Double,
    val fecha_registro: String
)