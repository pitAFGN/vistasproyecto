package com.example.vistasproyecto.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vistasproyecto.data.api.RetrofitClient
import com.example.vistasproyecto.data.model.Juego
import com.example.vistasproyecto.data.repository.JuegosRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class JuegosViewModel : ViewModel() {
    private val repository = JuegosRepository(RetrofitClient.apiService)

    private val _juegos = MutableStateFlow<List<Juego>>(emptyList())
    val juegos: StateFlow<List<Juego>> = _juegos

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        fetchJuegos()
    }

    fun fetchJuegos() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _juegos.value = repository.getJuegos()
                _error.value = null
            } catch (e: Exception) {
                e.printStackTrace()
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addJuego(titulo: String, genero: String, anoLanzamiento: String, descripcion: String, imagenUrl: String) {
        viewModelScope.launch {
            try {
                val nuevoJuego = Juego(
                    titulo = titulo,
                    genero = genero,
                    anoLanzamiento = anoLanzamiento,
                    descripcion = descripcion,
                    imagenUrl = imagenUrl
                )
                repository.createJuego(nuevoJuego)
                fetchJuegos()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun updateJuego(id: String, titulo: String, genero: String, anoLanzamiento: String, descripcion: String, imagenUrl: String) {
        viewModelScope.launch {
            try {
                val juegoActualizado = Juego(
                    id = id,
                    titulo = titulo,
                    genero = genero,
                    anoLanzamiento = anoLanzamiento,
                    descripcion = descripcion,
                    imagenUrl = imagenUrl
                )
                repository.updateJuego(id, juegoActualizado)
                fetchJuegos()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun deleteJuego(id: String) {
        viewModelScope.launch {
            try {
                repository.deleteJuego(id)
                fetchJuegos()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}