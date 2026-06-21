package com.example.vistasproyecto.data.repository

import com.example.vistasproyecto.data.api.VortexApiService
import com.example.vistasproyecto.data.model.SolicitudJuego

class SolicitudesRepository(private val apiService: VortexApiService) {
    suspend fun getSolicitudes() = apiService.getSolicitudes()
    suspend fun getSolicitud(id: String) = apiService.getSolicitud(id)
    suspend fun createSolicitud(solicitud: SolicitudJuego) = apiService.createSolicitud(solicitud)
    suspend fun updateSolicitud(id: String, solicitud: SolicitudJuego) = apiService.updateSolicitud(id, solicitud)
    suspend fun deleteSolicitud(id: String) = apiService.deleteSolicitud(id)
}
