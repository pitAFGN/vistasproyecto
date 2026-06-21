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

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

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

    fun login(email: String, contrasena: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val users = repository.getUserByEmail(email)
                val user = users.firstOrNull()
                if (user != null && user.contrasena == contrasena) {
                    _currentUser.value = user
                    _error.value = null
                    onSuccess()
                } else {
                    _error.value = "Invalid credentials"
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun registrarUsuario(usuario: String, email: String, contrasena: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val nuevoUsuario = User(
                    usuario = usuario,
                    email = email,
                    contrasena = contrasena,
                    puntosXp = 0
                )
                val createdUser = repository.createUser(nuevoUsuario)
                _currentUser.value = createdUser
                fetchUsuarios()
                onSuccess()
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun logout() {
        _currentUser.value = null
    }
}
