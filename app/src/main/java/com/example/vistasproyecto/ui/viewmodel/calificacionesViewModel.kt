package com.example.vistasproyecto.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vistasproyecto.data.api.RetrofitClient
import com.example.vistasproyecto.data.model.Calificacion
import com.example.vistasproyecto.data.repository.CalificacionesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CalificacionesViewModel : ViewModel() {
    private val repository = CalificacionesRepository(RetrofitClient.apiService)

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _success = MutableStateFlow(false)
    val success: StateFlow<Boolean> = _success

    private val _calificaciones = MutableStateFlow<List<Calificacion>>(emptyList())
    val calificaciones: StateFlow<List<Calificacion>> = _calificaciones

    fun fetchCalificaciones(juegoId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _calificaciones.value = repository.getCalificacionesPorJuego(juegoId)
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addCalificacion(usuarioId: String, juegoId: String, puntaje: Int, comentario: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _success.value = false
            try {
                val nuevaCalificacion = Calificacion(
                    usuarioId = usuarioId,
                    juegoId = juegoId,
                    puntaje = puntaje,
                    comentario = comentario
                )
                repository.createCalificacion(nuevaCalificacion)
                _success.value = true
                _error.value = null
                fetchCalificaciones(juegoId) // Actualizar lista tras añadir
            } catch (e: Exception) {
                e.printStackTrace()
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun resetSuccess() {
        _success.value = false
    }
}
