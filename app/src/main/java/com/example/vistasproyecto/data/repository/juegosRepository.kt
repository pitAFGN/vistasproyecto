package com.example.vistasproyecto.data.repository

import com.example.vistasproyecto.data.api.VortexApiService
import com.example.vistasproyecto.data.model.Juego

class JuegosRepository(private val apiService: VortexApiService) {
    suspend fun getJuegos() = apiService.getJuegos()
    suspend fun getJuego(id: String) = apiService.getJuego(id)
    suspend fun createJuego(juego: Juego) = apiService.createJuego(juego)
    suspend fun updateJuego(id: String, juego: Juego) = apiService.updateJuego(id, juego)
    suspend fun deleteJuego(id: String) = apiService.deleteJuego(id)
}