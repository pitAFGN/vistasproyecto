package com.example.vistasproyecto.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Mantiene el texto de búsqueda global para filtrar contenido en las pantallas
 * (Feed, Top Games, etc.) desde la barra superior.
 */
object SearchState {
    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query

    private val _isActive = MutableStateFlow(false)
    val isActive: StateFlow<Boolean> = _isActive

    fun setQuery(value: String) {
        _query.value = value
    }

    fun toggle() {
        _isActive.value = !_isActive.value
        if (!_isActive.value) _query.value = ""
    }

    fun close() {
        _isActive.value = false
        _query.value = ""
    }
}
