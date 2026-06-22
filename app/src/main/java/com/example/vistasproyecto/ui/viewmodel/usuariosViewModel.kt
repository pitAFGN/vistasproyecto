package com.example.vistasproyecto.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vistasproyecto.data.SessionManager
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

    fun registrarUsuario(
        usuario: String,
        email: String,
        contrasena: String,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // El correo es único: verificamos que no exista ya una cuenta con este email
                val emailNormalizado = email.trim().lowercase()
                val coincidencias = repository.getUserByEmail(emailNormalizado)
                val yaExiste = coincidencias.any { it.email.trim().lowercase() == emailNormalizado }

                if (yaExiste) {
                    onError("Ya existe una cuenta registrada con ese correo.")
                    return@launch
                }

                val nuevoUsuario = User(
                    usuario = usuario,
                    email = email.trim(),
                    contrasena = contrasena,
                    puntosXp = 0
                )
                repository.createUser(nuevoUsuario)
                fetchUsuarios()
                onSuccess()
            } catch (e: Exception) {
                val msg = e.message ?: "Error al registrar el usuario"
                _error.value = msg
                onError(msg)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loginUsuario(
        email: String,
        contrasena: String,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val coincidencias = repository.getUserByEmail(email)
                val usuario = coincidencias.firstOrNull { it.email == email }
                when {
                    usuario == null -> onError("No existe una cuenta con ese email.")
                    usuario.contrasena != contrasena -> onError("Contraseña incorrecta.")
                    else -> {
                        _error.value = null
                        SessionManager.login(usuario)   // ← guarda la sesión
                        onSuccess()
                    }
                }
            } catch (e: Exception) {
                val msg = e.message ?: "Error al conectar con el servidor"
                _error.value = msg
                onError(msg)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun logout() {
        SessionManager.logout()
    }
}
