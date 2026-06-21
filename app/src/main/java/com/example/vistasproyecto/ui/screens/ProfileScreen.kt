package com.example.vistasproyecto.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProfileScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BgColor)
    ) {
        // Banner & Profile Picture Header
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
            ) {
                // Banner (Placeholder with Gradient)
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
                    // Simulating the wave lines in the banner
                    Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                        Text("~~~~~~", color = AccentCyan.copy(alpha = 0.3f), fontSize = 100.sp)
                    }
                }

                // Profile Picture with Cyan Glow
                Surface(
                    modifier = Modifier
                        .size(110.dp)
                        .align(Alignment.BottomCenter)
                        .border(2.dp, AccentCyan, CircleShape)
                        .padding(4.dp),
                    shape = CircleShape,
                    color = Color.DarkGray
                ) {
                    Icon(
                        Icons.Default.Person, 
                        contentDescription = null, 
                        modifier = Modifier.padding(16.dp),
                        tint = Color.White
                    )
                }
            }
        }

        // Name and Bio
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Xenon_Hunter",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Professional frag hunter and lore enthusiast. Exploring the neon-lit streets of the multiverse one frame at a time.",
                    color = TextGray,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )
            }
        }

        // Favorites Section
        item {
            SectionHeader(title = "Favorites", icon = Icons.Default.Star)
            FavoriteGameCard()
            Spacer(modifier = Modifier.height(24.dp))
        }

        // Recently Viewed Section
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
        
        item {
            Spacer(modifier = Modifier.height(100.dp)) // Space for BottomBar
        }
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
            Text(
                text = title,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Text(
            text = "View All",
            color = AccentCyan,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
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
            // Game Image Placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Color(0xFF1A1A1A))
            ) {
                Icon(
                    Icons.Default.Gamepad, 
                    contentDescription = null, 
                    modifier = Modifier.align(Alignment.Center).size(48.dp),
                    tint = Color.White.copy(alpha = 0.1f)
                )
            }
            
            // Info Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
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
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon Placeholder
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
                // Progress dashes simulation
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
    ProfileScreen()
}
