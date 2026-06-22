package com.example.vistasproyecto.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.vistasproyecto.data.model.Juego
import com.example.vistasproyecto.ui.viewmodel.JuegosViewModel
import java.util.Locale

@Composable
fun TopGamesScreen(
    viewModel: JuegosViewModel = viewModel()
) {
    val juegos by viewModel.juegos.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // Ordenamos por calificación de mayor a menor
    val sortedJuegos = juegos.sortedByDescending { it.calificacionPromedio }

    Box(modifier = Modifier.fillMaxSize().background(BgColor)) {
        if (isLoading && sortedJuegos.isEmpty()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = AccentCyan)
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    HeaderSection()
                }

                if (sortedJuegos.isNotEmpty()) {
                    // Top 1
                    item {
                        FeaturedGameCard(sortedJuegos[0], isLarge = true, rank = 1)
                    }

                    // Top 2 & 3
                    if (sortedJuegos.size >= 3) {
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                FeaturedGameCard(sortedJuegos[1], modifier = Modifier.weight(1f), rank = 2)
                                FeaturedGameCard(sortedJuegos[2], modifier = Modifier.weight(1f), rank = 3)
                            }
                        }
                    }

                    // List remaining
                    val remainingJuegos = sortedJuegos.drop(if (sortedJuegos.size >= 3) 3 else 1)
                    itemsIndexed(remainingJuegos) { index, juego ->
                        val actualRank = if (sortedJuegos.size >= 3) index + 4 else index + 2
                        GameListItem(juego, actualRank)
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(100.dp))
                }
            }
        }
    }
}

@Composable
fun HeaderSection() {
    Column(modifier = Modifier.padding(vertical = 16.dp)) {
        Text(
            "GLOBAL RANKINGS",
            color = AccentCyan,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            "Top Rated Games",
            color = Color.White,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 4.dp)
        )
        Text(
            "The definitive hall of fame, curated by the community's highest performance metrics and critical acclaim.",
            color = TextGray,
            fontSize = 14.sp,
            lineHeight = 20.sp
        )
    }
}

@Composable
fun FeaturedGameCard(
    juego: Juego,
    modifier: Modifier = Modifier,
    isLarge: Boolean = false,
    rank: Int
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .then(if (isLarge) Modifier.height(400.dp) else Modifier.height(250.dp)),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = CardColor),
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Image
            AsyncImage(
                model = juego.imagenUrl,
                contentDescription = juego.titulo,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )

            // Overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f)),
                            startY = 300f
                        )
                    )
            )

            // Rank Number
            Surface(
                modifier = Modifier
                    .padding(16.dp)
                    .size(32.dp),
                shape = CircleShape,
                color = if (rank == 1) AccentCyan else Color.White
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        rank.toString(),
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Content at bottom
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    juego.titulo.uppercase(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = if (isLarge) 20.sp else 16.sp,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        tint = AccentCyan,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        juego.calificacionPromedio.toString(),
                        color = AccentCyan,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun GameListItem(juego: Juego, rank: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                String.format(Locale.getDefault(), "%02d", rank),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.width(32.dp)
            )

            AsyncImage(
                model = juego.imagenUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    juego.titulo,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    juego.genero,
                    color = TextGray,
                    fontSize = 11.sp
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        juego.calificacionPromedio.toString(),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        tint = AccentCyan,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun TopScreenPreview() {
    TopGamesScreen()
}
