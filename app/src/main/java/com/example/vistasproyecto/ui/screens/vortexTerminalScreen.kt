package com.example.vistasproyecto.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.vistasproyecto.data.SessionManager
import com.example.vistasproyecto.ui.viewmodel.UsuariosViewModel

@Composable
fun VortexTerminalScreen(
    navController: NavController,
    viewModel: UsuariosViewModel = viewModel()
) {
    val currentUser by SessionManager.currentUser.collectAsState()

    // Si ya hay sesión activa, redirigir a profile automáticamente
    LaunchedEffect(currentUser) {
        if (currentUser != null) {
            navController.navigate("profile") {
                popUpTo("home") { inclusive = false }
                launchSingleTop = true
            }
        }
    }

    var isLogin by remember { mutableStateOf(true) }
    var identifier by remember { mutableStateOf("") }
    var accessKey by remember { mutableStateOf("") }
    var confirmKey by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var statusMessage by remember { mutableStateOf<String?>(null) }
    var isSuccess by remember { mutableStateOf(false) }

    val isLoading by viewModel.isLoading.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF1A0A2E), Color(0xFF0A0A0A))
                )
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(24.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "VORTEX",
                color = Color.White,
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 4.sp
            )
            Text(
                text = "NEURAL INTERFACE V4.0",
                color = AccentCyan,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 2.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(60.dp))
        }

        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CardColor, RoundedCornerShape(16.dp))
                    .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(16.dp))
                    .padding(24.dp)
            ) {
                Column {
                    // Header
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (!isLogin) {
                            IconButton(
                                onClick = { isLogin = true; statusMessage = null },
                                modifier = Modifier.size(32.dp).padding(end = 8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back to Login",
                                    tint = Color.White
                                )
                            }
                        }
                        Text(
                            text = if (isLogin) "Initialize Session" else "Create Operative",
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    // Banner de estado
                    statusMessage?.let { msg ->
                        Spacer(modifier = Modifier.height(12.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    if (isSuccess) Color(0xFF1B5E20) else Color(0xFF7F0000),
                                    RoundedCornerShape(8.dp)
                                )
                                .padding(12.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = if (isSuccess) Icons.Default.CheckCircle else Icons.Default.Error,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = msg, color = Color.White, fontSize = 13.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Campo USERNAME (solo en registro)
                    if (!isLogin) {
                        Text("USERNAME", color = TextGray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        VortexTextField(
                            value = username,
                            onValueChange = { username = it },
                            placeholder = "Xenon_Hunter",
                            trailingIcon = { Icon(Icons.Default.Person, null, tint = TextGray) }
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                    }

                    // Campo IDENTIFIER
                    Text("EMAIL", color = TextGray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    VortexTextField(
                        value = identifier,
                        onValueChange = { identifier = it },
                        placeholder = "user@vortex.network",
                        trailingIcon = { Icon(Icons.Default.AlternateEmail, null, tint = TextGray) }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Campo ACCESS KEY
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("PASSWORD", color = TextGray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        if (isLogin) {
                            Text("LOST ACCESS?", color = TextGray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    VortexTextField(
                        value = accessKey,
                        onValueChange = { accessKey = it },
                        placeholder = "********",
                        visualTransformation = PasswordVisualTransformation(),
                        trailingIcon = { Icon(Icons.Default.Lock, null, tint = TextGray) }
                    )

                    // Campo CONFIRM ACCESS KEY (solo en registro)
                    if (!isLogin) {
                        Spacer(modifier = Modifier.height(24.dp))
                        Text("CONFIRM PASSWORD", color = TextGray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        VortexTextField(
                            value = confirmKey,
                            onValueChange = { confirmKey = it },
                            placeholder = "********",
                            visualTransformation = PasswordVisualTransformation(),
                            trailingIcon = { Icon(Icons.Default.Lock, null, tint = TextGray) }
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Botón principal
                    Button(
                        onClick = {
                            statusMessage = null
                            if (isLogin) {
                                viewModel.loginUsuario(
                                    email = identifier,
                                    contrasena = accessKey,
                                    onSuccess = {
                                        // La redirección la maneja el LaunchedEffect(currentUser)
                                    },
                                    onError = { msg ->
                                        statusMessage = msg
                                        isSuccess = false
                                    }
                                )
                            } else {
                                when {
                                    username.isBlank() -> { statusMessage = "El nombre de usuario no puede estar vacío."; isSuccess = false }
                                    identifier.isBlank() -> { statusMessage = "El email no puede estar vacío."; isSuccess = false }
                                    accessKey.length < 6 -> { statusMessage = "La contraseña debe tener al menos 6 caracteres."; isSuccess = false }
                                    accessKey != confirmKey -> { statusMessage = "Las contraseñas no coinciden."; isSuccess = false }
                                    else -> {
                                        viewModel.registrarUsuario(
                                            usuario = username,
                                            email = identifier,
                                            contrasena = accessKey,
                                            onSuccess = {
                                                statusMessage = "¡Cuenta creada! Ya puedes iniciar sesión."
                                                isSuccess = true
                                                isLogin = true
                                                username = ""; identifier = ""; accessKey = ""; confirmKey = ""
                                            },
                                            onError = { msg -> statusMessage = msg; isSuccess = false }
                                        )
                                    }
                                }
                            }
                        },
                        enabled = !isLoading,
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AccentPurple),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                        } else {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = if (isLogin) "ESTABLISH CONNECTION" else "INITIALIZE REGISTRATION",
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(
                                    if (isLogin) Icons.Default.Login else Icons.Default.VideogameAsset,
                                    contentDescription = null
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Toggle Login / Registro
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isLogin) "New operative? " else "Already have access? ",
                            color = TextGray, fontSize = 14.sp
                        )
                        TextButton(
                            onClick = {
                                isLogin = !isLogin; statusMessage = null
                                identifier = ""; accessKey = ""; confirmKey = ""; username = ""
                            },
                            contentPadding = PaddingValues(0.dp),
                            modifier = Modifier.height(20.dp)
                        ) {
                            Text(
                                text = if (isLogin) "REGISTER" else "LOGIN",
                                color = AccentCyan, fontSize = 14.sp, fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(40.dp)) }
    }
}

@Composable
fun VortexTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    visualTransformation: androidx.compose.ui.text.input.VisualTransformation = androidx.compose.ui.text.input.VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.White.copy(alpha = 0.2f), RoundedCornerShape(8.dp)),
        placeholder = { Text(placeholder, color = TextGray.copy(alpha = 0.5f)) },
        visualTransformation = visualTransformation,
        trailingIcon = trailingIcon,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
        ),
        shape = RoundedCornerShape(8.dp)
    )
}

@Composable
fun ExternalNodeButton(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = { },
        modifier = modifier.height(48.dp),
        shape = RoundedCornerShape(8.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.1f)),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}

@Preview
@Composable
fun VortexTerminalPreview() {
    VortexTerminalScreen(navController = rememberNavController())
}
