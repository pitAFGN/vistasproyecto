package com.example.vistasproyecto.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vistasproyecto.data.api.RetrofitClient
import com.example.vistasproyecto.data.model.User
import com.example.vistasproyecto.data.repository.UsuariosRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UsuariosViewModel : ViewModel() {
    private val repository = UsuariosRepository(RetrofitClient.apiService)

    private val _usuarios = MutableStateFlow<List<User>>(emptyList())
    val usuarios: StateFlow<List<User>> = _usuarios

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        fetchUsuarios()
    }

    fun fetchUsuarios() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _usuarios.value = repository.getUsers()
                _error.value = null
            } catch (e: Exception) {
                e.printStackTrace()
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Utilizado para el Formulario de Registro en Vortex
    fun registrarUsuario(usuario: String, email: String, contrasena: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val nuevoUsuario = User(
                    usuario = usuario,
                    email = email,
                    contrasena = contrasena,
                    puntosXp = 0
                )
                repository.createUser(nuevoUsuario)
                fetchUsuarios()
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}