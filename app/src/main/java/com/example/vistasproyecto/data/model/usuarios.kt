package com.example.vistasproyecto.data.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id") val id: String? = null,
    @SerializedName("usuario") val usuario: String,
    @SerializedName("email") val email: String,
    @SerializedName("contrasena") val contrasena: String,
    @SerializedName("puntos_xp") val puntosXp: Int = 0
)