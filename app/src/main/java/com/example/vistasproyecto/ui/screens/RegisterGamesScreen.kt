package com.example.vistasproyecto.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vistasproyecto.data.SessionManager
import com.example.vistasproyecto.ui.viewmodel.SolicitudesViewModel

@Composable
fun RegisterGamesScreen(
    viewModel: SolicitudesViewModel = viewModel()
) {
    val currentUser by SessionManager.currentUser.collectAsState()

    var gameTitle by remember { mutableStateOf("") }
    var selectedGenre by remember { mutableStateOf("") }
    var releaseYear by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var isGenreExpanded by remember { mutableStateOf(false) }
    var statusMessage by remember { mutableStateOf<String?>(null) }
    var isSuccess by remember { mutableStateOf(false) }

    val isLoading by viewModel.isLoading.collectAsState()
    val success by viewModel.success.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(success) {
        if (success) {
            statusMessage = "¡Solicitud enviada! Será revisada en 24h."
            isSuccess = true
            gameTitle = ""; selectedGenre = ""; releaseYear = ""; description = ""
            viewModel.clearSuccess()
        }
    }

    LaunchedEffect(error) {
        if (error != null) {
            statusMessage = "Error: $error"
            isSuccess = false
        }
    }

    val genres = listOf("Action RPG", "First Person Shooter", "Real-Time Strategy", "Simulation", "Survival Horror")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BgColor)
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Register New Game", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text(
                    "Contribute to the Vortex database by submitting a new title for verification.",
                    fontSize = 14.sp, color = TextGray
                )
                // Mostrar quién está enviando la solicitud
                if (currentUser != null) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AccountCircle, contentDescription = null, tint = AccentCyan, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "Enviando como ${currentUser!!.usuario}",
                            color = AccentCyan,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = Color(0xFFFFA000), modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "Inicia sesión para vincular tu solicitud a tu cuenta.",
                            color = Color(0xFFFFA000),
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        // Banner de estado
        item {
            statusMessage?.let { msg ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            if (isSuccess) Color(0xFF1B5E20) else Color(0xFF7F0000),
                            RoundedCornerShape(12.dp)
                        )
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (isSuccess) Icons.Default.CheckCircle else Icons.Default.Error,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = msg, color = Color.White, fontSize = 13.sp)
                    }
                }
            }
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CardColor, RoundedCornerShape(16.dp))
                    .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(16.dp))
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                VortexInputField(
                    label = "GAME TITLE",
                    value = gameTitle,
                    onValueChange = { gameTitle = it },
                    placeholder = "e.g. Cyber Nexus 2077"
                )

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    // GENRE
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("GENRE", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = AccentCyan, letterSpacing = 1.sp)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .background(BgColor, RoundedCornerShape(8.dp))
                                .border(1.dp, AccentCyan.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                                .clickable { isGenreExpanded = true }
                                .padding(horizontal = 16.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (selectedGenre.isEmpty()) "Select Genre" else selectedGenre,
                                    color = if (selectedGenre.isEmpty()) TextGray else Color.White,
                                    fontSize = 15.sp
                                )
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = Color.White)
                            }
                            DropdownMenu(
                                expanded = isGenreExpanded,
                                onDismissRequest = { isGenreExpanded = false },
                                modifier = Modifier.background(CardColor)
                            ) {
                                genres.forEach { genre ->
                                    DropdownMenuItem(
                                        text = { Text(genre, color = Color.White) },
                                        onClick = { selectedGenre = genre; isGenreExpanded = false }
                                    )
                                }
                            }
                        }
                    }

                    // RELEASE YEAR
                    Column(modifier = Modifier.weight(1f)) {
                        VortexInputField(
                            label = "RELEASE YEAR",
                            value = releaseYear,
                            onValueChange = { if (it.length <= 4) releaseYear = it },
                            placeholder = "2024",
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                    }
                }

                VortexInputField(
                    label = "DESCRIPTION",
                    value = description,
                    onValueChange = { description = it },
                    placeholder = "Briefly describe the gameplay and core mechanics...",
                    singleLine = false,
                    maxLines = 4,
                    modifier = Modifier.height(120.dp)
                )
            }
        }

        item {
            // Upload cover art (placeholder visual)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .background(CardColor, RoundedCornerShape(16.dp))
                    .border(1.5.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(16.dp))
                    .clickable { },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(AccentCyan.copy(alpha = 0.1f), RoundedCornerShape(22.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.AddAPhoto, contentDescription = "Upload", tint = AccentCyan, modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text("Upload Cover Art", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                Text("JPG, PNG up to 10MB", color = TextGray, fontSize = 11.sp)
            }
        }

        item {
            Button(
                onClick = {
                    statusMessage = null
                    when {
                        gameTitle.isBlank() -> { statusMessage = "El título del juego no puede estar vacío."; isSuccess = false }
                        selectedGenre.isEmpty() -> { statusMessage = "Debes seleccionar un género."; isSuccess = false }
                        releaseYear.isBlank() || releaseYear.length != 4 -> { statusMessage = "El año de lanzamiento debe tener 4 dígitos."; isSuccess = false }
                        description.isBlank() -> { statusMessage = "La descripción no puede estar vacía."; isSuccess = false }
                        else -> {
                            // Usar el ID real del usuario logueado, o "guest" si no hay sesión
                            val userId = currentUser?.id ?: "guest"
                            viewModel.addSolicitud(
                                usuarioId = userId,
                                tituloJuego = gameTitle,
                                genero = selectedGenre,
                                anoLanzamiento = releaseYear,
                                descripcion = description,
                                onError = { msg -> statusMessage = "Error: $msg"; isSuccess = false }
                            )
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(containerColor = AccentPurple),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues()
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    } else {
                        Text("SEND REQUEST", color = Color.White, fontWeight = FontWeight.Bold, letterSpacing = 2.sp, fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(Icons.Default.Send, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                    }
                }
            }
        }

        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                BentoCard(modifier = Modifier.weight(1f), title = "FAST LANE", value = "24h Review", icon = Icons.Default.RocketLaunch, iconTint = AccentCyan)
                BentoCard(modifier = Modifier.weight(1f), title = "XP BONUS", value = "+500 Points", icon = Icons.Default.Star, iconTint = AccentPurple)
            }
        }

        item { Spacer(modifier = Modifier.height(100.dp)) }
    }
}

@Composable
fun VortexInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    singleLine: Boolean = true,
    maxLines: Int = 1,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    modifier: Modifier = Modifier
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(label, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = AccentCyan, letterSpacing = 1.sp)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = TextGray.copy(alpha = 0.8f), fontSize = 15.sp) },
            singleLine = singleLine,
            maxLines = maxLines,
            keyboardOptions = keyboardOptions,
            modifier = modifier.fillMaxWidth().background(CardColor, RoundedCornerShape(8.dp)),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AccentCyan,
                unfocusedBorderColor = AccentCyan.copy(alpha = 0.3f),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            ),
            shape = RoundedCornerShape(8.dp)
        )
    }
}

@Composable
fun BentoCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color
) {
    Row(
        modifier = modifier
            .background(CardColor, RoundedCornerShape(12.dp))
            .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(12.dp))
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(18.dp))
        Column {
            Text(title, color = TextGray, fontSize = 9.sp, fontWeight = FontWeight.Bold)
            Text(value, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, maxLines = 1)
        }
    }
}
