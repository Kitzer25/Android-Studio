package com.example.starlinkloginapp.Conexion.Datos

import com.example.starlinkloginapp.Conexion.Models.AuthResponse
import com.example.starlinkloginapp.Conexion.Models.LoginRequest
import com.example.starlinkloginapp.Conexion.Models.RegisterRequest
import com.example.starlinkloginapp.Conexion.Models.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("/auth/signup")
    suspend fun signup(@Body request: RegisterRequest): Response<RegisterResponse>

    @POST("/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>
}