/// MainActivity.kt
package com.example.starlinkloginapp


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.starlinkloginapp.ui.theme.StarlinkLoginTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.starlinkloginapp.ui.ViewModel.AuthViewModel
import com.example.starlinkloginapp.ui.cameraX.CameraScreen
import androidx.compose.runtime.getValue
import com.example.starlinkloginapp.ui.screens.access.StileLoginScreen
import com.example.starlinkloginapp.ui.screens.access.StileRegisterScreen
import com.example.starlinkloginapp.ui.screens.access.StyledStartScreen
import com.example.starlinkloginapp.ui.screens.galeria.GalleryScreen
import com.example.starlinkloginapp.ui.screens.principal.PrincipalScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StarlinkLoginTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val authVM: AuthViewModel = viewModel()

    val correo by authVM.correoUsuario.collectAsState()


    NavHost(navController, startDestination = "welcome") {
        composable("welcome"){
            StyledStartScreen(navController)
        }
        composable("login") {
            StileLoginScreen(authVM, navController)
            //LoginScreen(authVM, navController)
        }
        composable("register") {
            StileRegisterScreen(authVM, navController)
            //RegisterScreen(authVM, navController)
        }
        composable("principal") {
            PrincipalScreen(navController, authVM)
        }
        composable("camera") {
            correo?.let {
                CameraScreen(
                    navController = navController,
                    correoUsuario = it)
            }?:run {
                LaunchedEffect(Unit) {
                    navController.navigate("login") {
                        popUpTo("camera") { inclusive = true }
                    }
                }
            }
        }
        composable("galery"){
            GalleryScreen(
                navController)
        }
    }
}
