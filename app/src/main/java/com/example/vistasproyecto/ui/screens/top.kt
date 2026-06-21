package com.example.vistasproyecto.ui.screens

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vistasproyecto.R
import java.util.Locale

data class Game(
    val rank: Int,
    val title: String,
    val genre: String,
    val rating: Double,
    val votes: String? = null,
    val badge: String? = null,
    val color: Color = Color.Gray,
    @DrawableRes val imageRes: Int
)

val games = listOf(
    Game(1, "ASSASSIN'S CREED VALHALLA", "ACTION / RPG", 9.9, null, "MASTERPIECE", Color(0xFF1A1A1A),
        R.drawable.asa
    ),
    Game(2, "MINECRAFT", "SANDBOX / SURVIVAL", 9.7, null, null, Color(0xFF4CAF50), R.drawable.mn),
    Game(3, "ROBLOX", "MULTIPLAYER / SANDBOX", 9.5, null, null, Color(0xFFFF3D00), R.drawable.rb),
    Game(4, "SHADOW OF WAR", "ACTION / RPG", 9.2, "12K VOTES", null, Color.Gray, R.drawable.sowme),
    Game(5, "THE WITCHER 3", "ADVENTURE / RPG", 8.9, "8.4K VOTES", null, Color.Gray,
        R.drawable.twwh
    ),
    Game(6, "NEED FOR SPEED HEAT", "RACING", 8.7, "22K VOTES", null, Color.Gray, R.drawable.nfs)
)

@Composable
fun TopGamesScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BgColor)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            HeaderSection()
        }
        
        // Top 1
        item {
            FeaturedGameCard(games[0], isLarge = true)
        }
        
        // Top 2 & 3
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                FeaturedGameCard(games[1], modifier = Modifier.weight(1f))
                FeaturedGameCard(games[2], modifier = Modifier.weight(1f))
            }
        }
        
        // List 4, 5, 6
        items(games.drop(3)) { game ->
            GameListItem(game)
        }
        
        item {
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
fun HeaderSection() {
    Column(modifier = Modifier.padding(vertical = 16.dp)) {
        Text(
            "GLOBAL RANKINGS",
            color = Color(0xFF00B4D8),
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
            color = Color.Gray,
            fontSize = 14.sp,
            lineHeight = 20.sp
        )
    }
}

@Composable
fun FeaturedGameCard(
    game: Game,
    modifier: Modifier = Modifier,
    isLarge: Boolean = false
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .then(if (isLarge) Modifier.height(400.dp) else Modifier.height(250.dp)),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF121212)),
        border = if (!isLarge) BorderStroke(1.dp, Brush.verticalGradient(listOf(game.color, Color.Transparent))) else null
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Image
            Image(
                painter = painterResource(id = game.imageRes),
                contentDescription = game.title,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )
            
            // Overlay to make text more readable
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f)),
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
                color = if (game.rank == 1) Color.LightGray else game.color
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        game.rank.toString(),
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
                    game.title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = if (isLarge) 20.sp else 16.sp
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFF00B4D8),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        game.rating.toString(),
                        color = Color(0xFF00B4D8),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
                
                if (game.badge != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        color = Color(0xFF2A2A2A),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            game.badge,
                            color = Color.Gray,
                            fontSize = 10.sp,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GameListItem(game: Game) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF121212))
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                String.format(Locale.getDefault(), "%02d", game.rank),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.width(32.dp)
            )
            
            Image(
                painter = painterResource(id = game.imageRes),
                contentDescription = null,
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    game.title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Text(
                    game.genre,
                    color = Color.Gray,
                    fontSize = 11.sp
                )
            }
            
            Column(horizontalAlignment = Alignment.End) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        game.rating.toString(),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFF00B4D8),
                        modifier = Modifier.size(12.dp)
                    )
                }
                game.votes?.let {
                    Text(
                        it,
                        color = Color.Gray,
                        fontSize = 10.sp
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
