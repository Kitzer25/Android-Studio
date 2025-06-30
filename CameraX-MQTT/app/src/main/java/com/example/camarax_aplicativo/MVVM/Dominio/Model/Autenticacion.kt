package com.example.camarax_aplicativo.MVVM.Dominio.Model

data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val name: String,
    val apellido: String,
    val telelfono: String,
    val correo: String,
    val password: String
)

data class AuthResponse(
    val token: String,
    val userId: String
)