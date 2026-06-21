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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vistasproyecto.ui.viewmodel.SolicitudesViewModel
import com.example.vistasproyecto.ui.viewmodel.UsuariosViewModel
import androidx.compose.runtime.collectAsState
import java.util.Locale

@Composable
fun RegisterGamesScreen(
    viewModel: SolicitudesViewModel = viewModel(),
    usuariosViewModel: UsuariosViewModel = viewModel()
) {
    val currentUser by usuariosViewModel.currentUser.collectAsState()
    
    // Estados para los campos del formulario
    var gameTitle by remember { mutableStateOf("") }
    var selectedGenre by remember { mutableStateOf("") }
    var releaseYear by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var isGenreExpanded by remember { mutableStateOf(false) }

    val genres = listOf("Action RPG", "First Person Shooter", "Real-Time Strategy", "Simulation", "Survival Horror")

    if (currentUser == null) {
        Box(modifier = Modifier.fillMaxSize().background(BgColor), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.Lock, contentDescription = null, tint = AccentCyan, modifier = Modifier.size(64.dp))
                Spacer(modifier = Modifier.height(16.dp))
                Text("Please login to submit requests", color = Color.White, fontSize = 18.sp)
            }
        }
        return
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BgColor)
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))
            // Header Section
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "Register New Game",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Contribute to the Vortex database by submitting a new title for verification.",
                    fontSize = 14.sp,
                    color = TextGray
                )
            }
        }

        item {
            // Form Container
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CardColor, RoundedCornerShape(16.dp))
                    .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(16.dp))
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // GAME TITLE FIELD
                VortexInputField(
                    label = "GAME TITLE",
                    value = gameTitle,
                    onValueChange = { gameTitle = it },
                    placeholder = "e.g. Cyber Nexus 2077"
                )

                // ROW FOR GENRE & RELEASE YEAR
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // GENRE
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "GENRE",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = AccentCyan,
                            letterSpacing = 1.sp
                        )
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
                                        onClick = {
                                            selectedGenre = genre
                                            isGenreExpanded = false
                                        }
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
                            onValueChange = { releaseYear = it },
                            placeholder = "2024",
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                    }
                }

                // DESCRIPTION FIELD
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
            // UPLOAD COVER ART PLACEHOLDER
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .background(CardColor, RoundedCornerShape(16.dp))
                    .border(
                        width = 1.5.dp,
                        color = Color.White.copy(alpha = 0.08f),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .clickable { /* Handle Upload */ },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(AccentCyan.copy(alpha = 0.1f), RoundedCornerShape(22.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AddAPhoto,
                        contentDescription = "Upload",
                        tint = AccentCyan,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text("Upload Cover Art", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                Text("JPG, PNG up to 10MB", color = TextGray, fontSize = 11.sp)
            }
        }

        item {
            // SEND REQUEST GRADIENT BUTTON
            val isLoading by viewModel.isLoading.collectAsState()
            
            Button(
                onClick = { 
                    viewModel.addSolicitud(
                        usuarioId = currentUser?.id ?: "unknown",
                        tituloJuego = gameTitle,
                        genero = selectedGenre,
                        anoLanzamiento = releaseYear,
                        descripcion = description
                    )
                    // Clear fields after sending
                    gameTitle = ""
                    selectedGenre = ""
                    releaseYear = ""
                    description = ""
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = !isLoading && gameTitle.isNotBlank() && selectedGenre.isNotBlank(),
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
                        Text(
                            text = "SEND REQUEST",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(Icons.Default.Send, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                    }
                }
            }
        }

        item {
            // BENTO INFO CARDS
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                BentoCard(modifier = Modifier.weight(1f), title = "FAST LANE", value = "24h Review", icon = Icons.Default.RocketLaunch, iconTint = AccentCyan)
                BentoCard(modifier = Modifier.weight(1f), title = "XP BONUS", value = "+500 Points", icon = Icons.Default.Star, iconTint = AccentPurple)
            }
        }

        item {
            Spacer(modifier = Modifier.height(100.dp))
        }
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
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = AccentCyan,
            letterSpacing = 1.sp
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = TextGray.copy(alpha = 0.8f), fontSize = 15.sp) },
            singleLine = singleLine,
            maxLines = maxLines,
            keyboardOptions = keyboardOptions,
            modifier = modifier
                .fillMaxWidth()
                .background(CardColor, RoundedCornerShape(8.dp)),
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
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.size(18.dp)
        )
        Column {
            Text(title, color = TextGray, fontSize = 9.sp, fontWeight = FontWeight.Bold)
            Text(value, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, maxLines = 1)
        }
    }
}