package com.example.camarax_aplicativo.MVVM.Data.Repository

import android.provider.ContactsContract.CommonDataKinds.Email
import com.example.camarax_aplicativo.MVVM.Data.Remoto.RetrofitInstance
import com.example.camarax_aplicativo.MVVM.Dominio.Model.AuthResponse
import com.example.camarax_aplicativo.MVVM.Dominio.Model.LoginRequest
import com.example.camarax_aplicativo.MVVM.Dominio.Model.RegisterRequest

class AuthRepository {
    private val api = RetrofitInstance.api

    suspend fun login(email: String, password: String): AuthResponse? {
        val response = api.login(LoginRequest(email, password))
        return if(response.isSuccessful) response.body() else null
    }

    suspend fun register(
        nombre: String,
        apellido: String,
        telelfono: String,
        correo: String,
        password: String
    ): AuthResponse?{
        val response = api.register(RegisterRequest(
            nombre,
            apellido,
            telelfono,
            correo,
            password)
        )
        return if(response.isSuccessful) response.body() else null
    }
}