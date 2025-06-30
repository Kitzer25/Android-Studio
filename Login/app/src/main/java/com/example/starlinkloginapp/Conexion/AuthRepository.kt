package com.example.starlinkloginapp.Conexion

import com.example.starlinkloginapp.Conexion.Datos.Instance
import com.example.starlinkloginapp.Conexion.Models.AuthResponse
import com.example.starlinkloginapp.Conexion.Models.LoginRequest
import com.example.starlinkloginapp.Conexion.Models.RegisterRequest

class AuthRepository {
    private val api = Instance.api

    suspend fun signup(nombre: String, apellido: String, correo: String, contraseña: String): String {
        val request = RegisterRequest(nombre, apellido, correo, contraseña)
        val response = api.signup(request)

        return if (response.isSuccessful && response.body() != null) {
            "REGISTERED"
        } else {
            "Error: ${response.errorBody()?.string() ?: "Desconocido"}"
        }
    }

    suspend fun login(req: LoginRequest): Result<AuthResponse> =
        try {
            val res = api.login(req)
            if(res.isSuccessful) Result.success(res.body()!!)
            else Result.failure(Exception("Credenciales incorrectas"))
        } catch (e: Exception){
            Result.failure(e)
        }
}