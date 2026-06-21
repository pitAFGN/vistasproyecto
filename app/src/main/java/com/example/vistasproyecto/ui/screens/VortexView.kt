package com.example.vistasproyecto.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.vistasproyecto.ui.viewmodel.JuegosViewModel
import com.example.vistasproyecto.ui.viewmodel.UsuariosViewModel

data class FeedItemUI(
    val idCalificacion: String,
    val userName: String,
    val userHandle: String,
    val rating: Double,
    val comment: String,
    val juegoTitulo: String,
    val juegoImagenUrl: String,
    val timeAgo: String = "Recent"
)

@Composable
fun VortexFeedScreen(
    juegosViewModel: JuegosViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    usuariosViewModel: UsuariosViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    // Recolectamos de forma reactiva las listas desde Render (json-server)
    val juegos by juegosViewModel.juegos.collectAsState()
    val usuarios by usuariosViewModel.usuarios.collectAsState()
    val isLoading by juegosViewModel.isLoading.collectAsState()

    // Unimos los datos dinámicamente para simular las opiniones en base a tu base de datos real
    val feedItems = remember(juegos, usuarios) {
        juegos.mapIndexed { index, juego ->
            // Rotamos o asignamos usuarios reales de la DB para simular los autores del feed
            val usuarioAsignado = if (usuarios.isNotEmpty()) usuarios[index % usuarios.size] else null

            FeedItemUI(
                idCalificacion = juego.id ?: index.toString(),
                userName = usuarioAsignado?.usuario ?: "GUEST_WARRIOR",
                userHandle = "@${usuarioAsignado?.email?.split("@")?.first() ?: "vortex"}",
                rating = juego.calificacionPromedio,
                comment = juego.descripcion,
                juegoTitulo = juego.titulo,
                juegoImagenUrl = juego.imagenUrl
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgColor)
    ) {
        if (isLoading && feedItems.isEmpty()) {
            // Un bonito indicador de carga en lo que Render despierta del modo sueño
            CircularProgressIndicator(
                color = AccentCyan,
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    // Toma dinámicamente el primer juego del backend como el destacado del editor
                    val destacado = juegos.firstOrNull()
                    FeaturedCard(
                        titulo = destacado?.titulo ?: "VORTEX INSTALADO",
                        descripcion = destacado?.descripcion ?: "Conecta tu API para cargar la base de datos cyberpunk en tiempo real.",
                        imagenUrl = destacado?.imagenUrl ?: ""
                    )
                }
                item {
                    FilterSection()
                }
                // Lista funcional e interactiva desde internet
                items(feedItems) { item ->
                    VortexFeedItem(item)
                }
                item {
                    Spacer(modifier = Modifier.height(100.dp))
                }
            }
        }
    }
}

@Composable
fun FeaturedCard(titulo: String, descripcion: String, imagenUrl: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFF2D2D2D))
    ) {
        // Carga la portada del juego destacado como fondo
        if (imagenUrl.isNotBlank()) {
            AsyncImage(
                model = imagenUrl,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF4C1D95).copy(alpha = 0.5f),
                            Color.Black.copy(alpha = 0.85f)
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(20.dp)
        ) {
            Surface(
                color = AccentCyan.copy(alpha = 0.15f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    "EDITOR'S CHOICE",
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = AccentCyan
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                titulo.uppercase(),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                descripcion,
                fontSize = 13.sp,
                color = Color.LightGray.copy(alpha = 0.8f),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun FilterSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(listOf("Recent", "Trending", "Top Rated")) { filter ->
                Text(
                    filter,
                    color = if (filter == "Recent") Color.White else Color.Gray,
                    fontWeight = if (filter == "Recent") FontWeight.Bold else FontWeight.Normal,
                    modifier = Modifier.padding(vertical = 8.dp),
                    fontSize = 15.sp
                )
            }
        }
        Icon(Icons.Default.Tune, contentDescription = "Filter", tint = Color.Gray, modifier = Modifier.size(20.dp))
    }
}

@Composable
fun VortexFeedItem(item: FeedItemUI) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(CardColor)
        ) {
            // ASYNCIMAGE DESCARGA LA IMAGEN EN TIEMPO REAL DESDE LA URL ASIGNADA EN TU JSON
            AsyncImage(
                model = item.juegoImagenUrl,
                contentDescription = "Portada de ${item.juegoTitulo}",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Gradiente estético ciberpunk sobre la imagen para mantener contraste
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color(0xFF121212).copy(alpha = 0.9f))
                        )
                    )
            )

            // Badge de Rating
            Surface(
                color = Color.Black.copy(alpha = 0.6f),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        tint = AccentCyan,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        item.rating.toString(),
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Título del juego renderizado dinámicamente encima del contenedor
            Text(
                text = item.juegoTitulo.uppercase(),
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Fila de información del usuario de tu DB
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(AccentPurple),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = item.userName.take(2).uppercase(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Text(
                    item.userName,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 15.sp
                )
                Text(
                    item.userHandle,
                    color = Color.Gray,
                    fontSize = 12.sp,
                    letterSpacing = 0.5.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // El comentario/reseña del juego
        Text(
            text = "\"${item.comment}\"",
            color = Color.White.copy(alpha = 0.85f),
            fontSize = 15.sp,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            lineHeight = 22.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(item.timeAgo, color = Color.Gray, fontSize = 13.sp)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Text(
                    "Read More",
                    color = AccentCyan,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = AccentCyan
                )
            }
        }
    }
}
