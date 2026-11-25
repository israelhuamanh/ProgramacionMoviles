package com.tecsup.murocampus.data.remote

import com.tecsup.murocampus.BuildConfig
import com.tecsup.murocampus.data.UsuarioApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object RetrofitClient {
    private val json = Json { ignoreUnknownKeys = true }
    private val cliente = OkHttpClient.Builder()
        .addInterceptor { cadena ->
            val peticion = cadena.request().newBuilder().header("Accept", "application/json").build()
            cadena.proceed(peticion)
        }
        .apply {
            if (BuildConfig.DEBUG) addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
        }
        .build()

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .client(cliente)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }
    val api: PostApi by lazy { retrofit.create(PostApi::class.java) }
    val usuarios: UsuarioApi by lazy { retrofit.create(UsuarioApi::class.java) }
}
