package com.example.starlinkloginapp.ui.screens.principal

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.ui.graphics.vector.ImageVector


sealed class PrincipalDestiny(val route: String, val label: String, val icon: ImageVector) {
    object Camera  : PrincipalDestiny("principal/camera",  "Cámara",  Icons.Default.PhotoCamera)
    object Gallery : PrincipalDestiny("principal/gallery", "Galería", Icons.Default.Collections)
    object Logout  : PrincipalDestiny("principal/logout",  "Salir", Icons.AutoMirrored.Filled.ExitToApp)

    companion object {
        val items = listOf(Camera, Gallery, Logout)
    }
}