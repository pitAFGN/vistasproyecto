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

// Colores personalizados basados en la imagen
val BgColor = Color(0xFF0A0A0A)
val CardColor = Color(0xFF121212)
val AccentCyan = Color(0xFF4DD0E1)
val AccentPurple = Color(0xFF6A1B9A)
val TextGray = Color(0xFF9E9E9E)

@Composable
fun VortexTerminalScreen() {
    var isLogin by remember { mutableStateOf(true) }

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
            // Reduced spacer because of TopAppBar
            Spacer(modifier = Modifier.height(20.dp))

            // Header
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
            // Login/Register Card
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

                    // Identifier Field
                    var identifier by remember { mutableStateOf("") }
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

                    // Access Key Field
                    var accessKey by remember { mutableStateOf("") }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "ACCESS KEY",
                            color = TextGray,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        if (isLogin) {
                            Text(
                                text = "LOST ACCESS?",
                                color = TextGray,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
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

                    if (!isLogin) {
                        Spacer(modifier = Modifier.height(24.dp))
                        // Confirm Access Key Field
                        var confirmKey by remember { mutableStateOf("") }
                        Text(
                            text = "CONFIRM ACCESS KEY",
                            color = TextGray,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        VortexTextField(
                            value = confirmKey,
                            onValueChange = { confirmKey = it },
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
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Action Button
                    Button(
                        onClick = { /* Handle action */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AccentPurple),
                        shape = RoundedCornerShape(8.dp)
                    ) {
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

                    Spacer(modifier = Modifier.height(32.dp))

                    // Footer toggle INSIDE the card
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

@Composable
fun ExternalNodeButton(text: String, icon: androidx.compose.ui.graphics.vector.ImageVector, modifier: Modifier = Modifier) {
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
    VortexTerminalScreen()
}
