package com.example.starlinkloginapp.ui.screens.access

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.starlinkloginapp.ui.ViewModel.AuthViewModel
import com.example.starlinkloginapp.ui.theme.AlegreyaFont
import com.example.starlinkloginapp.ui.theme.AlegreyaSansFont
import com.example.starlinkloginapp.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StileLoginScreen(
    viewModel: AuthViewModel = viewModel(),
    navController: NavController
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    val status by viewModel.status.collectAsState()

    var isEmailError by remember { mutableStateOf(false) }
    var isPasswordError by remember { mutableStateOf(false) }

    Surface(
        color = Color(0xFF253334),
        modifier = Modifier.fillMaxSize()
    ) {
        Box(Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.background),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logotipo),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(top = 54.dp, bottom = 16.dp)
                        .height(100.dp)
                        .align(Alignment.Start)
                        .offset(x = (-20).dp)
                )

                Text(
                    text = "INICIAR SESIÓN",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        color = Color(0xff79939b),
                        fontFamily = AlegreyaFont,
                        fontWeight = FontWeight.SemiBold
                    ),
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(bottom = 4.dp)
                )

                Text(
                    text = "Inicia sesión para acceder a las funciones de análisis",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontFamily = AlegreyaSansFont,
                        color = Color(0xff79939b)
                    ),
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(bottom = 20.dp)
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        isEmailError = false
                    },
                    label = {
                        Text(
                            "Correo electrónico",
                            color = if (isEmailError) MaterialTheme.colorScheme.error else Color(0xFF18779B)
                        )
                    },
                    isError = isEmailError,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                    supportingText = {
                        AnimatedVisibility(visible = isEmailError) {
                            Text(
                                text = "El correo no puede estar vacío",
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = if (isEmailError) MaterialTheme.colorScheme.error else Color(0xFF18779B),
                        unfocusedBorderColor = if (isEmailError) MaterialTheme.colorScheme.error else Color(0xFF18779B),
                        cursorColor = Color(0xFF18779B),
                        focusedTextColor = Color(0xFF18779B),
                        unfocusedTextColor = Color(0xFF18779B)
                    )
                )

                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        isPasswordError = false
                    },
                    label = {
                        Text(
                            "Contraseña",
                            color = if (isPasswordError) MaterialTheme.colorScheme.error else Color(0xFF18779B)
                        )
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    isError = isPasswordError,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                    supportingText = {
                        AnimatedVisibility(visible = isPasswordError) {
                            Text(
                                text = "La contraseña no puede estar vacía",
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = if (isPasswordError) MaterialTheme.colorScheme.error else Color(0xFF18779B),
                        unfocusedBorderColor = if (isPasswordError) MaterialTheme.colorScheme.error else Color(0xFF18779B),
                        cursorColor = Color(0xFF18779B),
                        focusedTextColor = Color(0xFF18779B),
                        unfocusedTextColor = Color(0xFF18779B)
                    )
                )

                Spacer(modifier = Modifier.weight(1f))

                if (!status.isNullOrEmpty() && status != "OK") {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                            .background(
                                color = MaterialTheme.colorScheme.errorContainer,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = "Error",
                                tint = MaterialTheme.colorScheme.onErrorContainer,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = when (status) {
                                    "auth/invalid-credential" -> "Credenciales incorrectas. Verifica tu email y contraseña"
                                    "auth/too-many-requests" -> "Demasiados intentos. Intenta más tarde"
                                    else -> "Error: $status"
                                },
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }


                Button(
                    onClick = {
                        isEmailError = email.isBlank()
                        isPasswordError = password.isBlank()

                        if (!isEmailError && !isPasswordError) {
                            viewModel.login(email.trim(), password)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("Ingresar")
                }

                if (status == "OK") {
                    LaunchedEffect(Unit) {
                        navController.navigate("principal")
                        viewModel.clearStatus()
                    }
                }

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "REGISTRARSE",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier
                        .clickable { navController.navigate("register") }
                        .padding(8.dp)
                )

                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}


