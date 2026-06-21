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
import com.example.vistasproyecto.ui.viewmodel.UsuariosViewModel

@Composable
fun VortexTerminalScreen(
    viewModel: UsuariosViewModel = viewModel()
) {
    var isLogin by remember { mutableStateOf(true) }
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()

    var identifier by remember { mutableStateOf("") }
    var accessKey by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }

    if (currentUser != null) {
        Column(
            modifier = Modifier.fillMaxSize().background(BgColor),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Welcome, ${currentUser?.usuario}!", color = Color.White, fontSize = 24.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { viewModel.logout() }, colors = ButtonDefaults.buttonColors(containerColor = AccentPurple)) {
                Text("DISCONNECT")
            }
        }
        return
    }

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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (!isLogin) {
                            IconButton(
                                onClick = { isLogin = true },
                                modifier = Modifier
                                    .size(32.dp)
                                    .padding(end = 8.dp)
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
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (isLogin) "Enter your credentials to sync with the grid."
                        else "Join the network to access the neural interface.",
                        color = TextGray,
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    if (error != null) {
                        Text(error!!, color = Color.Red, fontSize = 12.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    if (!isLogin) {
                        Text(
                            text = "USERNAME",
                            color = TextGray,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        VortexTextField(
                            value = username,
                            onValueChange = { username = it },
                            placeholder = "Xenon_Hunter",
                            trailingIcon = {
                                Icon(
                                    Icons.Default.Person,
                                    contentDescription = null,
                                    tint = TextGray
                                )
                            }
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                    }

                    Text(
                        text = "IDENTIFIER",
                        color = TextGray,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    VortexTextField(
                        value = identifier,
                        onValueChange = { identifier = it },
                        placeholder = "user@vortex.network",
                        trailingIcon = {
                            Icon(
                                Icons.Default.AlternateEmail,
                                contentDescription = null,
                                tint = TextGray
                            )
                        }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "ACCESS KEY",
                        color = TextGray,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    VortexTextField(
                        value = accessKey,
                        onValueChange = { accessKey = it },
                        placeholder = "********",
                        visualTransformation = PasswordVisualTransformation(),
                        trailingIcon = {
                            Icon(
                                Icons.Default.Lock,
                                contentDescription = null,
                                tint = TextGray
                            )
                        }
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = { 
                            if (isLogin) {
                                viewModel.login(identifier, accessKey, onSuccess = {})
                            } else {
                                viewModel.registrarUsuario(username, identifier, accessKey, onSuccess = {})
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AccentPurple),
                        shape = RoundedCornerShape(8.dp),
                        enabled = !isLoading
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

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isLogin) "New operative? " else "Already have access? ",
                            color = TextGray,
                            fontSize = 14.sp
                        )
                        TextButton(
                            onClick = { isLogin = !isLogin },
                            contentPadding = PaddingValues(0.dp),
                            modifier = Modifier.height(20.dp)
                        ) {
                            Text(
                                text = if (isLogin) "REGISTER ACCOUNT" else "LOG IN",
                                color = AccentCyan,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(40.dp))
        }
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
