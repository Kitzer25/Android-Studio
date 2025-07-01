/// MainActivity.kt
package com.example.starlinkloginapp


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.starlinkloginapp.ui.theme.StarlinkLoginTheme
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.starlinkloginapp.ui.ViewModel.AuthViewModel
import com.example.starlinkloginapp.ui.cameraX.CameraScreen
import com.example.starlinkloginapp.ui.screens.LoginScreen
import com.example.starlinkloginapp.ui.screens.RegisterScreen

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

    NavHost(navController, startDestination = "login") {
        composable("login") {
            LoginScreen(authVM, navController)
        }
        composable("register") {
            RegisterScreen(authVM, navController)
        }
        /*composable("principal") {
            PrincipalScreen(navController)
        }*/
        composable("camera"){
            CameraScreen(navController)
        }
    }
}
