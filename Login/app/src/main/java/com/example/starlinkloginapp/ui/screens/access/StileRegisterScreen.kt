package com.example.starlinkloginapp.ui.screens.access

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.starlinkloginapp.ui.ViewModel.AuthViewModel
import com.example.starlinkloginapp.R
import com.example.starlinkloginapp.ui.theme.AlegreyaFont
import com.example.starlinkloginapp.ui.theme.AlegreyaSansFont


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StileRegisterScreen(
    authVM: AuthViewModel = viewModel(),
    navController: NavHostController
) {
    var nombre by rememberSaveable { mutableStateOf("") }
    var apellido by rememberSaveable { mutableStateOf("") }
    var usuario by rememberSaveable { mutableStateOf("") }
    var contraseña by rememberSaveable { mutableStateOf("") }
    val status by authVM.status.collectAsState()

    var mostrarErrores by remember { mutableStateOf(false) }

    val nombreError = mostrarErrores && nombre.isBlank()
    val apellidoError = mostrarErrores && apellido.isBlank()
    val usuarioError = mostrarErrores && usuario.isBlank()
    val contraseñaError = mostrarErrores && contraseña.isBlank()

    Surface(
        color = Color(0xFF253334),
        modifier = Modifier.fillMaxSize()
    ) {
        Box(Modifier.fillMaxSize()) {

            Image(
                painter = painterResource(id = R.drawable.background),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .fillMaxSize()
                    .height(190.dp)
                    .align(Alignment.BottomCenter)
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
                        .padding(top = 54.dp)
                        .height(100.dp)
                        .align(Alignment.Start)
                        .offset(x = (-20).dp)
                )

                Text(
                    text = "REGISTRARSE",
                    style = TextStyle(
                        fontSize = 28.sp,
                        fontFamily = AlegreyaFont,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xff79939b)
                    ),
                    modifier = Modifier.align(Alignment.Start)
                )

                Text(
                    text = "Regístrate gratis y comienza tu proceso de control y calidad",
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontFamily = AlegreyaSansFont,
                        color = Color(0xff79939b)
                    ),
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(bottom = 24.dp)
                )

                // Nombre
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    isError = nombreError,
                    singleLine = true,
                    supportingText = {
                        if (nombreError) Text(
                            "Este campo es obligatorio.",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.labelSmall
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = defaultOutlinedColors(nombreError)
                )

                // Apellido
                OutlinedTextField(
                    value = apellido,
                    onValueChange = { apellido = it },
                    label = { Text("Apellido") },
                    isError = apellidoError,
                    singleLine = true,
                    supportingText = {
                        if (apellidoError) Text(
                            "Este campo es obligatorio.",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.labelSmall
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = defaultOutlinedColors(apellidoError)
                )

                OutlinedTextField(
                    value = usuario,
                    onValueChange = { usuario = it },
                    label = { Text("Correo Electrónico") },
                    isError = usuarioError,
                    singleLine = true,
                    supportingText = {
                        if (usuarioError) Text(
                            "Este campo es obligatorio.",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.labelSmall
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = defaultOutlinedColors(usuarioError)
                )

                OutlinedTextField(
                    value = contraseña,
                    onValueChange = { contraseña = it },
                    label = { Text("Contraseña") },
                    isError = contraseñaError,
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    supportingText = {
                        if (contraseñaError) Text(
                            "Este campo es obligatorio.",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.labelSmall
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = defaultOutlinedColors(contraseñaError)
                )

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = {
                        mostrarErrores = true
                        if (!nombreError && !apellidoError && !usuarioError && !contraseñaError) {
                            authVM.signup(
                                nombre.trim(),
                                apellido.trim(),
                                usuario.trim(),
                                contraseña
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Registrarse")
                }

                if (!status.isNullOrEmpty() && status != "REGISTERED" && !nombreError && !apellidoError && !usuarioError && !contraseñaError) {
                    Text(
                        text = "Error: $status",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                if (status == "REGISTERED") {
                    LaunchedEffect(Unit) {
                        navController.popBackStack()
                        authVM.clearStatus()
                    }
                }

                Row(
                    modifier = Modifier.padding(top = 32.dp, bottom = 52.dp)
                ) {
                    Text(
                        text = "¿Ya tienes una cuenta? ",
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontFamily = AlegreyaSansFont,
                            color = Color.White
                        )
                    )
                    Text(
                        text = "Inicia sesión",
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontFamily = AlegreyaSansFont,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier.clickable {
                            navController.navigate("login")
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun defaultOutlinedColors(isError: Boolean): TextFieldColors {
    return OutlinedTextFieldDefaults.colors(
        focusedBorderColor = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
        unfocusedBorderColor = if (isError) MaterialTheme.colorScheme.error else Color.White,
        cursorColor = MaterialTheme.colorScheme.primary,
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White,
        focusedLabelColor = if (isError) MaterialTheme.colorScheme.error else Color.White,
        unfocusedLabelColor = if (isError) MaterialTheme.colorScheme.error else Color.White
    )
}


