package com.example.vistasproyecto.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vistasproyecto.data.SessionManager
import com.example.vistasproyecto.data.model.Calificacion
import com.example.vistasproyecto.data.model.User
import com.example.vistasproyecto.ui.viewmodel.CalificacionesViewModel
import com.example.vistasproyecto.ui.viewmodel.JuegosViewModel
import com.example.vistasproyecto.ui.viewmodel.UsuariosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResenaJuegoScreen(
    juegoId: String,
    juegoTitulo: String,
    onBack: () -> Unit,
    calificacionesViewModel: CalificacionesViewModel = viewModel(),
    juegosViewModel: JuegosViewModel = viewModel(),
    usuariosViewModel: UsuariosViewModel = viewModel(),
) {
    var rating by remember { mutableIntStateOf(0) }
    var reviewText by remember { mutableStateOf("") }
    
    val userId = SessionManager.getUserId()
    val isLoading by calificacionesViewModel.isLoading.collectAsState()
    val success by calificacionesViewModel.success.collectAsState()
    val error by calificacionesViewModel.error.collectAsState()
    val calificaciones by calificacionesViewModel.calificaciones.collectAsState()
    val juegos by juegosViewModel.juegos.collectAsState()
    val usuarios by usuariosViewModel.usuarios.collectAsState()
    
    // Encontrar el juego actual en la lista para mostrar su info completa
    val juegoActual = remember(juegos, juegoId) {
        juegos.find { it.id == juegoId }
    }

    LaunchedEffect(juegoId) {
        calificacionesViewModel.fetchCalificaciones(juegoId)
        usuariosViewModel.fetchUsuarios() // Asegurar que tenemos la lista de usuarios para los nombres
    }

    LaunchedEffect(success) {
        if (success) {
            rating = 0
            reviewText = ""
            calificacionesViewModel.resetSuccess()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(juegoTitulo.uppercase(), fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BgColor)
            )
        },
        containerColor = BgColor
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // 1. CARTA DEL JUEGO
            item {
                Spacer(modifier = Modifier.height(8.dp))
                juegoActual?.let { juego ->
                    FeaturedGameCard(
                        juego = juego,
                        rank = 0,
                        isLarge = true
                    )
                }
            }

            // 2. FORMULARIO PARA ESCRIBIR RESEÑA
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(CardColor, RoundedCornerShape(16.dp))
                        .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(16.dp))
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "DEJA TU CALIFICACIÓN",
                        color = AccentCyan,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        repeat(5) { index ->
                            val starIndex = index + 1
                            Icon(
                                imageVector = if (rating >= starIndex) Icons.Default.Star else Icons.Default.StarBorder,
                                contentDescription = "Star $starIndex",
                                tint = if (rating >= starIndex) AccentCyan else TextGray,
                                modifier = Modifier
                                    .size(40.dp)
                                    .clickable { rating = starIndex }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    OutlinedTextField(
                        value = reviewText,
                        onValueChange = { reviewText = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        placeholder = { Text("Comparte tu experiencia con este título...", color = TextGray.copy(alpha = 0.5f), fontSize = 14.sp) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AccentCyan,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.1f),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedContainerColor = BgColor.copy(alpha = 0.5f),
                            unfocusedContainerColor = BgColor.copy(alpha = 0.3f)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    if (error != null) {
                        Text(error ?: "Error", color = Color.Red, fontSize = 12.sp, modifier = Modifier.padding(bottom = 8.dp))
                    }
                    
                    if (userId == null) {
                        Text("INICIA SESIÓN PARA PUBLICAR", color = Color.Yellow, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = {
                            if (rating > 0 && reviewText.isNotBlank() && userId != null) {
                                calificacionesViewModel.addCalificacion(
                                    usuarioId = userId,
                                    juegoId = juegoId,
                                    puntaje = rating,
                                    comentario = reviewText
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        enabled = !isLoading && rating > 0 && reviewText.isNotBlank() && userId != null,
                        colors = ButtonDefaults.buttonColors(containerColor = AccentPurple),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
                        } else {
                            Text("PUBLICAR RESEÑA", fontWeight = FontWeight.Black, letterSpacing = 1.sp, fontSize = 14.sp)
                        }
                    }
                }
            }

            // 3. LISTA DE RESEÑAS REGISTRADAS
            item {
                Text(
                    "RESEÑAS DE LA COMUNIDAD (${calificaciones.size})",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            if (calificaciones.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(vertical = 40.dp), contentAlignment = Alignment.Center) {
                        Text("No hay reseñas aún. ¡Sé el primero!", color = TextGray, fontSize = 14.sp)
                    }
                }
            } else {
                items(calificaciones.reversed()) { calificacion ->
                    val autor = usuarios.find { it.id == calificacion.usuarioId }
                    ItemResena(calificacion, autor)
                }
            }

            item {
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@Composable
fun ItemResena(calificacion: Calificacion, usuario: User?) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardColor),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(AccentPurple.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = (usuario?.usuario?.take(1) ?: "?").uppercase(),
                            color = AccentPurple,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = usuario?.usuario ?: "Operativo Desconocido",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    repeat(5) { index ->
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = if (index < calificacion.puntaje) AccentCyan else Color.Gray.copy(alpha = 0.3f),
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = calificacion.comentario,
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
        }
    }
}
