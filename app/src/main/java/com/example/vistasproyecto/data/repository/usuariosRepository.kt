package com.example.vistasproyecto.data.repository

import com.example.vistasproyecto.data.api.VortexApiService
import com.example.vistasproyecto.data.model.User

class UsuariosRepository(private val apiService: VortexApiService) {
    suspend fun getUsers() = apiService.getUsers()
    suspend fun getUser(id: String) = apiService.getUser(id)
    suspend fun createUser(user: User) = apiService.createUser(user)
    suspend fun updateUser(id: String, user: User) = apiService.updateUser(id, user)
    suspend fun deleteUser(id: String) = apiService.deleteUser(id)
    suspend fun getUserByEmail(email: String) = apiService.getUserByEmail(email)
}