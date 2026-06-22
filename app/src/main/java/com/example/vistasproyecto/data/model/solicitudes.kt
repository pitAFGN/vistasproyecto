package com.example.vistasproyecto.data.model

import com.google.gson.annotations.SerializedName

data class SolicitudJuego(
    @SerializedName("id") val id: String? = null,
    @SerializedName("usuario_id") val usuarioId: String,
    @SerializedName("titulo_juego") val tituloJuego: String,
    @SerializedName("genero") val genero: String,
    @SerializedName("ano_lanzamiento") val anoLanzamiento: String,
    @SerializedName("descripcion") val descripcion: String,
    @SerializedName("imagen_url") val imagenUrl: String? = null,
    @SerializedName("estado") val estado: String = "pendiente"
)