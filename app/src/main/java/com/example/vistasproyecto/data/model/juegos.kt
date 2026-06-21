package com.example.vistasproyecto.data.model

import com.google.gson.annotations.SerializedName

data class Juego(
    @SerializedName("id") val id: String? = null,
    @SerializedName("titulo") val titulo: String,
    @SerializedName("genero") val genero: String,
    @SerializedName("ano_lanzamiento") val anoLanzamiento: String,
    @SerializedName("descripcion") val descripcion: String,
    @SerializedName("imagen_url") val imagenUrl: String,
    @SerializedName("calificacion_promedio") val calificacionPromedio: Double = 0.0
)