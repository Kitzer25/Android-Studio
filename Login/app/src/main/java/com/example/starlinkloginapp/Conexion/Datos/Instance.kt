package com.example.starlinkloginapp.Conexion.Datos

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object Instance {
    private const val BASE_URL = "https://programador.arequipa.site/"

    var token: String? = null

    private val  moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    private val client = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        })
        .addInterceptor(Interceptor { chain ->
            val request = chain.request().newBuilder().apply {
                token?.let {
                    addHeader("Authorizacion", "Bearer $it")
                }
            }.build()
            chain.proceed(request)
        })
        .build()

    val api: AuthApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create(AuthApi::class.java)
}