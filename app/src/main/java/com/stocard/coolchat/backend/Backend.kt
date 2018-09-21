package com.stocard.coolchat.backend

import android.util.Log
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.experimental.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object Backend {

    private val okHttpClient by lazy {
        val requestLogger = HttpLoggingInterceptor { message -> Log.d("OkHttp", message) }
                .setLevel(HttpLoggingInterceptor.Level.BODY)

        OkHttpClient.Builder()
                .addInterceptor(requestLogger)
                .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://android-hackschool.herokuapp.com")
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build()
    }

    val instance: BackendService by lazy {
        retrofit.create<BackendService>(BackendService::class.java)
    }

}