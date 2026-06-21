package com.example.vistasproyecto.data.repository

import com.example.vistasproyecto.data.api.VortexApiService
import com.example.vistasproyecto.data.model.Calificacion

class CalificacionesRepository(private val apiService: VortexApiService) {
    suspend fun getCalificaciones() = apiService.getCalificaciones()
    suspend fun createCalificacion(calificacion: Calificacion) = apiService.createCalificacion(calificacion)
    suspend fun getCalificacionesPorJuego(juegoId: String) = apiService.getCalificacionesPorJuego(juegoId)
}
