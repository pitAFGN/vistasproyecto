package com.example.vistasproyecto.data.api

import com.example.vistasproyecto.data.model.Juego
import com.example.vistasproyecto.data.model.SolicitudJuego
import com.example.vistasproyecto.data.model.User
import com.example.vistasproyecto.data.model.Calificacion
import retrofit2.http.*

interface VortexApiService {

    // --- USER CRUD & AUTHENTICATION ---
    @GET("usuarios")
    suspend fun getUsers(): List<User>

    @GET("usuarios/{id}")
    suspend fun getUser(@Path("id") id: String): User

    @POST("usuarios")
    suspend fun createUser(@Body user: User): User

    @PUT("usuarios/{id}")
    suspend fun updateUser(@Path("id") id: String, @Body user: User): User

    @DELETE("usuarios/{id}")
    suspend fun deleteUser(@Path("id") id: String)

    // Tip: Útil para validar si un correo ya existe al iniciar sesión
    @GET("usuarios")
    suspend fun getUserByEmail(@Query("email") email: String): List<User>


    // --- JUEGOS CRUD (Para la biblioteca principal de Vortex) ---
    @GET("juegos")
    suspend fun getJuegos(): List<Juego>

    @GET("juegos/{id}")
    suspend fun getJuego(@Path("id") id: String): Juego

    @POST("juegos")
    suspend fun createJuego(@Body juego: Juego): Juego

    @PUT("juegos/{id}")
    suspend fun updateJuego(@Path("id") id: String, @Body juego: Juego): Juego

    @DELETE("juegos/{id}")
    suspend fun deleteJuego(@Path("id") id: String)


    // --- SOLICITUDES CRUD (Mapeado a tu pantalla RegisterGamesScreen) ---
    @GET("solicitudes")
    suspend fun getSolicitudes(): List<SolicitudJuego>

    @GET("solicitudes/{id}")
    suspend fun getSolicitud(@Path("id") id: String): SolicitudJuego

    @POST("solicitudes")
    suspend fun createSolicitud(@Body solicitud: SolicitudJuego): SolicitudJuego

    @PUT("solicitudes/{id}")
    suspend fun updateSolicitud(@Path("id") id: String, @Body solicitud: SolicitudJuego): SolicitudJuego

    @DELETE("solicitudes/{id}")
    suspend fun deleteSolicitud(@Path("id") id: String)


    // --- CALIFICACIONES CRUD (Para reseñar los juegos) ---
    @GET("calificaciones")
    suspend fun getCalificaciones(): List<Calificacion>

    @POST("calificaciones")
    suspend fun createCalificacion(@Body calificacion: Calificacion): Calificacion

    // Tip de json-server: Permite traer solo las calificaciones pertenecientes a un juego específico
    @GET("calificaciones")
    suspend fun getCalificacionesPorJuego(@Query("juego_id") juegoId: String): List<Calificacion>

}
