package com.example.vistasproyecto.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vistasproyecto.data.api.RetrofitClient
import com.example.vistasproyecto.data.model.SolicitudJuego
import com.example.vistasproyecto.data.repository.SolicitudesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SolicitudesViewModel : ViewModel() {
    private val repository = SolicitudesRepository(RetrofitClient.apiService)

    private val _solicitudes = MutableStateFlow<List<SolicitudJuego>>(emptyList())
    val solicitudes: StateFlow<List<SolicitudJuego>> = _solicitudes

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    // Estado de éxito para notificar a la UI
    private val _success = MutableStateFlow(false)
    val success: StateFlow<Boolean> = _success

    init {
        fetchSolicitudes()
    }

    fun fetchSolicitudes() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _solicitudes.value = repository.getSolicitudes()
                _error.value = null
            } catch (e: Exception) {
                e.printStackTrace()
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Envía solicitud con callbacks de resultado
    fun addSolicitud(
        usuarioId: String,
        tituloJuego: String,
        genero: String,
        anoLanzamiento: String,
        descripcion: String,
        imagenUrl: String,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _success.value = false
            try {
                val nuevaSolicitud = SolicitudJuego(
                    usuarioId = usuarioId,
                    tituloJuego = tituloJuego,
                    genero = genero,
                    anoLanzamiento = anoLanzamiento,
                    descripcion = descripcion,
                    imagenUrl = imagenUrl
                )
                repository.createSolicitud(nuevaSolicitud)
                fetchSolicitudes()
                _success.value = true
                onSuccess()
            } catch (e: Exception) {
                val msg = e.message ?: "Error al enviar la solicitud"
                _error.value = msg
                onError(msg)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearSuccess() {
        _success.value = false
    }
}
