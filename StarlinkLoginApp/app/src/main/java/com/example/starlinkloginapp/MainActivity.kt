package com.example.starlinkloginapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import com.example.starlinkloginapp.ui.theme.StarlinkLoginTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.starlinkloginapp.ui.screens.LoginScreen
import com.example.starlinkloginapp.ui.screens.RegisterScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val isDarkTheme = mutableStateOf(false)

            StarlinkLoginTheme(useDarkTheme = isDarkTheme.value) {
                AppNavigation(
                    isDarkTheme = isDarkTheme.value,
                    onThemeToggle = { isDarkTheme.value = !isDarkTheme.value }
                )
            }
        }
    }
}

@Composable
fun AppNavigation(
    isDarkTheme: Boolean,
    onThemeToggle: () -> Unit
) {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "login") {
        composable("login") {
            LoginScreen(navController, isDarkTheme, onThemeToggle)
        }
        composable("register") {
            RegisterScreen(navController)
        }
    }
}
