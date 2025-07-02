package com.example.starlinkloginapp.ui.screens.access

import androidx.compose.foundation.Image
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
                    text = "Inicia sesión para accedes a las funciones de análisis",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontFamily = AlegreyaSansFont,
                        color = Color(0xff79939b)
                    ),
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(bottom = 20.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Correo electrónico") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    leadingIcon = { Icon(Icons.Default.Email, null) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = Color(0xFF18779B),
                        cursorColor = MaterialTheme.colorScheme.primary,
                        focusedTextColor = Color(0xFF18779B),
                        unfocusedTextColor = Color(0xFF18779B),
                        focusedLabelColor = Color(0xFF18779B),
                        unfocusedLabelColor = Color(0xFF18779B)
                    )
                )

                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    leadingIcon = { Icon(Icons.Default.Lock, null) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = Color(0xFF18779B),
                        cursorColor = MaterialTheme.colorScheme.primary,
                        focusedTextColor = Color(0xFF18779B),
                        unfocusedTextColor = Color(0xFF18779B),
                        focusedLabelColor = Color(0xFF18779B),
                        unfocusedLabelColor = Color(0xFF18779B)
                    )
                )

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = { viewModel.login(email.trim(), password) },
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
                        navController.navigate("camera")
                        viewModel.clearStatus()
                    }
                } else if (!status.isNullOrEmpty()) {
                    Text(
                        text = "ERROR: $status",
                        color = Color.Red,
                        modifier = Modifier.padding(top = 8.dp)
                    )
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


