package com.example.vistasproyecto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.vistasproyecto.data.SessionManager
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vistasproyecto.ui.viewmodel.UsuariosViewModel
import com.example.vistasproyecto.ui.screens.AccentCyan
import com.example.vistasproyecto.ui.screens.BgColor
import com.example.vistasproyecto.ui.screens.CardColor
import com.example.vistasproyecto.ui.screens.ProfileScreen
import com.example.vistasproyecto.ui.screens.VortexTerminalScreen
import com.example.vistasproyecto.ui.screens.VortexFeedScreen
import com.example.vistasproyecto.ui.screens.TopGamesScreen
import com.example.vistasproyecto.ui.screens.RegisterGamesScreen
import com.example.vistasproyecto.ui.theme.VistasProyectoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VistasProyectoTheme {
                MainApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp(
    usuariosViewModel: UsuariosViewModel = viewModel()
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val currentUser by SessionManager.currentUser.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                "VORTEX",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 2.sp
                            )
                            if (currentUser != null) {
                                Text(
                                    currentUser?.usuario?.uppercase() ?: "",
                                    color = AccentCyan,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BgColor)
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color.Black,
                tonalElevation = 0.dp,
                modifier = Modifier.height(70.dp)
            ) {
                // HOME / TERMINAL
                NavigationBarItem(
                    selected = currentRoute == "home",
                    onClick = {
                        navController.navigate("home") {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home", modifier = Modifier.size(26.dp)) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        unselectedIconColor = Color.Gray,
                        indicatorColor = Color.Transparent
                    )
                )

                NavigationBarItem(
                    selected = currentRoute == "top",
                    onClick = {
                        navController.navigate("top") {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    },
                    icon = {
                        Icon(
                            Icons.Default.EmojiEvents,
                            contentDescription = "Top",
                            modifier = Modifier.size(26.dp),
                            tint = if (currentRoute == "top") AccentCyan else Color.Gray
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = AccentCyan,
                        unselectedIconColor = Color.Gray,
                        indicatorColor = Color.Transparent
                    )
                )

                // ADD
                NavigationBarItem(
                    selected = currentRoute == "add",
                    onClick = {
                        navController.navigate("add") {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    },
                    icon = { Icon(Icons.Default.Add, contentDescription = "Add", modifier = Modifier.size(26.dp)) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        unselectedIconColor = Color.Gray,
                        indicatorColor = Color.Transparent
                    )
                )

                NavigationBarItem(
                    selected = currentRoute == "feed",
                    onClick = {
                        navController.navigate("feed") {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    },
                    icon = {
                        Icon(
                            Icons.Default.FormatListBulleted,
                            contentDescription = "Feed",
                            modifier = Modifier.size(26.dp)
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        unselectedIconColor = Color.Gray,
                        indicatorColor = Color.Transparent
                    )
                )

                NavigationBarItem(
                    selected = currentRoute == "profile",
                    onClick = {
                        navController.navigate("profile") {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile", modifier = Modifier.size(26.dp)) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        unselectedIconColor = Color.Gray,
                        indicatorColor = Color.Transparent
                    )
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") {
                VortexTerminalScreen(navController = navController)
            }
            composable("top") {
                TopGamesScreen()
            }
            composable("add") {
                RegisterGamesScreen()
            }
            composable("feed") {
                VortexFeedScreen()
            }
            composable("profile") {
                ProfileScreen(navController = navController)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    VistasProyectoTheme {
        MainApp()
    }
}

