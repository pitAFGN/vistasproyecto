package com.example.vistasproyecto.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.vistasproyecto.R
import com.example.vistasproyecto.data.SessionManager
import com.example.vistasproyecto.ui.viewmodel.UsuariosViewModel

@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: UsuariosViewModel = viewModel()
) {
    val currentUser by SessionManager.currentUser.collectAsState()

    // Si se cierra sesión, volver a home (login)
    LaunchedEffect(currentUser) {
        if (currentUser == null) {
            navController.navigate("home") {
                popUpTo(0) { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    // Diálogo de confirmación de cierre de sesión
    var showLogoutDialog by remember { mutableStateOf(false) }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            containerColor = CardColor,
            shape = RoundedCornerShape(16.dp),
            title = {
                Text("Cerrar sesión", color = Color.White, fontWeight = FontWeight.Bold)
            },
            text = {
                Text(
                    "¿Seguro que quieres desconectarte del sistema Vortex?",
                    color = TextGray, fontSize = 14.sp
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    showLogoutDialog = false
                    viewModel.logout()
                }) {
                    Text("DESCONECTAR", color = Color(0xFFEF5350), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("CANCELAR", color = TextGray)
                }
            }
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BgColor)
    ) {
        // Banner & Avatar
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
            ) {
                // Banner con gradiente
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .background(
                            Brush.verticalGradient(
                                listOf(Color(0xFF2D1B4E), Color(0xFF1A0A2E))
                            )
                        )
                ) {
                    Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                        Text("~~~~~~", color = AccentCyan.copy(alpha = 0.3f), fontSize = 100.sp)
                    }
                }

                // Avatar
                Surface(
                    modifier = Modifier
                        .size(110.dp)
                        .align(Alignment.BottomCenter)
                        .border(2.dp, AccentCyan, CircleShape)
                        .padding(4.dp),
                    shape = CircleShape,
                    color = Color(0xFF1E1E1E)
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        // Inicial del username como avatar
                        val initial = currentUser?.usuario?.firstOrNull()?.uppercaseChar() ?: '?'
                        Text(
                            text = initial.toString(),
                            color = AccentCyan,
                            fontSize = 40.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Nombre, email y XP
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = currentUser?.usuario ?: "—",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = currentUser?.email ?: "",
                    color = AccentCyan,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Badge XP
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(AccentPurple.copy(alpha = 0.18f), RoundedCornerShape(20.dp))
                        .border(1.dp, AccentPurple.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        tint = AccentPurple,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${currentUser?.puntosXp ?: 0} XP",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                if (currentUser?.id == "user_01") {
                    Button(
                        onClick = { navController.navigate("admin_panel") },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AccentPurple,
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        contentPadding = PaddingValues(vertical = 10.dp)
                    ) {
                        Icon(
                            Icons.Default.AdminPanelSettings,
                            contentDescription = "Panel de Control",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "ADMIN PANEL",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.2.sp
                        )
                    }
                }

                // Botón de cerrar sesión
                OutlinedButton(
                    onClick = { showLogoutDialog = true },
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        Color(0xFFEF5350).copy(alpha = 0.45f)
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFFEF5350).copy(alpha = 0.85f),
                        containerColor = Color(0xFFEF5350).copy(alpha = 0.06f)
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp)
                ) {
                    Icon(
                        Icons.Default.PowerSettingsNew,
                        contentDescription = "Cerrar sesión",
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(7.dp))
                    Text(
                        text = "DISCONNECT",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp
                    )
                }
            }
        }

        // Sección Favoritos
        item {
            SectionHeader(title = "Favorites", icon = Icons.Default.Star)
            FavoriteGameCard()
            Spacer(modifier = Modifier.height(24.dp))
        }

        // Sección Recently Viewed
        item {
            SectionHeader(title = "Recently Viewed", icon = Icons.Default.History)
        }

        item {
            RecentlyViewedItem(
                title = "TITAN BREAKER",
                subtitle = "Reviewed 2 days ago",
                progress = 0.4f,
                accentColor = AccentCyan
            )
        }
        item {
            RecentlyViewedItem(
                title = "RIG SIMULATOR 24",
                subtitle = "Reviewed 5 days ago",
                progress = 0.8f,
                accentColor = Color(0xFF9575CD)
            )
        }

        item { Spacer(modifier = Modifier.height(100.dp)) }
    }
}

@Composable
fun SectionHeader(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = Color(0xFFB39DDB), modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = title, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
        Text(text = "View All", color = AccentCyan, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun FavoriteGameCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .height(200.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardColor)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Color(0xFF1A1A1A))
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("https://tenor.com/search/sahur-gifs")
                        .crossfade(true)
                        .build(),
                    contentDescription = "Portada de Juego",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.sahur),
                    error = painterResource(id = R.drawable.sahur)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("CYBERSTORM 2099", color = Color.White, fontWeight = FontWeight.Bold)
                    Text("Action RPG • 800+ Hours", color = TextGray, fontSize = 12.sp)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = AccentCyan, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("4.9", color = AccentCyan, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun RecentlyViewedItem(title: String, subtitle: String, progress: Float, accentColor: Color) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CardColor),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(Color.Black, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.SportsEsports, contentDescription = null, tint = Color.White.copy(alpha = 0.3f))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(subtitle, color = TextGray, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    repeat(5) { index ->
                        Box(
                            modifier = Modifier
                                .width(20.dp)
                                .height(3.dp)
                                .background(
                                    if (index < (progress * 5).toInt()) accentColor else Color.Gray.copy(alpha = 0.3f),
                                    RoundedCornerShape(2.dp)
                                )
                        )
                    }
                }
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextGray)
        }
    }
}

@Preview
@Composable
fun ProfilePreview() {
    ProfileScreen(navController = rememberNavController())
}