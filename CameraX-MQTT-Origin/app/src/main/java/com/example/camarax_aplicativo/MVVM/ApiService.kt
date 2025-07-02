package com.example.camarax_aplicativo.MVVM

import androidx.activity.result.contract.ActivityResultContracts
import com.example.camarax_aplicativo.MVVM.Dominio.Model.AuthResponse
import com.example.camarax_aplicativo.MVVM.Dominio.Model.LoginRequest
import com.example.camarax_aplicativo.MVVM.Dominio.Model.RegisterRequest
import okhttp3.MultipartBody
import retrofit2.Response

import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body losginRequest: LoginRequest): Response<AuthResponse>

    @POST("auth/register")
    suspend fun register(@Body registerRequest: RegisterRequest): Response<AuthResponse>

    @Multipart
    @POST("files/upload")
    suspend fun uploadFile(
        @Part file: MultipartBody.Part,
        @Header("Authorization") token: String
    ): Response<Unit>
}