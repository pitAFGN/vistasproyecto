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

    private val _calificaciones = MutableStateFlow<List<Calificacion>>(emptyList())
    val calificaciones: StateFlow<List<Calificacion>> = _calificaciones

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun fetchCalificacionesPorJuego(juegoId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _calificaciones.value = repository.getCalificacionesPorJuego(juegoId)
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addCalificacion(usuarioId: String, juegoId: String, puntaje: Int, comentario: String) {
        viewModelScope.launch {
            try {
                val nuevaCalificacion = Calificacion(
                    usuarioId = usuarioId,
                    juegoId = juegoId,
                    puntaje = puntaje,
                    comentario = comentario
                )
                repository.createCalificacion(nuevaCalificacion)
                fetchCalificacionesPorJuego(juegoId) // Refresca las notas de ese juego específico
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}