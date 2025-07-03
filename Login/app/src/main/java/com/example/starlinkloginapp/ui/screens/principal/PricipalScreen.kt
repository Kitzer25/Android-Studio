package com.example.starlinkloginapp.ui.screens.principal

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.starlinkloginapp.ui.ViewModel.AuthViewModel
import com.example.starlinkloginapp.ui.cameraX.CameraScreen
import com.example.starlinkloginapp.ui.screens.galeria.GalleryScreen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrincipalScreen(
    navController: NavHostController,
    authVM: AuthViewModel
) {
    val innerNav = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar {
                val current by innerNav.currentBackStackEntryAsState()
                val currentRoute = current?.destination?.route

                PrincipalDestiny.items.forEach { dest ->
                    NavigationBarItem(
                        selected = currentRoute == dest.route,
                        onClick = {
                            if (dest == PrincipalDestiny.Logout) {
                                authVM.logout()
                                navController.navigate("welcome") {
                                    popUpTo(0) { inclusive = true }
                                }
                            } else {
                                innerNav.navigate(dest.route) {
                                    launchSingleTop = true
                                }
                            }
                        },
                        icon  = { Icon(dest.icon, null) },
                        label = { Text(dest.label) }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = innerNav,
            startDestination = PrincipalDestiny.Camera.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(PrincipalDestiny.Camera.route) {
                CameraScreen(
                    navController = navController, // usa el root si lo necesitas
                    correoUsuario = authVM.correoUsuario.collectAsState().value ?: ""
                )
            }
            composable(PrincipalDestiny.Gallery.route) {
                GalleryScreen(navController)
            }
        }
    }
}