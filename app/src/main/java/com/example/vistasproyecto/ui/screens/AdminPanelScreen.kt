package com.example.vistasproyecto.ui.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.vistasproyecto.data.model.SolicitudJuego
import com.example.vistasproyecto.ui.viewmodel.AdminViewModel

@Composable
fun AdminPanelScreen(
    navController: NavController? = null,
    viewModel: AdminViewModel = viewModel()
) {
    val solicitudes by viewModel.solicitudes.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val successMessage by viewModel.successMessage.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(error, successMessage) {
        error?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearMessages()
        }
        successMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearMessages()
        }
    }

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BgColor)
                    .padding(horizontal = 8.dp, vertical = 12.dp)
            ) {
                IconButton(
                    onClick = { navController?.popBackStack() },
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Text(
                    text = "VORTEX CONTROL PANEL",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = BgColor
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = AccentCyan,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else if (solicitudes.isEmpty()) {
                Text(
                    text = "No hay solicitudes pendientes de verificación.",
                    color = TextGray,
                    fontSize = 16.sp,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(solicitudes) { solicitud ->
                        SolicitudAdminCard(
                            solicitud = solicitud,
                            onAceptar = { viewModel.aceptarSolicitud(solicitud) },
                            onRechazar = { solicitud.id?.let { viewModel.rechazarSolicitud(it) } }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SolicitudAdminCard(
    solicitud: SolicitudJuego,
    onAceptar: () -> Unit,
    onRechazar: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = CardColor), // Tu color de tarjeta (oscuro)
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // PREVISUALIZACIÓN DE LA IMAGEN DE LA URL
                AsyncImage(
                    model = solicitud.imagenUrl,
                    contentDescription = "Preview de ${solicitud.tituloJuego}",
                    modifier = Modifier
                        .size(90.dp)
                        .background(BgColor, RoundedCornerShape(8.dp))
                        .border(1.dp, AccentCyan.copy(alpha = 0.3f), RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop,
                    placeholder = androidx.compose.ui.res.painterResource(android.R.drawable.ic_menu_gallery),
                    error = androidx.compose.ui.res.painterResource(android.R.drawable.stat_notify_error)
                )

                // DATOS PRINCIPALES DEL JUEGO SOLICITADO
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = solicitud.tituloJuego,
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Género: ${solicitud.genero}",
                        color = AccentCyan,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "Año: ${solicitud.anoLanzamiento}",
                        color = TextGray,
                        fontSize = 12.sp
                    )
                    Text(
                        text = "Enviado por: ${solicitud.usuarioId}",
                        color = AccentPurple,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // DESCRIPCIÓN DEL JUEGO
            Text(
                text = solicitud.descripcion,
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 13.sp,
                lineHeight = 18.sp,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // BOTONES DE ACCIÓN (ACEPTAR / RECHAZAR)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Botón Rechazar
                Button(
                    onClick = onRechazar,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7F0000)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Clear, contentDescription = "Rechazar", tint = Color.White)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("RECHAZAR", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }

                // Botón Aceptar
                Button(
                    onClick = onAceptar,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B5E20)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Check, contentDescription = "Aceptar", tint = Color.White)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("ACEPTAR", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        }
    }
}