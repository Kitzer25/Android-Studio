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
import com.example.starlinkloginapp.ui.screens.access.LoginScreen
import com.example.starlinkloginapp.ui.screens.access.RegisterScreen
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import com.example.starlinkloginapp.ui.ViewModel.ImageViewModel
import com.example.starlinkloginapp.ui.screens.access.StileLoginScreen
import com.example.starlinkloginapp.ui.screens.access.StileRegisterScreen
import com.example.starlinkloginapp.ui.screens.access.StyledStartScreen
import com.example.starlinkloginapp.ui.screens.galeria.GalleryScreen

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
    val imageVM: ImageViewModel = viewModel()

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
        /*composable("principal") {
            PrincipalScreen(navController)
        }*/
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
            GalleryScreen(imageVM, navController)
        }
    }
}
