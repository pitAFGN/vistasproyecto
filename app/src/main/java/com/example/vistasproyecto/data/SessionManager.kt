package com.example.vistasproyecto.data

import com.example.vistasproyecto.data.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Singleton que mantiene la sesión del usuario activa durante la app.
 * Al cerrar la app se pierde (no hay persistencia); si se quiere persistencia
 * habría que añadir SharedPreferences o DataStore.
 */
object SessionManager {

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    fun login(user: User) {
        _currentUser.value = user
    }

    fun logout() {
        _currentUser.value = null
    }

    fun isLoggedIn(): Boolean = _currentUser.value != null

    fun getUserId(): String? = _currentUser.value?.id
}
