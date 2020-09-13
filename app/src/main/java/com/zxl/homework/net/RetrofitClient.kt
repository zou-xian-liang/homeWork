package com.zxl.homework.net

import com.zxl.homework.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val TIMEOUT_READ = 20L
    private const val TIMEOUT_CONNECTION = 10L

    const val BASE_URL = "https://thoughtworks-ios.herokuapp.com/"
    //    const val BASE_URL = "http://192.168.1.20:8080/project/"
    private val logInterceptor = LogInterceptor()

    val httpClient: OkHttpClient.Builder = if (BuildConfig.DEBUG) OkHttpClient.Builder()
        .addInterceptor(logInterceptor)
        .connectTimeout(TIMEOUT_CONNECTION, TimeUnit.SECONDS)
        .readTimeout(TIMEOUT_READ, TimeUnit.SECONDS)
        .writeTimeout(TIMEOUT_READ, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
    else OkHttpClient.Builder()
        .connectTimeout(TIMEOUT_CONNECTION, TimeUnit.SECONDS)
        .readTimeout(TIMEOUT_READ, TimeUnit.SECONDS)
        .writeTimeout(TIMEOUT_READ, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)

    inline fun <reified T> getService(): T {
        return Retrofit.Builder()
            .client(httpClient.build())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build().create(T::class.java)
    }
}