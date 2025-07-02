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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.starlinkloginapp.R
import com.example.starlinkloginapp.ui.theme.AlegreyaFont
import com.example.starlinkloginapp.ui.theme.AlegreyaSansFont


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StyledStartScreen(
    navController: NavHostController
) {
    Surface(
        color = Color(0xFF253334),
        modifier = Modifier.fillMaxSize()
    ) {
        Box(Modifier.fillMaxSize()) {

            // Imagen de fondo
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

                // ---------- PARTE SUPERIOR ----------
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
                    text = "Bienvenido",
                    style = TextStyle(
                        fontSize = 28.sp,
                        fontFamily = AlegreyaFont,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xff79939b)
                    ),
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(bottom = 4.dp)
                )

                Text(
                    text = "Protegiendo el agua que provees, para una mejor calidad.",
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontFamily = AlegreyaSansFont,
                        color = Color(0xff79939b)
                    ),
                    modifier = Modifier
                        .align(Alignment.Start)
                )

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = { navController.navigate("login") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Iniciar Sesión")
                }

                Row(
                    modifier = Modifier
                        .padding(top = 32.dp, bottom = 52.dp)
                ) {
                    Text(
                        text = "¿No tienes cuenta? ",
                        style = TextStyle(
                            fontSize   = 18.sp,
                            fontFamily = AlegreyaSansFont,
                            color      = Color.White
                        )
                    )
                    Text(
                        text = "Registrate",
                        style = TextStyle(
                            fontSize   = 18.sp,
                            fontFamily = AlegreyaSansFont,
                            fontWeight = FontWeight.Bold,
                            color      = MaterialTheme.colorScheme.primary
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
