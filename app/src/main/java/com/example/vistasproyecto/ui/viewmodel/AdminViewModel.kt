package com.example.vistasproyecto.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vistasproyecto.data.api.RetrofitClient
import com.example.vistasproyecto.data.model.Juego
import com.example.vistasproyecto.data.model.SolicitudJuego
import com.example.vistasproyecto.data.repository.JuegosRepository
import com.example.vistasproyecto.data.repository.SolicitudesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AdminViewModel : ViewModel() {

    private val apiService = RetrofitClient.apiService
    private val solicitudesRepository = SolicitudesRepository(apiService)
    private val juegosRepository = JuegosRepository(apiService)

    private val _solicitudes = MutableStateFlow<List<SolicitudJuego>>(emptyList())
    val solicitudes: StateFlow<List<SolicitudJuego>> = _solicitudes

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _successMessage = MutableStateFlow<String?>(null)
    val successMessage: StateFlow<String?> = _successMessage

    init {
        cargarSolicitudes()
    }

    fun cargarSolicitudes() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = solicitudesRepository.getSolicitudes()
                _solicitudes.value = response
            } catch (e: Exception) {
                _error.value = "Error al cargar solicitudes: ${e.message}"
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun aceptarSolicitud(solicitud: SolicitudJuego) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val nuevoJuego = Juego(
                    titulo = solicitud.tituloJuego,
                    genero = solicitud.genero,
                    anoLanzamiento = solicitud.anoLanzamiento,
                    descripcion = solicitud.descripcion,
                    imagenUrl = solicitud.imagenUrl ?: ""
                )
                
                // 1. Creamos el juego en la colección principal
                juegosRepository.createJuego(nuevoJuego)

                // 2. Si se creó correctamente, eliminamos la solicitud
                solicitud.id?.let { id ->
                    solicitudesRepository.deleteSolicitud(id)
                    // Actualizamos la lista local
                    _solicitudes.value = _solicitudes.value.filter { it.id != id }
                    _successMessage.value = "Juego '${solicitud.tituloJuego}' aprobado con éxito."
                }
            } catch (e: Exception) {
                _error.value = "Error al procesar la aprobación: ${e.message}"
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun rechazarSolicitud(id: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                solicitudesRepository.deleteSolicitud(id)
                _solicitudes.value = _solicitudes.value.filter { it.id != id }
                _successMessage.value = "Solicitud rechazada."
            } catch (e: Exception) {
                _error.value = "Error al rechazar: ${e.message}"
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearMessages() {
        _error.value = null
        _successMessage.value = null
    }
}
