package com.example.starlinkloginapp.Conexion.Models

import com.squareup.moshi.Json

data class RegisterRequest(
    @Json(name = "nombre") val nombre: String,
    @Json(name = "apellido") val apellido: String,
    @Json(name = "correo") val correo: String,
    @Json(name = "contraseña") val contraseña: String
)

data class LoginRequest(
    @Json(name = "correo") val correo: String,
    @Json(name = "contraseña") val contraseña: String
)

data class AuthResponse(
    @Json(name = "token") val token: String,
    @Json(name = "expiresIn") val expiresIn: Int
)

data class RegisterResponse(
    @Json(name = "nombre") val nombre: String,
    @Json(name = "apellido") val apellido: String,
    @Json(name = "correo") val correo: String
)
