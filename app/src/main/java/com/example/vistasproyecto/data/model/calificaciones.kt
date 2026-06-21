package com.example.vistasproyecto.data.model

import com.google.gson.annotations.SerializedName

data class Calificacion(
    @SerializedName("id") val id: String? = null,
    @SerializedName("usuario_id") val usuarioId: String,
    @SerializedName("juego_id") val juegoId: String,
    @SerializedName("puntaje") val puntaje: Int,
    @SerializedName("comentario") val comentario: String
)